package org.example.tankWar;

import javax.swing.ImageIcon;
import java.awt.Image;

public class Tool {

    public static Image getImage(String imageName){
        return new ImageIcon("assets/images/" + imageName).getImage();
    }
}
