//Q6 Write a program for a simple GUI based chat application between client and server.
import java.io.*;
import java.net.*;

public class ChatClient {
    public static void main(String[] args) throws Exception {
        Socket sock = new Socket("127.0.0.1", 3000);
        BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
        OutputStream ostream = sock.getOutputStream();
        PrintWriter pwrite = new PrintWriter(ostream, true);
        InputStream istream = sock.getInputStream();
        BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));
        System.out.println("Start the chitchat, type and press Enter key");
        String receiveMessage, sendMessage;
        while (true) {
            sendMessage = keyRead.readLine(); // keyboard reading
            pwrite.println(sendMessage); // sending to server
            pwrite.flush(); // flush the data
            if ((receiveMessage = receiveRead.readLine()) != null) //receive from server
            {
                System.out.println(receiveMessage); // displaying at DOS prompt
            }
        }
    }
}