package 连连看;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
import java.util.List;



public class GamePanel extends JPanel implements ActionListener ,MouseListener,KeyListener{

    private Image[] pics;
    private final int n;

    //private  int k=0;

    private int[][] map;

    private int[][] map1=new int[10][10];
    //private int[][] map2=new int[10][10];


    private final int leftX = 40;
    private final int leftY = 32;
    private boolean isClick = false;
    private int clickId,clickX,clickY;
    private int linkMethod;
    private Node z1,z2;
    private Map mapUtil;
    public int count = 0;
    public int count1 = 0;
    private int k=0;

    //private List<String> operationRecord;
    private List<String> operationRecord = new ArrayList<>();

    //public GameState gameState;


    public static final int LINKBYHORIZONTAL = 1,LINKBYVERTICAL = 2,LINKBYONECORNER = 3,LINKBYTWOCORNER = 4;
    public static final int BLANK_STATE = -1;

    public GamePanel(int count){
        setSize(600, 600);
        n = 10;
        mapUtil = new Map(count, n);
        map = mapUtil.getMap();
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                map1[i][j]=map[i][j];
            }
        }
        count1=count;

        this.setVisible(true);
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.setFocusable(true);
        getPics();
        repaint();
    }
    public GamePanel(GameState gameState){
        setSize(600, 600);
        n=10;
        count=Integer.parseInt(gameState.getCount());
        count1=count;
        map=gameState.G_getMap();
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                map1[i][j]=map[i][j];
            }
        }

        this.setVisible(true);
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.setFocusable(true);
        getPics();
        repaint();
    }

    private void getPics() {
        pics = new Image[10];
        for(int i=0;i<=9;i++){
            pics[i] = Toolkit.getDefaultToolkit().getImage("pic"+(i+1)+".png");
        }
    }

    public void paint(Graphics g){
        g.clearRect(0, 0, 800, 30);

        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                if(map[i][j]!=BLANK_STATE){
                    g.drawImage(pics[map[i][j]],leftX+j*50,leftY+i*50,50,50,this);
                }else{
                    g.clearRect(leftX+j*50,leftY+i*50,50,50);
                }
            }
        }
        k=1;

    }
    //水平方向的消除
    private boolean horizontalLink(int clickX1, int clickY1, int clickX2, int clickY2) {

        if(clickY1>clickY2){
            int temp1 = clickX1;
            int temp2 = clickY1;
            clickX1 = clickX2;
            clickY1 = clickY2;
            clickX2 = temp1;
            clickY2 = temp2;
        }

        if(clickX1==clickX2){

            for(int i=clickY1+1;i<clickY2;i++){
                if(map[clickX1][i]!=BLANK_STATE){
                    return false;
                }
            }

            linkMethod = LINKBYHORIZONTAL;
            return true;
        }


        return false;
    }

    //垂直方向的消除
    private boolean verticalLink(int clickX1, int clickY1, int clickX2, int clickY2) {

        if(clickX1>clickX2){
            int temp1 = clickX1;
            int temp2 = clickY1;
            clickX1 = clickX2;
            clickY1 = clickY2;
            clickX2 = temp1;
            clickY2 = temp2;
        }

        if(clickY1==clickY2){

            for(int i=clickX1+1;i<clickX2;i++){
                if(map[i][clickY1]!=BLANK_STATE){
                    return false;
                }
            }

            linkMethod = LINKBYVERTICAL;

            return true;
        }
        return false;
    }

    //一个折角的消除
    private boolean oneCornerLink(int clickX1, int clickY1, int clickX2, int clickY2) {

        if(clickY1>clickY2){
            int temp1 = clickX1;
            int temp2 = clickY1;
            clickX1 = clickX2;
            clickY1 = clickY2;
            clickX2 = temp1;
            clickY2 = temp2;
        }

        if(clickX1<clickX2){

            if(map[clickX1][clickY2]==BLANK_STATE && horizontalLink(clickX1, clickY1, clickX1, clickY2)&&verticalLink(clickX2,clickY2,clickX1,clickY2)){
                linkMethod = LINKBYONECORNER;
                z1 = new Node(clickX1, clickY2);
                return true;
            }


            if(map[clickX2][clickY1]==BLANK_STATE&&horizontalLink(clickX2, clickY2, clickX2, clickY1)&&verticalLink(clickX1,clickY1,clickX2, clickY1)){
                linkMethod = LINKBYONECORNER;
                z1 = new Node(clickX2, clickY1);
                return true;
            }

        }else{

            if(map[clickX2][clickY1]==BLANK_STATE&&horizontalLink(clickX2, clickY2, clickX2, clickY1)&&verticalLink(clickX1,clickY1,clickX2, clickY1)){
                linkMethod = LINKBYONECORNER;
                z1 = new Node(clickX2, clickY1);
                return true;
            }

            if(map[clickX1][clickY2]==BLANK_STATE&&horizontalLink(clickX1, clickY1, clickX1, clickY2)&&verticalLink(clickX2,clickY2,clickX1, clickY2)){
                linkMethod = LINKBYONECORNER;
                z1 = new Node(clickX1, clickY2);
                return true;
            }

        }

        return false;
    }

    //两个折角的消除
    private boolean twoCornerLink(int clickX1, int clickY1, int clickX2, int clickY2) {

        for(int i=clickX1-1;i>=-1;i--){

            if(i==-1&&throughVerticalLink(clickX2, clickY2, true)){
                z1 = new Node(-1, clickY1);
                z2 = new Node(-1, clickY2);
                linkMethod = LINKBYTWOCORNER;
                return true;
            }

            if(i>=0&&map[i][clickY1]==BLANK_STATE){

                if(oneCornerLink(i, clickY1, clickX2, clickY2)){
                    linkMethod = LINKBYTWOCORNER;
                    z1 = new Node(i, clickY1);
                    z2 = new Node(i, clickY2);
                    return true;
                }


            }else{
                break;
            }

        }

        for(int i=clickX1+1;i<=n;i++){

            if(i==n&&throughVerticalLink(clickX2, clickY2, false)){
                z1 = new Node(n, clickY1);
                z2 = new Node(n, clickY2);
                linkMethod = LINKBYTWOCORNER;
                return true;
            }

            if(i!=n&&map[i][clickY1]==BLANK_STATE){

                if(oneCornerLink(i, clickY1, clickX2, clickY2)){
                    linkMethod = LINKBYTWOCORNER;
                    z1 = new Node(i, clickY1);
                    z2 = new Node(i, clickY2);
                    return true;
                }

            }else{
                break;
            }

        }


        for(int i=clickY1-1;i>=-1;i--){

            if(i==-1&&throughHorizontalLink(clickX2, clickY2, true)){
                linkMethod = LINKBYTWOCORNER;
                z1 = new Node(clickX1, -1);
                z2 = new Node(clickX2, -1);
                return true;
            }


            if(i!=-1&&map[clickX1][i]==BLANK_STATE){

                if(oneCornerLink(clickX1, i, clickX2, clickY2)){
                    linkMethod = LINKBYTWOCORNER;
                    z1 = new Node(clickX1, i);
                    z2 = new Node(clickX2, i);
                    return true;
                }

            }else{
                break;
            }

        }

        for(int i=clickY1+1;i<=n;i++){

            if(i==n&&throughHorizontalLink(clickX2, clickY2, false)){
                z1 = new Node(clickX1, n);
                z2 = new Node(clickX2, n);
                linkMethod = LINKBYTWOCORNER;
                return true;
            }

            if(i!=n&&map[clickX1][i]==BLANK_STATE){

                if(oneCornerLink(clickX1, i, clickX2, clickY2)){
                    linkMethod = LINKBYTWOCORNER;
                    z1 = new Node(clickX1, i);
                    z2 = new Node(clickX2, i);
                    return true;
                }

            }else{
                break;
            }

        }


        return false;
    }



    private boolean throughHorizontalLink(int clickX, int clickY,boolean flag){

        if(flag){

            for(int i=clickY-1;i>=0;i--){
                if(map[clickX][i]!=BLANK_STATE){
                    return false;
                }
            }

        }else{

            for(int i=clickY+1;i<n;i++){
                if(map[clickX][i]!=BLANK_STATE){
                    return false;
                }
            }

        }

        return true;
    }

    private boolean throughVerticalLink(int clickX,int clickY,boolean flag){

        if(flag){

            for(int i=clickX-1;i>=0;i--){
                if(map[i][clickY]!=BLANK_STATE){
                    return false;
                }
            }

        }else{

            for(int i=clickX+1;i<n;i++){
                if(map[i][clickY]!=BLANK_STATE){
                    return false;
                }
            }

        }


        return true;
    }

    private void drawSelectedBlock(int x, int y, Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        BasicStroke s = new BasicStroke(1);
        g2.setStroke(s);
        g2.setColor(Color.RED);
        g.drawRect(x+1, y+1, 48, 48);
    }

    @SuppressWarnings("static-access")
    private void drawLink(int x1, int y1, int x2, int y2) {

        Graphics g = this.getGraphics();
        Point p1 = new Point(y1*50+leftX+25,x1*50+leftY+25);
        Point p2 = new Point(y2*50+leftX+25,x2*50+leftY+25);
        if(linkMethod == LINKBYHORIZONTAL || linkMethod == LINKBYVERTICAL){
            g.drawLine(p1.x, p1.y,p2.x, p2.y);

        }else if(linkMethod ==LINKBYONECORNER){
            Point point_z1 = new Point(z1.y*50+leftX+25,z1.x*50+leftY+25);
            g.drawLine(p1.x, p1.y,point_z1.x, point_z1.y);
            g.drawLine(p2.x, p2.y,point_z1.x, point_z1.y);

        }else{
            Point point_z1 = new Point(z1.y*50+leftX+25,z1.x*50+leftY+25);
            Point point_z2 = new Point(z2.y*50+leftX+25,z2.x*50+leftY+25);

            if(p1.x!=point_z1.x&&p1.y!=point_z1.y){
                Point temp;
                temp = point_z1;
                point_z1 = point_z2;
                point_z2 = temp;
            }

            g.drawLine(p1.x, p1.y, point_z1.x, point_z1.y);
            g.drawLine(p2.x, p2.y, point_z2.x, point_z2.y);
            g.drawLine(point_z1.x,point_z1.y, point_z2.x, point_z2.y);

        }

        count+=2;
        GameClient.textField.setText(count+"");
        try {
            Thread.currentThread().sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        repaint();
        map[x1][y1] = BLANK_STATE;
        map[x2][y2] = BLANK_STATE;
        isWin();
    }


    public void clearSelectBlock(int i,int j,Graphics g){
        g.clearRect(j*50+leftX, i*50+leftY, 50, 50);
        g.drawImage(pics[map[i][j]],leftX+j*50,leftY+i*50,50,50,this);
    }

    public int[][]get_Map(){
        return map;
    }

    private boolean find2Block() {


        if(isClick){
            clearSelectBlock(clickX, clickY, this.getGraphics());
            isClick = false;
        }

        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){

                if(map[i][j]==BLANK_STATE){
                    continue;
                }

                for(int p=i;p<n;p++){
                    for(int q=0;q<n;q++){
                        if(map[p][q]!=map[i][j]||(p==i&&q==j)){
                            continue;
                        }

                        if(verticalLink(p,q,i,j)||horizontalLink(p,q,i,j)
                                ||oneCornerLink(p,q,i,j)||twoCornerLink(p,q,i,j)){
                            drawSelectedBlock(j*50+leftX, i*50+leftY, this.getGraphics());
                            drawSelectedBlock(q*50+leftX, p*50+leftY, this.getGraphics());
                            drawLink(p, q, i, j);
                            repaint();
                            return true;
                        }

                    }
                }
            }
        }

        isWin();

        return false;
    }


    private void isWin() {

        if(count==n*n){
            String msg = "再来一轮吧";
            int type = JOptionPane.YES_NO_OPTION;
            String title = "恭喜你完成了游戏！";
            int choice = JOptionPane.showConfirmDialog(null, msg,title,type);
            if(choice==1){
                System.exit(0);
            }else if(choice == 0){
                startNewGame();
            }
        }

    }


    public void startNewGame() {
        // TODO Auto-generated method stub
        count = 0;
        mapUtil = new Map(10,n);
        map = mapUtil.getMap();
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                map1[i][j]=map[i][j];
            }
        }
        count1=count;

        isClick = false;
        clickId = -1;
        clickX = -1;
        clickY = -1;
        linkMethod = -1;
        GameClient.textField.setText(count+"");
        repaint();
    }

    public void replay() {
        // 重置地图和计数器
        //resetMap();
        int count2=0;
        map=map1;
        count=count1;

        Graphics g = this.getGraphics();

        isClick = false;
        clickId = -1;
        clickX = -1;
        clickY = -1;
        linkMethod = -1;
        GameClient.textField.setText(count+"");

        paint(g);
        //revalidate();


        if(k==1){
            // 逐步执行操作记录
            for (String operation : operationRecord) {
                String[] parts = operation.split(" ");
                String action = parts[0];
                System.out.println("Operation: " + operation);
                System.out.println("Parts length: " + parts.length);
                System.out.println("Parts content: " + Arrays.toString(parts));

                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);

                if (action.equals("CLICK")) {
                    // 执行鼠标点击操作
                    if (count2 == 0) {
                        // 第一个点击，记录点击的图像编号和坐标
                        clickId = map[x][y];
                        clickX = x;
                        clickY = y;
                        isClick = true;
                        count2=1;
                    } else {
                        // 第二个点击，判断是否可以连接
                        if(clickId==map[x][y]){
                            if(x==clickX&&y==clickY)
                                continue;
                            if (verticalLink(clickX,clickY,x,y)||horizontalLink(clickX,clickY,x,y)||oneCornerLink(clickX,clickY,x,y)||twoCornerLink(clickX,clickY,x,y)) {

                                drawSelectedBlock(y*50+leftX,x*50+leftY,g);
                                drawSelectedBlock(clickY*50+leftX,clickX*50+leftY,g);
                                drawLink(clickX, clickY, x, y);

                                map[clickX][clickY] = BLANK_STATE;
                                map[x][y] = BLANK_STATE;
                                isClick = false;
                                count2 = 0;
                                paint(g);
                                try {
                                    Thread.currentThread().sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            else{
                                count=1;
                                isClick = true;
                                clickId = map[x][y];
                                clearSelectBlock(clickX,clickY,g);
                                clickX = x;
                                clickY = y;
                                drawSelectedBlock(y*50+leftX,x*50+leftY,g);
                            }
                        }
                        else{
                            count=1;
                            isClick = true;
                            clickId = map[x][y];
                            clearSelectBlock(clickX,clickY,g);
                            clickX = x;
                            clickY = y;
                            drawSelectedBlock(y*50+leftX,x*50+leftY,g);
                        }
                    }
                }
            }
        }
    }


    public static class Node{
        int x;
        int y;

        public Node(int x,int y){
            this.x = x;
            this.y = y;
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub

        if(e.getKeyCode() == KeyEvent.VK_A){
            map = mapUtil.getResetMap();
            repaint();
        }

        if(e.getKeyCode() == KeyEvent.VK_D){
            find2Block();
            isWin();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

        Graphics g = this.getGraphics();


        int x = e.getX()-leftX;
        int y = e.getY()-leftY;

        if(x<0||y<0||x>500||y>500)
            return ;

        int i = y/50;
        int j = x/50;

        String operation = "CLICK " + i + " " + j;
        operationRecord.add(operation);


        if(isClick){

            if(map[i][j]!=BLANK_STATE){
                if(map[i][j]==clickId){
                    if(i==clickX&&j==clickY)
                        return ;

                    if(verticalLink(clickX,clickY,i,j)||horizontalLink(clickX,clickY,i,j)||oneCornerLink(clickX,clickY,i,j)||twoCornerLink(clickX,clickY,i,j)){
                        drawSelectedBlock(j*50+leftX,i*50+leftY,g);
                        drawLink(clickX,clickY,i,j);
                        isClick = false;

                    }else{
                        clickId = map[i][j];
                        clearSelectBlock(clickX,clickY,g);
                        clickX = i;
                        clickY = j;
                        drawSelectedBlock(j*50+leftX,i*50+leftY,g);
                    }

                }else{
                    clickId = map[i][j];
                    clearSelectBlock(clickX,clickY,g);
                    clickX = i;
                    clickY = j;
                    drawSelectedBlock(j*50+leftX,i*50+leftY,g);
                }

            }

        }else{
            if(map[i][j]!=BLANK_STATE){
                clickId = map[i][j];
                isClick = true;
                clickX = i;
                clickY = j;
                drawSelectedBlock(j*50+leftX,i*50+leftY,g);
            }
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

    }


    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }


    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

}

