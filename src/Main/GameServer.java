package Main;

import Entity.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
public class GameServer extends JFrame{

    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    private static JLabel heading = new JLabel("Chat Area");
    private static JTextArea messageArea = new JTextArea();
    private static JTextField messageInput = new JTextField();
    private static Font font = new Font("Roboto", Font.PLAIN, 20);
    static JFrame window = new JFrame("Pixy Green");
    static JPanel win = new JPanel();
    public static int newgame = 0;
    public GameServer(){

        try {
            CreateGUI();
            server = new ServerSocket(7777);
            System.out.println("Server is read to accept ");
            socket = server.accept();
            InputStreamReader in = new InputStreamReader(socket.getInputStream());
            br = new BufferedReader(in);
            out = new PrintWriter(socket.getOutputStream());

            handleEvents();
            startReading();
            startWriting();


        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public void handleEvents(){
        messageInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

                if(e.getKeyCode()==10){
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me : "+ contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }
        });
    }
    private void CreateGUI(){


        win.setSize(700,480);
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        messageArea.setRows(15);
        messageArea.setColumns(15);
        win.setLayout(new BorderLayout());

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        win.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        win.add(jScrollPane, BorderLayout.CENTER);
        win.add(messageInput, BorderLayout.SOUTH);



        JPanel firstPanel = new GamePanel();
        JPanel secondPanel = win;
        window.setLayout(new FlowLayout());
        window.add(firstPanel);
        window.add(secondPanel);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setVisible(true);
        window.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                newgame=1;
                GameClient.newgame = 0;
                System.out.println("Game Server "+newgame+" "+GameClient.newgame);
                firstPanel.requestFocus();
                firstPanel.requestFocusInWindow();
                firstPanel.setRequestFocusEnabled(true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                newgame=1;
                GameClient.newgame = 0;
                System.out.println("Game Server "+newgame+" "+GameClient.newgame);
                firstPanel.requestFocus();
                firstPanel.requestFocusInWindow();
                firstPanel.setRequestFocusEnabled(true);

            }
        });
    }

    public void startReading(){
        Runnable r1 =()->{
            System.out.println("Reader Started");
            try {
                while(true){

                    String msg =  br.readLine();
                    if(msg.equals("exit")){
                        System.out.println("Server Terminated gthe Chat");
//                        JOptionPane.showMessageDialog(this, "Server Terminated The Chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    messageArea.append("Player : "+ msg+"\n");
                    System.out.println("Player : " + msg);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(r1).start();
    }
    public void startWriting(){
        Runnable r2 =()->{
            System.out.println("writiting Started");
            try {
                while(!socket.isClosed()){

                    InputStreamReader in  = new InputStreamReader(System.in);
                    BufferedReader br1 = new BufferedReader(in);
                    String content = br1.readLine();

                    out.println(content);
                    out.flush();
                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(r2).start();
    }
    public static void updatestaticint(){
//    if(flag==0) {
//flag++;
        window.getContentPane().removeAll();
//     window.revalidate();
//     window.repaint();

          System.out.println("Dialouge box");
        String description = "Your Score is " +String.valueOf(Player.getScore())+"\nDO You Want to Play Again?";
        int a = JOptionPane.showConfirmDialog(null, description, "Pixy Green", JOptionPane.YES_NO_OPTION);

        if (a == 0){
             CreateGuiAgain();
        } else if (a == 1) {
            System.exit(1);
        }



    }
    public static void main(String[] args){
        System.out.println("This is Server");
        new GameServer();
    }

    public static void CreateGuiAgain(){
   win.setSize(700,480);
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        messageArea.setRows(15);
        messageArea.setColumns(15);
        win.setLayout(new BorderLayout());

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        win.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        win.add(jScrollPane, BorderLayout.CENTER);
        win.add(messageInput, BorderLayout.SOUTH);



        JPanel firstPanel = new GamePanel();
        JPanel secondPanel = win;
        window.setLayout(new FlowLayout());
        window.add(firstPanel);
        window.add(secondPanel);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        firstPanel.setFocusable(true);
        firstPanel.requestFocusInWindow();
        window.pack();
        window.setVisible(true);
        window.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                firstPanel.requestFocus();
                firstPanel.requestFocusInWindow();
                firstPanel.setRequestFocusEnabled(true);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                firstPanel.requestFocus();
                firstPanel.requestFocusInWindow();
                firstPanel.setRequestFocusEnabled(true);

            }
        });

    }
}
