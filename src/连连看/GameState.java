package 连连看;

import java.io.Serializable;

public class GameState implements Serializable {
    private String count;
    private int[][] map;
    GameState(String count,int[][]map){
        this.count=count;
        this.map=map;
    }

    public String getCount(){
        return count;
    }
    public int[][] G_getMap(){
        return map;
    }


}
