package org.example.tankWar;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.awt.*;
import java.awt.event.KeyEvent;

import java.util.Random;
import org.example.tankWar.Save.Position;

public class Tank {

    Position getPosition(){
        return new Position(x,y,direction);
    }
    private static int MOVE_SPEED = 5;
    private int x;
    private int y;
    private boolean enemy;
    private Direction direction;
    private boolean stopped;
    private int code;
    private boolean up, down, left, right;
    private boolean live = true;
    private int hp = 100;
    public boolean isEnemy() {
        return enemy;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    public Tank(int x, int y, Direction direction) {
        this(x, y, false, direction);
    }

    public Tank(Position position, boolean enemy){
        this(position.getX(), position.getY(), enemy, position.getDirection());
    }
    public Tank(int x, int y, boolean enemy, Direction direction) {
        this.x = x;
        this.y = y;
        this.enemy = enemy;
        this.direction = direction;
    }

    void move() {
        if (this.stopped) return;
        x += direction.xFactor * MOVE_SPEED;
        y += direction.yFactor * MOVE_SPEED;
    }

    Image getImage() {
        String prefix = enemy ? "e" : "";
        return direction.getImage(prefix + "tank");
    }
    void draw(Graphics g){
        int preX = x;
        int preY = y;
        this.move();

        if (x < 0) x = 0;
        else if (x > GameClient.WIDTH - getImage().getWidth(null)) x = GameClient.WIDTH- getImage().getWidth(null);
        if (y < 0) y = 0;
        else if (y > GameClient.HEIGHT - getImage().getHeight(null)) y = GameClient.HEIGHT - getImage().getHeight(null);

        Rectangle rec = this.getRectangle();
        for(Wall wall : GameClient.getInstance().getWalls()){
            if (rec.intersects(wall.getRectangle())){
                x = preX;
                y = preY;
                break;
            }
        }
        for (Tank enemyTank : GameClient.getInstance().getEnemyTanks()){
            if (enemyTank != this && rec.intersects(enemyTank.getRectangle())){
                x = preX;
                y = preY;
                break;
            }
        }
        if (this.enemy && rec.intersects(GameClient.getInstance().getPlayerTank().getRectangle())){
            x = preX;
            y = preY;
        }
        if (! enemy){
            g.setColor(Color.WHITE);
            g.fillRect(x, y - 10, this.getImage().getWidth(null), 10);
            g.setColor(Color.RED);
            int width = hp * this.getImage().getWidth(null)/100;
            g.fillRect(x,y-10,width,10);
            if (GameClient.getInstance().getBlood().isLive() &&
            rec.intersects(GameClient.getInstance().getBlood().getRectangle())){
                this.setHp(100);
                GameClient.getInstance().getBlood().setLive(false);
            }
        }
        g.drawImage(this.getImage(), this.x, this.y,null);
    }
    public Rectangle getRectangle(){
        return new Rectangle(x, y, getImage().getWidth(null), getImage().getHeight(null));
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                code |= Direction.UP.code;
                break;
            case KeyEvent.VK_DOWN:
                code |= Direction.DOWN.code;
                break;
            case KeyEvent.VK_LEFT:
                code |= Direction.LEFT.code;
                break;
            case KeyEvent.VK_RIGHT:
                code |= Direction.RIGHT.code;
                break;
            case KeyEvent.VK_CONTROL:
                fire();
                break;
            case KeyEvent.VK_A:
                superFire();
                break;
            case KeyEvent.VK_F2:
                GameClient.getInstance().restart();
                break;
            case KeyEvent.VK_Z:
                MOVE_SPEED = 20;
                break;

        }
        this.determineDirection();
    }

    private void fire() {

        Missile missile = new Missile(x + getImage().getWidth(null) / 2 - 6,
                                      y + getImage().getHeight(null)/ 2 - 6,enemy,direction);
        GameClient.getInstance().getMissiles().add(missile);
        if(isLive()) Tool.playAudio(isEnemy()?"shoot.wav": "supershoot.wav");
    }

    private void superFire() {
        for (Direction direction : Direction.values()) {
            Missile missile = new Missile(x + getImage().getWidth(null) / 2 - 6,
                    y + getImage().getHeight(null) / 2 - 6, enemy, direction);
            GameClient.getInstance().getMissiles().add(missile);
        }
        if(isLive()) Tool.playAudio("supershoot.aiff" );
    }

    private void determineDirection() {
        Direction newDirection = Direction.get(code);
        if(newDirection == null){
            this.stopped = true;
        }else {
            this.direction = newDirection;
            this.stopped = false;
        }
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                code ^= Direction.UP.code;
                break;
            case KeyEvent.VK_DOWN:
                code ^= Direction.DOWN.code;
                break;
            case KeyEvent.VK_LEFT:
                code ^= Direction.LEFT.code;
                break;
            case KeyEvent.VK_RIGHT:
                code ^= Direction.RIGHT.code;
                break;
            case KeyEvent.VK_Z:
                MOVE_SPEED = 5;
                break;
        }
        this.determineDirection();
    }
    private final Random random = new Random();
    private int step = new Random().nextInt(5);
    public void actRandomly() {
        Direction [] dirs = Direction.values();
        if(step == 0){
            step = random.nextInt(5) + 3 ;
            this.direction = dirs[random.nextInt(dirs.length)];
            if (random.nextBoolean()){
                this.fire();
            }
        }
        step --;

    }
}
