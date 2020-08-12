package org.example.tankWar;

import java.awt.*;

public class Blood {
    private int x, y;

    public void setLive(boolean live) {
        this.live = live;
    }

    public boolean isLive() {
        return live;
    }

    private boolean live = false;

    public Blood(int x, int y){
        this.x = x;
        this.y = y;
    }

    void draw(Graphics g){
        if (!live){
            return;
        }
        g.drawImage(Tool.getImage("blood.png"),x,y,null);
    }

    Rectangle getRectangle(){
        return new Rectangle(x, y, Tool.getImage("blood.png").getWidth(null),
                Tool.getImage("blood.png").getHeight(null));
    }
}
