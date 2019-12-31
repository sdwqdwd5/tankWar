package org.example.tankWar;

import java.awt.*;

public class Explosion {

    private int x, y;
    private int step = 0;
    private boolean live = true;

    public boolean isLive() {
        return live;
    }

    public Explosion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    void draw (Graphics g){
        if(step > 10) {
            this.live = false;
            return;
        }
        g.drawImage(Tool.getImage(step++ + ".gif"), x-10, y-12, null);
    }
}
