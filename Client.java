import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;



public class Client extends JFrame{

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    //Declare Components
    public JLabel heading = new JLabel("Client Area");
    public JTextArea messageArea = new JTextArea();
    public JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN, 20);

    //constructor
    public Client(){
        try {
            System.out.println("Sending request to server");
            socket = new Socket("127.0.0.1",7777);
            System.out.println("Connection done.");

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();
            startReading();
            // startWriting();

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void handleEvents(){
        messageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                // System.out.println("Key released:"+e.getKeyCode());
                if(e.getKeyCode()==10){
                    // System.out.println("You have pressed Enter Button");

                    //messageInput field ka text kaise nikalenge
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me: "+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }

        });
    }

    private void createGUI(){
        //GUI
        this.setTitle("Client Messager[END]");
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("icon.jpg"));
        heading.setSize(50, 60);
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        //frame ka layout set karenge
        this.setLayout(new BorderLayout());


        //adding the component to frame
        this.add(heading, BorderLayout.NORTH);
        // JScrollPane JScrollPane = new JScrollPane();
        // JScrollBar ScrollBar = new JScrollBar();
        this.add(messageArea, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);




        this.setVisible(true);

    }

    public void startReading(){
        // thread-read karke deta rahega
        Runnable r1 = () -> {
            System.out.println("reader started..");

            try {
                while(true){
                
                    String msg = br.readLine();
                    if(msg.equals("exit")){
                        System.out.println("Server terminated the chat");
                        JOptionPane.showMessageDialog(this, "Server terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    // System.out.println("Server : "+msg);
                    messageArea.append("Server : "+msg+"\n");
                
                }
            } catch (Exception e) {
                // TODO: handle exception
                System.out.println("Connection Closed");
            }
            
        };

        new Thread(r1).start();
    }
    //Comment

    public void startWriting(){
        // threaad- data ko user se lega and then send karega client tak
        Runnable r2 = () -> {
            System.out.println("Writer Started");

            try {
                while(!socket.isClosed()){
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine(); 
                    out.println(content);
                    out.flush();

                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }

   
                }
                System.out.println("Connection Closed");
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            
        };
        new Thread(r2).start();
    }


    public static void main(String[] args) {
        System.out.println("this is client..");
        new Client();
    }
}
