package Main;
import GameState.GameStateManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import TileMap.TileMap;

//@SuppressWarnings("searial")
public class GamePanel extends JPanel implements Runnable,KeyListener{

    //Dimensions
    public static final int WIDTH = 320;
    public static final int HEIGHT = 240;
    public static final int SCALE = 2;

    //Game thread
    private Thread thread;
    private boolean running;
    private int FPS = 60;
    private long targetTime = 1000/FPS;

    //Image
    private BufferedImage image;
    private Graphics2D g;

    //Game State Manager
    private GameStateManager gsm;


    public GamePanel()
    {
        super();

        setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
        setFocusable(true);
        requestFocus();
        requestFocusInWindow();
//        setFocusable(true);
        setRequestFocusEnabled(true);

        JPanel p = new JPanel();


        add(p);


        grabFocus();
    }

    public void addNotify()
    {
        super.addNotify();
        if(thread==null)
        {
            thread = new Thread(this);
            addKeyListener(this);
            thread.start();
        }
    }

    private void init()
    {
        image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);

        g=(Graphics2D) image.getGraphics();

        running = true;

        gsm = new GameStateManager();

    }

    public void run()
    {
        init();

        long start;
        long elapsed;
        long wait;

        //Game loop

        while (running)
        {
//            System.out.println("WHILE LOOP "+ TileMap.getacode());
//            if(TileMap.getacode()>7){
//                System.out.println("WHILE LOOP BREAK"+ TileMap.getacode());
//
//    break;
//
//            }
            start = System.nanoTime();

            update();
            draw();
            drawToScreen();

            elapsed = System.nanoTime() - start;

            wait = targetTime - elapsed/1000000;
            if(wait < 0) wait = 5;
            try
            {
                Thread.sleep(wait);
            }
            catch (Exception e)
            {
//                e.printStackTrace();
            }
        }
    }

    private void update()
    {
        gsm.update();
    }

    private void draw()
    {
        gsm.draw(g);
    }

    private void drawToScreen()
    {
        try{
            Graphics g2 = getGraphics();
            //g2.drawImage(image,0,0,null);
            g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
            g2.dispose();
        }catch (Exception e){

        }


    }

    public void keyTyped(KeyEvent key)
    {

    }

    public void keyPressed(KeyEvent key)
    {
        gsm.keyPressed(key.getKeyCode());
    }

    public void keyReleased(KeyEvent key)
    {
        gsm.keyReleased(key.getKeyCode());
    }

}









