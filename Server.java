import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.*;

class Server extends JFrame{
    ServerSocket server;
    Socket socket;
    BufferedReader in;
    PrintWriter ou  ;
    private JLabel title=new JLabel("Server Side");
    private JTextArea messagearea=new JTextArea();
    private JTextField messageinput=new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);


    public Server(){
        try {
            server= new ServerSocket(7777);
            System.out.println("Server ready to Connect");
            System.out.println("Waiting...");
            socket=server.accept();
            in= new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ou= new PrintWriter(socket.getOutputStream());
            gui();
            event();
            readstart();
            //readstart();
            //writestart();
        } catch (Exception e) {
        }
        

    }
    private void event() {
        messageinput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method s
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                if(e.getKeyCode()==10){
                    String msgtosend=messageinput.getText();
                    ou.println(msgtosend);
                    ou.flush();
                    messagearea.append("Me: "+msgtosend+"\n");
                    messageinput.setText("");
                    messageinput.requestFocus();
                    
                }
            }
            
        });
    }
    public void gui(){
        this.setTitle("- - - SERVER END - - -");
        this.setSize(700,500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        title.setFont(font);
        messagearea.setFont(font);
        messageinput.setFont(font);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        this.setLayout(new BorderLayout());
        this.add(title,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messagearea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageinput,BorderLayout.SOUTH);
        messagearea.setEditable(false);

        this.setVisible(true);

        

    }
    public static void main(String[] args) {
        System.out.println("This is SERVER. Please wait...."); 
        new Server();
    }
    public void readstart(){
        Runnable r1=()->{
            System.out.println("Reader Started----");
            while(true){
                try{
                String msg= in.readLine();
                if(msg.equals("/end")){
                    System.out.println("User ended the conversation");
                    JOptionPane.showMessageDialog(this, ":Client ended the conversation:-(");
                    messageinput.setEnabled(false);
                    break;
                }
                //System.out.println("User:   "+msg);
                messagearea.append("Client:   "+msg+"\n");
            }catch(Exception e){
                e.printStackTrace();
            }
            }

        };
        new Thread(r1).start();

    }
    public void writestart(){
        Runnable r2=()->{
            System.out.println("Writer Started----");
            while(true){
                try {
                    BufferedReader in1=new BufferedReader(new InputStreamReader(System.in));   
                    String sentmsg=in1.readLine();
                    ou.println(sentmsg);
                    ou.flush();
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        };
        new Thread(r2).start();

    }
}