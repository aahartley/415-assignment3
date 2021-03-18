
/*****
 **
 **  USCA ACSC492F
 **
 */

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class EmailClient {

	Socket clientSocket;
	Scanner In;
	DataOutputStream Out;

	// A email sender
	public EmailClient(String Host, int Port) {

		try {

			// Establish a TCP connection with the mail server.
			clientSocket = new Socket(Host, Port);

			// Create a Scanner to read a line at a time.
			In = new Scanner(clientSocket.getInputStream());

			// Get a reference to the socket's output stream.
			Out = new DataOutputStream(clientSocket.getOutputStream());

			// Read greeting from the server.
			String response = In.nextLine();
			System.out.println(response);
			if (!response.startsWith("220")) {
				throw new Exception("220 reply not received from server. ");
			}

			// Send HELO command and get server response.
			String message = "HELO usca.edu\r\n";
			System.out.print(message);
			Out.writeBytes(message);
			response = In.nextLine();
			System.out.println(response);
			if (!response.startsWith("250")) {
				throw new Exception("250 reply not received from server. Handshaking failed.");
			}

			// Send MAIL FROM command.
			message = "MAIL FROM: cindy@usca.edu\r\n";
			System.out.print(message);
			Out.writeBytes(message);
			response = In.nextLine();
			System.out.println(response);
			if (!response.startsWith("250")) {
				throw new Exception("250 reply not received from server. MAIL FROM not accepted");
			}

			// Send RCPT TO command.
			message = "RCPT TO: red6tigera@gmail.com\r\n";
			System.out.println(message);
			Out.writeBytes(message);
			response = In.nextLine();
			System.out.println(response);
			if(!response.startsWith("250"))
				throw new Exception("250 reply not recieved from server. RCPT TO not accepted");


			// Send DATA command.
			message = "DATA\r\n";
			System.out.println(message);
			Out.writeBytes(message);
			response = In.nextLine();
			System.out.println(response);
			if(!response.startsWith("354"))
				throw new Exception("354 reply not recieved from server. DATA not accepted");
			


			// Send message data.
			Path inFilePath = Paths.get("uscaicon.jpg");
        	byte[] buffer = Files.readAllBytes(inFilePath);
        	String base64 = new String(Base64.getEncoder().encodeToString(buffer));
			message = "From: cindy@usca.edu\r\nTo: red6tigera@gmail.com\r\nSubject: hello\r\n"+
        	"MIME-Version: 1.0\r\nContent-Type: multipart/alternative; boundary=\"---=_NEXT_11c94f24691\"\r\n"+
					"-----=_NEXT_11c94f24691\r\nContent-Type: text/plain\r\nhello hello\r\n"+
        	"-----=_NEXT_11c94f24691\r\nContent-Transfer-Encoding: base64\r\nContent-Type: image/jpeg\r\n"
        			+base64+ "\r\n.\r\n";
			System.out.println(message);
			Out.writeBytes(message);
			response = In.nextLine();
			System.out.println(response);
			if(!response.startsWith("250"))
				throw new Exception("250 reply not recieved from server. message not accepted");


			// Send QUIT command.
			message = "Quit\r\n";
			System.out.println(message);
			Out.writeBytes(message);
			response = In.nextLine();
			System.out.println(response);
			if(!response.startsWith("221"))
				throw new Exception("221 reply not recieved from server. connecton not closed");
		

			// close socket and all streams
			if (clientSocket != null)
				clientSocket.close();
			if (In != null)
				In.close();
			if (Out != null)
				Out.close();

		} catch (Exception e) {
			System.err.print(e.getMessage());
		}

	}

	// Send message and get response from mail server
	public String send(String message) throws IOException {
		System.out.print(message);
		Out.writeBytes(message);
		String response = In.nextLine();
		System.out.println(response);
		return response;
	}

	public static void main(String argv[]) throws Exception {
		EmailClient p = new EmailClient("129.252.199.151", 25);
	}
}