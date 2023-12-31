package 连连看;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;

import javax.swing.*;

public class GameClient extends JFrame{
    GamePanel panel2 = new GamePanel(10);
    JButton button1 = new JButton("重新开始");
    JButton button2 = new JButton("退出");
    JButton button3 = new JButton("replay");
    static JTextField textField = new JTextField(10);
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8888;

    public GameClient(){
        JLabel label1 = new JLabel("已消去方块数量：");
        JLabel label2=new JLabel("按D提示");
        JPanel panel = new JPanel(new BorderLayout());
        textField.setEditable(false);

        loadState();
        System.out.println(gameState.getCount());
        //panel2=new GamePanel(gameState);
        textField.setText(gameState.getCount());

        panel2.setLayout(new BorderLayout());
        panel.setLayout(new FlowLayout());
        panel.add(label1);
        panel.add(textField);
        panel.add(button1);
        panel.add(button2);
        panel.add(button3);
        panel.add(label2);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(panel,BorderLayout.SOUTH);
        this.getContentPane().add(panel2,BorderLayout.CENTER);
        this.setSize(600,630);
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveState();
                dispose();
            }
        });

        this.setTitle("连连看游戏");
        this.setVisible(true);
        button1.setEnabled(true);
        button2.setEnabled(true);
        button3.setEnabled(true);

        button1.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                textField.setText("0");
                panel2.startNewGame();
            }
        });

        button2.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                saveState();
                System.exit(0);
            }
        });

        button3.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                textField.setText("0");
                panel2.replay();
            }
        });
    }

    private GameState gameState;
    public void saveState(){
        try{
            gameState=new GameState(textField.getText(), panel2.get_Map());
            FileOutputStream fileout=new FileOutputStream("gamestate.txt");
            ObjectOutputStream out =new ObjectOutputStream(fileout);
            out.writeObject(gameState);
            out.close();
            fileout.close();
            System.out.println("saved");
        }catch (Exception e){e.printStackTrace();}

        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            //gameState = new GameState(textField.getText(), panel2.get_Map());

            // 向服务器发送游戏状态数据
            out.writeObject(gameState);
            out.flush();

            // 接收服务器的响应
            String response = (String) in.readObject();
            System.out.println(response);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadState(){
        try{
            FileInputStream filein=new FileInputStream("gamestate.txt");
            ObjectInputStream in=new ObjectInputStream(filein);
            gameState=(GameState)in.readObject();
            in.close();
            filein.close();
            System.out.println("load");
        }catch (Exception e){e.printStackTrace();}
    }

    public static void main(String[] args) {
        new GameClient();
    }

}
