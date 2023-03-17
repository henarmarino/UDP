import java.io.*;
import java.util.*;
import java.net.*;
import java.util.Scanner;
import java.net.SocketTimeoutException;


public class udpcli {

	public static void main (String [] args){  //java udpcli ip_address port_number

		if (args.length!=2) {
			System.out.println("You have to enter the parameters with this format: udpcli ip_address port_number");
		} else {
			try {
				//to read
				Scanner keyboard = new Scanner (System.in);

				InetAddress ipAddress = InetAddress.getByName(args[0]); //args[0] is the ip_address
				String port = args[1]; //args[1] is the port_number
				int portServer = Integer.parseInt(port);

				System.out.println("Enter a row of numbers separated by spaces. The 0 or the enter means the end of the message:");
				String clientNumbers= keyboard.nextLine(); 
				String separatedNumbers[] = clientNumbers.split(" "); //numbers separated by spaces

				if(Integer.parseInt(separatedNumbers[0])==0){
					System.out.println("The 0 means end of the message, so it cannot be the first number");
					System.exit(-1);
				}

				String numbersToSend = "";
				//put the numbers into the string to send them
				for (int i=0; i<separatedNumbers.length; i++) {
					Integer number = Integer.parseInt(separatedNumbers[i]);
					if (number != 0) {
						numbersToSend+= separatedNumbers[i]+" ";
					} else
						break;
				}

			    byte[] bytes = new byte[numbersToSend.length()];
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				DataOutputStream dataOutputStream = new DataOutputStream(outputStream); 
				dataOutputStream.writeChars(numbersToSend);
				bytes = numbersToSend.getBytes();
				dataOutputStream.close(); 

				//to connect to the server
				DatagramSocket socketClient = new DatagramSocket();

				//send the packet with the message to the server
				DatagramPacket packetMessage = new DatagramPacket(bytes, bytes.length, ipAddress, portServer);
				socketClient.send(packetMessage);

				//receive the answer of the server
				byte[] informationServer = new byte[1024];
				DatagramPacket packetAnswer = new DatagramPacket(informationServer, informationServer.length);

				//There is a limitation of time for the answer of the server. The time is 10 seconds.
				socketClient.setSoTimeout(10000);
				socketClient.receive(packetAnswer);

				String answer= new String(packetAnswer.getData());
				System.out.println("The accumulator received by the server is: " + answer);

				//we close the socket
				socketClient.close();

				} catch(SocketTimeoutException exception1) { //Error when the server has taken more than 10 seconds to answer
					System.out.println("[ERROR] Time server: The server has taken more than 10 seconds to answer");
					System.exit(-1);
				} catch (SocketException exception2) {
					System.out.println("[ERROR] Socket: " + exception2.getMessage());
				} catch (IOException exception3) {
					System.out.println("[ERROR] IO: " + exception3.getMessage()); 
				}  

		}//end else
	}

}//end class