import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePlay extends JPanel implements KeyListener , ActionListener {

    private boolean play = false;
    private int scores = 0;
    private int totalBricks = 21;

    private Timer timer;
    private int delay = 8;
    private int playerX = 310;
    private int ballPosiX = 120;
    private int ballPosiY = 350;

    private int ballXdir = -1;
    private  int ballYdir = -2;

    private MapGenerator mapGenerator;

    public GamePlay(){
        mapGenerator = new MapGenerator(3,7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay , this);
        timer.start();
    }

    public void paint(Graphics graphics) {
        //background
        graphics.setColor(Color.gray);
        graphics.fillRect(1,1,692,592);

        //draw map
        mapGenerator.draw(((Graphics2D) graphics));

        //borders
        graphics.setColor(Color.red);
        graphics.fillRect(0,0,3,592);
        graphics.fillRect(0,0,692,3);
        graphics.fillRect(681,0,3,592);

        //score
        graphics.setColor(Color.black);
        graphics.setFont(new Font("serif",Font.BOLD, 25));
        graphics.drawString(""+scores,590,30);

        //the paddle
        graphics.setColor(Color.yellow);
        graphics.fillRect(playerX,549,100,8);


        //the ball
        graphics.setColor(Color.green);
        graphics.fillOval(ballPosiX,ballPosiY,20,20);

        if (totalBricks <= 0){
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            graphics.setColor(Color.red);
            graphics.setFont(new Font("serif",Font.BOLD, 30));
            graphics.drawString("You won! Scores: "+scores,190,300);

            graphics.setFont(new Font("serif",Font.BOLD, 20));
            graphics.drawString("Press Enter to restart! "+scores,230,350);
        }

        if (ballPosiY > 570){
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            graphics.setColor(Color.red);
            graphics.setFont(new Font("serif",Font.BOLD, 30));
            graphics.drawString("Game Over! Scores: "+scores,190,300);

            graphics.setFont(new Font("serif",Font.BOLD, 20));
            graphics.drawString("Press Enter to restart! "+scores,230,350);
        }

        graphics.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        ballMovements();

        repaint();

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT){
            if (playerX >= 600) {
                playerX = 600;
            }
            else {
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT){
            if (playerX < 10) {
                playerX = 10;
            }
            else {
                moveLeft();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER){
            if (!play){
                play = true;
                ballPosiX = 120;
                ballPosiY = 350;
                ballXdir = -1;
                ballYdir = -2;
                playerX = 310;
                scores = 0;
                totalBricks = 21;
                mapGenerator = new MapGenerator(3,7);
                repaint();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    public void moveRight(){
        play = true;
        playerX+=20;
    }
    public void moveLeft(){
        play = true;
        playerX-=20;
    }

    public void ballMovements(){
        if (play){
           //check if ball hits
            ballHitPaddle();

            //hit the map
           A: for (int i =0 ; i<mapGenerator.map.length ; i++){
                for (int j = 0 ; j < mapGenerator.map[0].length ; j++){
                    if (mapGenerator.map[i][j] > 0){
                        int brickX = j * mapGenerator.brickWidth + 80;
                        int brickY = i * mapGenerator.brickHeight + 50;
                        int brickWidth = mapGenerator.brickWidth;
                        int brickHeight = mapGenerator.brickHeight;

                        Rectangle rectangle = new Rectangle(brickX,brickY,brickWidth,brickHeight);
                        Rectangle ballRectangle = new Rectangle(ballPosiX,ballPosiY,20,20);
                        Rectangle brickRectangle = rectangle;

                        if(ballRectangle.intersects(brickRectangle)){
                            mapGenerator.setBrickValue(0,i,j);
                            totalBricks--;
                            scores +=5;

                            if (ballPosiX + 19 <= brickRectangle.x ||
                                    ballPosiX + 1 >= brickRectangle.x + brickRectangle.width){
                                ballXdir = -ballXdir;
                            }
                            else{
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }
                    }
                }
            }

            ballPosiX += ballXdir;
            ballPosiY += ballYdir;
            //right check
            if (ballPosiX < 0){
                ballXdir = -ballXdir;
            }
            ////top check
            if (ballPosiY < 0){
                ballYdir = -ballYdir;
            }
            //left check
            if (ballPosiX > 670){
                ballXdir = -ballXdir;
            }
        }

    }
    public void ballHitPaddle(){
        if (new  Rectangle(ballPosiX,ballPosiY,20,20)
                .intersects(new Rectangle(playerX,550,100,8))){
            ballYdir = -ballYdir;
            System.out.println("hit!");
        }
    }
}
