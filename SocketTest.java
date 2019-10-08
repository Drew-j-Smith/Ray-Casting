import javax.swing.*;
import java.io.IOException;
import java.util.Scanner;

public class SocketTest extends Thread {

    private Player player;

    public SocketTest(Player player){
        this.player = player;
    }

    public void run() {
        String ans;
        //Scanner reader = new Scanner(System.in);
        ans = JOptionPane.showInputDialog("Server or Client? (s/c)");


        if (ans.equals("s")) {
            Server server = new Server();


            new Thread(new Runnable() {
                @Override
                public void run() {
                    server.startServer(5000);
                }
            }).start();


            while (true){
                //String message = reader.nextLine();
                //if (message.equals(""))
                //    message = "server";
                //server.setMessage(message);
                server.setMessage(player.getCenter().toString());
                //System.out.println(server.getUsernames().toString());
                //System.out.println(server.getUserStrings().toString());
            }



        }
        else {
            Client client = new Client();
            System.out.println("Enter Ip");
            String ip = JOptionPane.showInputDialog("Enter IP Address");
            if (ip.equals("localhost"))
                ip = "127.0.0.1";

            String finalIp = ip;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            //client.startClient("127.0.0.1", 5000, "Username");
                            client.startClient(finalIp, 5000, "Username");

                            break;
                        } catch (IOException e) {
                            e.printStackTrace();
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }).start();


            while (!client.isConnected()){
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Connected");
            System.out.println(client.getUsername());

            while (client.isConnected()){
                //String message = reader.nextLine();
                //if (message.equals(""))
                //    message = "client";
                //client.setMessage(message);

                //System.out.println(client.getReceivedMessage());
                //System.out.println(client.getPingTime());
                //System.out.println(client.isConnected());

                Point p;
                try {
                    p = new Point(client.getReceivedMessage());
                    player.setCenter(p);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(client.getReceivedMessage());
                }
            }

        }


    }

}
