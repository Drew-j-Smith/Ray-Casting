import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {

    private String message;
    private ArrayList<String> usernames;
    private HashMap<String, String> userStrings;


    public Server(){
        usernames = new ArrayList<String>();
        userStrings = new HashMap<String, String>();
        message = "This the default message if you see this either someone is lazy or someone did something wrong";
    }

    public void startServer(int port) {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // new thread for a client
            new EchoThread(socket).start();
        }
    }



    //region sets and gets
    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<String> getUsernames() {
        return usernames;
    }

    public HashMap<String, String> getUserStrings() {
        return userStrings;
    }
    //endregion



    //private classes
    private class EchoThread extends Thread{
        private Socket socket;

        private EchoThread(Socket clientSocket) {
            this.socket = clientSocket;
        }

        public void run() {

            System.out.println("Connected");

            //region initialization
            OutputStream outputStream = null;
            InputStream inputStream = null;

            try {
                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            PrintWriter printWriter = new PrintWriter(outputStream, true);
            BufferedReader receiveRead = new BufferedReader(new InputStreamReader(inputStream));

            String receiveMessage, username = "";

            //endregion

            //region username assignment
            try {
                if ((username = receiveRead.readLine()) != null) {

                    boolean isUnique; String tempUsername = username; int addedNum = 1;
                    do {
                        isUnique = true;
                        for (String name : usernames) {
                            if (name.equals(tempUsername)){
                                isUnique = false;
                            }
                        }

                        if (!isUnique){
                            tempUsername = username + addedNum;
                            addedNum++;
                        }

                    }  while (!isUnique);

                    username = tempUsername;



                    usernames.add(username);
                    userStrings.put(username, null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                if(e.getMessage().equals("Connection reset"))
                    return;
            }

            printWriter.println(username);
            printWriter.flush();
            //endregion

            System.out.println("The username is: " + username);


            //main loop
            while (true) {
                try {
                    if ((receiveMessage = receiveRead.readLine()) != null) {
                        receiveMessage = receiveMessage.substring(username.length() + 2);
                        userStrings.put(username, receiveMessage);
                        if (receiveMessage.equals("Exit")){
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if(e.getMessage().equals("Connection reset"))
                        break;

                }

                printWriter.println(message);
                printWriter.flush();
                if (message.equals("Exit"))
                    break;
            }

            usernames.remove(username);
            userStrings.remove(username);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

