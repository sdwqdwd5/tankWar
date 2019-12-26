package org.example.tankWar;

import java.awt.*;

public class Missile {
    public static final int SPEED = 10;
    private int x;
    private int y;
    private final boolean enemy;
    private final Direction direction;

    public Missile(int x, int y, boolean enemy, Direction direction) {
        this.x = x;
        this.y = y;
        this.enemy = enemy;
        this.direction = direction;
    }
    Image getImage() {
        String prefix = enemy ? "e" : "";
        switch (direction) {
            case UP:
                return  Tool.getImage("missileU.gif");
            case DOWN:
                return  Tool.getImage("missileD.gif");
            case LEFT:
                return  Tool.getImage("missileL.gif");
            case RIGHT:
                return  Tool.getImage("missileR.gif");
            case UPLEFT:
                return  Tool.getImage("missileLU.gif");
            case UPRIGHT:
                return  Tool.getImage("missileRU.gif");
            case DOWNLEFT:
                return  Tool.getImage("missileLD.gif");
            case DOWNRIGHT:
                return  Tool.getImage("missileRD.gif");
        }
        return null;
    }

    void move(){
        switch (direction) {
            case UP:
                y -= SPEED;
                break;
            case DOWN:
                y += SPEED;
                break;
            case LEFT:
                x -= SPEED;
                break;
            case RIGHT:
                x += SPEED;
                break;
            case UPLEFT:
                x -= SPEED;
                y -= SPEED;
                break;
            case UPRIGHT:
                x += SPEED;
                y -= SPEED;
                break;
            case DOWNLEFT:
                x -= SPEED;
                y += SPEED;
                break;
            case DOWNRIGHT:
                x += SPEED;
                y += SPEED;
                break;
        }
    }

    void draw(Graphics g){
        move();
        if (x < 0 || x > 800 || y < 0 || y > 600) return;
        g.drawImage(getImage(), x, y, null);
    }
}
