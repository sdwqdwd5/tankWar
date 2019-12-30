package org.example.tankWar;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Random;

public class Tank {
    private static final int MOVE_SPEED = 5;
    private int x;
    private int y;
    private boolean enemy;
    private Direction direction;
    private boolean stopped;
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
        this.determineDirection();
        this.move();

        if (x < 0) x = 0;
        else if (x > 800 - getImage().getWidth(null)) x = 800 - getImage().getWidth(null);
        if (y < 0) y = 0;
        else if (y > 600 - getImage().getHeight(null)) y = 600 - getImage().getHeight(null);

        Rectangle rec = this.getRectangle();
        for(Wall wall : GameClient.getInstance().getWalls()){
            if (rec.intersects(wall.getRectangle())){
                x = preX;
                y = preY;
                break;
            }
        }
        for (Tank enemyTank : GameClient.getInstance().getEnemyTanks()){
            if (rec.intersects(enemyTank.getRectangle())){
                x = preX;
                y = preY;
                break;
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
                up = true;
                break;
            case KeyEvent.VK_DOWN:
                down = true;
                break;
            case KeyEvent.VK_LEFT:
                left = true;
                break;
            case KeyEvent.VK_RIGHT:
                right = true;
                break;
            case KeyEvent.VK_CONTROL:
                fire();
                break;
            case KeyEvent.VK_A:
                superFire();
                break;
        }
    }

    private void fire() {

        Missile missile = new Missile(x + getImage().getWidth(null) / 2 - 6,
                                      y + getImage().getHeight(null)/ 2 - 6,enemy,direction);
        GameClient.getInstance().getMissiles().add(missile);
        playAudio("shoot.wav");
    }

    private void superFire() {
        for (Direction direction : Direction.values()) {
            Missile missile = new Missile(x + getImage().getWidth(null) / 2 - 6,
                    y + getImage().getHeight(null) / 2 - 6, enemy, direction);
            GameClient.getInstance().getMissiles().add(missile);
        }
        playAudio(new Random().nextBoolean() ? "supershoot.aiff" : "supershoot.wav");
    }

    private void playAudio(String fileName) {
        Media sound = new Media(new File("assets/audios/" + fileName).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    private void determineDirection() {
        if (!up && !left && !down && !right){
            this.stopped = true;
        }else {
                 if (up && left && !down && !right)  this.direction = Direction.LEFT_UP;
            else if (up && !left && !down && right)  this.direction = Direction.RIGHT_DOWN;
            else if (!up && left && down && !right)  this.direction = Direction.LEFT_DOWN;
            else if (!up && !left && down && right)  this.direction = Direction.RIGHT_DOWN;
            else if (up && !left && !down && !right) this.direction = Direction.UP;
            else if (!up && !left && down && !right) this.direction = Direction.DOWN;
            else if (!up && !left && !down && right) this.direction = Direction.RIGHT;
            else if (!up && left && !down && !right) this.direction = Direction.LEFT;
            this.stopped = false;
        }
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                up = false;
                break;
            case KeyEvent.VK_DOWN:
                down = false;
                break;
            case KeyEvent.VK_LEFT:
                left = false;
                break;
            case KeyEvent.VK_RIGHT:
                right = false;
                break;
        }
    }
}
