import java.io.*;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;

public class Client {

    private String message;
    private PingTime pingTime;
    private String receivedMessage;
    private String username;
    private boolean connected;

    public Client(){
        message = "This the default message if you see this either someone is lazy or someone did something wrong";
        pingTime = new PingTime();
        receivedMessage = null;
        username = null;
        connected = false;
    }

    public void startClient(String address, int port, String username) throws IOException {

        //region initialization
        Socket sock = new Socket(address, port);

        OutputStream outputStream = sock.getOutputStream();
        InputStream inputStream = sock.getInputStream();

        PrintWriter printWriter = new PrintWriter(outputStream, true);
        BufferedReader receiveRead = new BufferedReader(new InputStreamReader(inputStream));

        String receivedMessage, sendMessage;
        Timestamp pastPing;
        Timestamp now;
        //endregion

        //region username assignment
        printWriter.println(username);
        printWriter.flush();

        if((username = receiveRead.readLine()) != null)
        {
            this.username = username;
        }

        //endregion

        connected = true;

        //main loop
        while(true) {
			try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
			
            sendMessage = username + ": " + message;
            pastPing = Timestamp.valueOf(LocalDateTime.now());
            printWriter.println(sendMessage);
            printWriter.flush();


            try {
                if((receivedMessage = receiveRead.readLine()) != null)
                {
                    this.receivedMessage = receivedMessage;
                    now = Timestamp.valueOf(LocalDateTime.now());
                    pingTime.add(now.getTime() - pastPing.getTime(), now.getTime());
                    if (receivedMessage.equals("Exit"))
                        break;
                }
            }
            catch (IOException e){
                e.printStackTrace();
                if(e.getMessage().equals("Connection reset"))
                    break;
            }

            if (message.equals("Exit"))
                break;
        }


        sock.close();
        outputStream.close();
        printWriter.close();
        connected = false;
    }

    //region sets and gets
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getPingTime() {
        return pingTime.calculatePingTime();
    }

    public void setPingTime(PingTime pingTime) {
        this.pingTime = pingTime;
    }

    public String getReceivedMessage() {
        return receivedMessage;
    }

    public String getUsername() {
        return username;
    }

    public boolean isConnected() {
        return connected;
    }

    //endregion

    //private class
    private class PingTime {
        private long[] pingTimes = new long[1000];
        private long[] timeStamps = new long[1000];

        public PingTime(){
            Arrays.fill(pingTimes, -1);
        }

        public void add(long pingTime, long time){
            for (int i = 0; i < pingTimes.length; i++){
                if (pingTimes[i] == -1){
                    pingTimes[i] = pingTime;
                    timeStamps[i] = time;
                    break;
                }
            }
        }

        public float calculatePingTime(){
            float time = Timestamp.valueOf(LocalDateTime.now()).getTime();
            float total = 0;
            int numPingTimes = 0;
            for (int i = 0; i < pingTimes.length; i++){
                if (time - timeStamps[i] > 5000){
                    pingTimes[i] = -1;
                }
                if (pingTimes[i] != -1) {
                    total += pingTimes[i];
                    numPingTimes++;
                }
            }
            return total / (float) numPingTimes;
        }

    }

}