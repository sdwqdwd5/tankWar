package org.example.tankWar;

import java.awt.*;

public enum Direction {
    UP("U"),
    DOWN("D"),
    LEFT("L"),
    RIGHT("R"),
    LEFT_UP("LU"),
    RIGHT_UP("RU"),
    LEFT_DOWN("LD"),
    RIGHT_DOWN("RD");

    private final String abbrev;

    Direction(String abbrev){
        this.abbrev = abbrev;
    }

    Image getImage(String prefix){
        return Tool.getImage(prefix + abbrev + ".gif");

    }
}
