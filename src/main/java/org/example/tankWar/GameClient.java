package org.example.tankWar;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameClient extends JComponent {
    private Tank playerTank;
    private List<Tank> enemyTanks;
    private List<Wall> walls;
    private List<Missile> missiles;
    public static final GameClient INSTANCE = new GameClient();

    public static GameClient getInstance(){
        return INSTANCE;
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public List<Tank> getEnemyTanks() {
        return enemyTanks;
    }

    public List<Missile> getMissiles() {
        return missiles;
    }

    public Tank getPlayerTank() {
        return playerTank;
    }
    //set data field
    public GameClient(){
        this.playerTank = new Tank(380,80,Direction.DOWN);

        this.missiles   = new ArrayList<>();
        this.walls = Arrays.asList(
                new Wall(175, 120, true, 15),
                new Wall(175, 520, true, 15),
                new Wall(80,  80, false,15),
                new Wall(700,  80, false,15)
        );
        initialEnemyTank();
        this.setPreferredSize(new Dimension(800,600));
    }

    private void initialEnemyTank() {
        this.enemyTanks = new ArrayList<>(12);
        for (int i = 0; i < 3; i++){
           for (int j = 0; j < 4; j++){
               this.enemyTanks.add(new Tank(175 + j * 137,360 + 50 * i, true, Direction.UP));
           }
        }
    }

    @Override
    // paint data field
    protected void paintComponent(Graphics g) {
        //set background to black
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 800, 600);
       playerTank.draw(g);
       enemyTanks.removeIf(t -> !t.isLive());
       if(enemyTanks.isEmpty()){
           this.initialEnemyTank();
       }
       for (Tank tank:enemyTanks){
           tank.draw(g);
       }
       for (Wall wall: walls){
           wall.draw(g);
       }
       missiles.removeIf(m -> !m.isLive());
       for (Missile missile: missiles){
           missile.draw(g);
       }
    }

    public static void main(String[] args){
        com.sun.javafx.application.PlatformImpl.startup(() -> {});
        JFrame frame = new JFrame();
        frame.setTitle("Tank War");
        frame.setIconImage(Tool.getImage("Icon.png"));
        final GameClient client = GameClient.getInstance();

        frame.add(client);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                client.playerTank.keyPressed(e);
            }
            @Override
            public void keyReleased(KeyEvent e) {
               client.playerTank.keyReleased(e);
            }
        });
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        while(true){
            client.repaint();
            try{
                Thread.sleep(15);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

    }
}
