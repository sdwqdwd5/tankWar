package org.example.tankWar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class GameClient extends JComponent {
    private Tank playerTank;
    private List<Tank> enemyTanks;
    private List<Wall> walls;
    private List<Missile> missiles;
    private List<Explosion> explosions;
    private AtomicInteger enemyKilled = new AtomicInteger(0);
    public List<Explosion> getExplosions() {
        return explosions;
    }

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
        this.missiles   = new CopyOnWriteArrayList<>();
        this.explosions = new CopyOnWriteArrayList<>();
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
        this.enemyTanks = new CopyOnWriteArrayList<>();
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
        if(! playerTank.isLive()){
            g.setColor(Color.RED);
            g.setFont(new Font(null, Font.BOLD,100));
            g.drawString("GAME　OVER", 50,150);
            g.setColor(Color.WHITE);
            g.setFont(new Font(null, Font.PLAIN,50));
            g.drawString("Press F2 TO RESTART", 125,500);
        }else {
            g.setColor(Color.WHITE);
            g.setFont(new Font(null, Font.PLAIN,10));
            g.drawString("Missiles: " + missiles.size(), 10, 10);
            g.drawString("Explosions: " + explosions.size(),10,25);
            g.drawString("Player Tank HP: " + playerTank.getHp(),10,40);
            g.drawString("Enemy left: " + enemyTanks.size(),10,55);
            g.drawString("Enemy Killed: " + enemyKilled.get(),10,70);
            playerTank.draw(g);

            int count = enemyTanks.size();
            enemyTanks.removeIf(t -> !t.isLive());
            enemyKilled.addAndGet((count - enemyTanks.size()));
            if (enemyTanks.isEmpty()) {
                this.initialEnemyTank();
            }
            //enemyTanks.removeIf(et -> !et.isLive());
            for (Tank tank : enemyTanks) {
                tank.draw(g);
            }
            for (Wall wall : walls) {
                wall.draw(g);
            }
            missiles.removeIf(m -> !m.isLive());
            for (Missile missile : missiles) {
                missile.draw(g);
            }
            explosions.removeIf(e -> !e.isLive());
            for (Explosion explosion : explosions) {
                explosion.draw(g);
            }
        }
    }

    public static void main(String[] args){
        //com.sun.javafx.application.PlatformImpl.startup(() -> {});
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
            try {
                client.repaint();
                if (client.playerTank.isLive()) {
                    for (Tank tank : client.enemyTanks) {
                        tank.actRandomly();
                    }
                }
                Thread.sleep(50);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public void restart() {
        if (!playerTank.isLive()){
            this.playerTank = new Tank(380,80,Direction.DOWN);

        }
        this.initialEnemyTank();
    }
}
