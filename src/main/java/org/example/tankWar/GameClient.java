package org.example.tankWar;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.example.tankWar.Save.Position;

public class GameClient extends JComponent {
    static final int WIDTH = 800, HEIGHT = 600;
    private static final String GAME_SAV = "game.sav";
    private Tank playerTank;
    private List<Tank> enemyTanks;
    private List<Wall> walls;
    private List<Missile> missiles;
    private List<Explosion> explosions;
    private Blood blood;

    public Blood getBlood() {
        return blood;
    }

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
        this.playerTank = new Tank(380,80,false,Direction.DOWN);
        this.missiles   = new CopyOnWriteArrayList<>();
        this.explosions = new CopyOnWriteArrayList<>();
        this.blood = new Blood(380, 150);
        this.walls = Arrays.asList(
                new Wall(175, 120, true, 15),
                new Wall(175, 520, true, 15),
                new Wall(80,  80, false,15),
                new Wall(700,  80, false,15)
        );
        initialEnemyTank();
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
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
        g.fillRect(0, 0, WIDTH, HEIGHT);

        if(! playerTank.isLive()){
            g.setColor(Color.RED);
            g.setFont(new Font(null, Font.BOLD,100));
            g.drawString("GAME　OVER", 50,150);
            g.setColor(Color.WHITE);
            g.setFont(new Font(null, Font.PLAIN,50));
            g.drawString("Press F2 TO RESTART", 125,500);
            g.drawString("Enemy Killed: " + enemyKilled.get(),125,300);
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

            if(blood.isLive()) {
                blood.draw(g);
            }
            if (enemyKilled.get() % 20 == 0 && enemyKilled.get() != 0){
                blood.setLive(true);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        com.sun.javafx.application.PlatformImpl.startup(() -> {});

        JFrame frame = new JFrame();
        frame.setTitle("Tank War");
        frame.setIconImage(Tool.getImage("Icon.png"));
        final GameClient client = GameClient.getInstance();
        frame.add(client);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try{
                    client.save();
                    System.exit(0);
                }catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Failed to save current game",
                            "Oops! Error", JOptionPane.ERROR_MESSAGE);
                    System.exit(4);
                    ex.printStackTrace();
                }

            }
        });
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
        try{
            client.load();
        }catch (IOException e){
            JOptionPane.showMessageDialog(null, "Failed to load previos game!",
                    "Oop! Error Occurred", JOptionPane.ERROR_MESSAGE);
        }

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

    private void load() throws IOException {
        File file = new File(GAME_SAV);
        if (file.exists() && file.isFile()){
            String json = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            Save save = JSON.parseObject(json, Save.class);
            if (save.isGameContinued()){
                this.playerTank = new Tank(save.getPlayerPosition(), false);
                this.enemyTanks.clear();
                List<Position> enemyPosition = save.getEnemyPosition();
                if(enemyPosition != null && !enemyPosition.isEmpty()){
                    for (Position position: enemyPosition){
                        this.enemyTanks.add(new Tank(position, true));
                    }
                }
            }
        }
    }

    void save(String destination) throws IOException {
        Save save = new Save(playerTank.isLive(), playerTank.getPosition(),
                enemyTanks.stream().filter(Tank::isLive)
                        .map(Tank::getPosition).collect(Collectors.toList()));
        FileUtils.write(new File(destination), JSON.toJSONString(save, true), StandardCharsets.UTF_8);
    }
    void save() throws IOException {
        this.save(GAME_SAV);
    }
    void restart() {
        this.playerTank = new Tank(380,80,false,Direction.DOWN);
        this.initialEnemyTank();
        this.enemyKilled.set(0);
        this.missiles.clear();
        this.explosions.clear();
    }
}
