import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class GUIServer {

    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    private JFrame frame;
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private String username;

    public GUIServer() {
        initializeUI();
        try {
            server = new ServerSocket(7777);
            appendToChatArea("Server is ready to accept the connection");
            appendToChatArea("Waiting...");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeUI() {
        frame = new JFrame();
        frame.setTitle("Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.getContentPane().setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        frame.getContentPane().add(inputPanel, BorderLayout.SOUTH);
        inputPanel.setLayout(new GridLayout(0, 2, 0, 0));

        messageField = new JTextField();
        messageField.setEnabled(false);
        inputPanel.add(messageField);

        sendButton = new JButton("Send");
        sendButton.setEnabled(false);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        inputPanel.add(sendButton);

        JLabel usernameLabel = new JLabel("Username:");
        inputPanel.add(usernameLabel);

        JTextField usernameField = new JTextField();
        inputPanel.add(usernameField);

        JButton connectButton = new JButton("Connect");
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usernameInput = usernameField.getText();
                if (!usernameInput.isEmpty()) {
                    username = usernameInput;
                    connectButton.setEnabled(false);
                    usernameField.setEditable(false);
                }
                try {
                    socket = server.accept();
                    appendToChatArea("[Server]: Client connected");
                    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintWriter(socket.getOutputStream());
                    startReading();
                    messageField.setEnabled(true);
                    sendButton.setEnabled(true);
                    messageField.requestFocus();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        inputPanel.add(connectButton);

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shutdown();
            }
        });
        inputPanel.add(quitButton);

        frame.setVisible(true);
    }

    private void shutdown() {
        try {
            server.close();
            socket.close();
            br.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private void appendToChatArea(String message) {
        chatArea.append(message + "\n");
    }

    public void startReading() {
        Runnable reader = () -> {
            appendToChatArea("Started reading...");
            try {
                while (true) {
                    String message = br.readLine();
                    if (message == null) {
                        appendToChatArea("Client has disconnected.");
                        shutdown();
                        break;
                    }
                    appendToChatArea(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
                shutdown();
            }
        };

        new Thread(reader).start();
    }

    public void startWriting() {
        Runnable writer = () -> {
            try {
                BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));

                while (true) {
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        new Thread(writer).start();
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            if (message.startsWith("@username:")) {
                out.println(message);
            } else {
                out.println(username + ": " + message);
            }
            out.flush();
            messageField.setText("");
        }
    }

    public static void main(String[] args) {
        System.out.println("Going to start the server");
        Thread serverThread = new Thread(() -> new GUIServer());

        System.out.println("Client is started");
        Thread clientThread = new Thread(() -> new GUIClient());

        serverThread.start();
        clientThread.start();
    }
}