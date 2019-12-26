package org.example.tankWar;

import javax.swing.*;
import java.awt.*;

public class Wall {
    private int x;
    private int y;
    private boolean horizontal;
    private int bricks;
    private final Image brickImage;
    public Wall(int x, int y, boolean horizontal, int bricks) {
        this.brickImage = Tool.getImage("brick.png");
        this.x = x;
        this.y = y;
        this.horizontal = horizontal;
        this.bricks = bricks;
    }
    public Rectangle getRectangle(){
        return horizontal ? new Rectangle(x, y, bricks * brickImage.getWidth(null), brickImage.getHeight(null)):
                            new Rectangle(x, y, brickImage.getWidth(null),bricks * brickImage.getHeight(null));
    }
    public void draw(Graphics g){
        if (horizontal){
            for (int i = 0; i < bricks; i++){
                g.drawImage(brickImage, x + i * brickImage.getWidth(null), y, null);
            }
        }else{
            for (int i = 0; i < bricks; i++){
                g.drawImage(brickImage, x, y + i * brickImage.getWidth(null), null);
            }
        }
    }
}
