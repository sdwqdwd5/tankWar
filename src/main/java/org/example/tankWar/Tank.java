package org.example.tankWar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Tank {
    private int x;
    private int y;
    private boolean enemy;
    private Direction direction;
    private boolean stopped;

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
        if (this.stopped){
            return;
        }
        switch (direction) {
            case UP:
                y -= 5;
                break;
            case DOWN:
                y += 5;
                break;
            case LEFT:
                x -= 5;
                break;
            case RIGHT:
                x += 5;
                break;
            case UPLEFT:
                x -= 5;
                y -= 5;
                break;
            case UPRIGHT:
                x += 5;
                y -= 5;
                break;
            case DOWNLEFT:
                x -= 5;
                y += 5;
                break;
            case DOWNRIGHT:
                x += 5;
                y += 5;
                break;
        }
    }

    Image getImage() {
        String prefix = enemy ? "e" : "";
        switch (direction) {
            case UP:
                return  Tool.getImage(prefix + "tankU.gif");
            case DOWN:
                return  Tool.getImage(prefix + "tankD.gif");
            case LEFT:
                return  Tool.getImage(prefix + "tankL.gif");
            case RIGHT:
                return  Tool.getImage(prefix + "tankR.gif");
            case UPLEFT:
                return  Tool.getImage(prefix + "tankLU.gif");
            case UPRIGHT:
                return  Tool.getImage(prefix + "tankRU.gif");
            case DOWNLEFT:
                return  Tool.getImage(prefix + "tankLD.gif");
            case DOWNRIGHT:
                return  Tool.getImage(prefix + "tankRD.gif");
        }
        return null;
    }
    void draw(Graphics g){
        this.determineDirection();
        this.move();
        g.drawImage(this.getImage(), this.x, this.y,null);
    }
    private boolean up, down, left, right;

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
        }

    }

    private void determineDirection() {
        if (!up && !left && !down && !right){
            this.stopped = true;
        }else {
            if (up && left && !down && !right) this.direction = Direction.UPLEFT;
            else if (up && !left && !down && right) this.direction = Direction.UPRIGHT;
            else if (!up && left && down && !right) this.direction = Direction.DOWNLEFT;
            else if (!up && !left && down && right) this.direction = Direction.DOWNRIGHT;
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
