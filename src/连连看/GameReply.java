package 连连看;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
public class GameReply{
    public List<GameState> moves;
    //GameState g=new GameState(GameClient.textField.getText(), GameClient.panel2.get_Map());
    public GameReply(){
        this.moves=new ArrayList<>();
    }
    public void add(GameState g){
        moves.add(g);
    }
    public void reply(){
        JFrame f=new JFrame("回放");
        for(GameState g:moves){
        //GameState g=new GameState(GameClient.textField.getText(),GameClient.panel2.get_Map());
            GamePanel panel0=new GamePanel(g);
            panel0.setLayout(new BorderLayout());
            f.getContentPane().add(panel0,BorderLayout.CENTER);
            f.setSize(600,630);
            try{
                Thread.sleep(500);
            }catch (InterruptedException e){e.printStackTrace();}
        }
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }


}
