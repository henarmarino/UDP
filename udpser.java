import java.io.*;
import java.util.*;
import java.net.*;

public class udpser {

    public static void main (String [] args){ //java udpser port_number

        if(args.length!=1){
            System.out.println("You have to enter the parameters with this format: udpser port_number");
        } else {
            try{

                int accumulator=0;
                String port = args[0]; //args[0] is the port_number
                int portServer = Integer.parseInt(port);

                //to connect to the client
                DatagramSocket socketServer = new DatagramSocket(portServer);

                while(true){

                    //receive the packet of the client
                    byte[] informationClient = new byte[1024];
                    DatagramPacket packetAsk = new DatagramPacket(informationClient, informationClient.length);
                    socketServer.receive(packetAsk);
                    
                    //add number by number to the accumulator
                    String ask= new String(packetAsk.getData());
                    String askBySpace[] = ask.trim().split(" ");
                    for (int i=0; i<askBySpace.length; i++) {
                        int number = Integer.parseInt(askBySpace[i]);
                        accumulator += number;
                        System.out.println("The accumulator is: " + accumulator);
                    }

                    //get the address,port and message of the client
                    InetAddress addressClient = packetAsk.getAddress();
                    int portClient = packetAsk.getPort();
                    String infoAccumulator = Integer.toString(accumulator);
                    byte[] message = infoAccumulator.getBytes();

                    //send the packet to the client
                    DatagramPacket packetAnswer = new DatagramPacket( message, message.length , addressClient, portClient);
                    socketServer.send(packetAnswer);

                } //end while
                } catch (SocketException exception1) {
                    System.out.println("[ERROR] Socket: " + exception1.getMessage());
                } catch (IOException exception2) {
                    System.out.println("[ERROR] IO: " + exception2.getMessage()); 
                }  
        } //end else
    } //end main
} //end class