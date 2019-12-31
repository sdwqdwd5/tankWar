package org.example.tankWar;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.io.File;

public class Tool {

    public static Image getImage(String imageName){
        return new ImageIcon("assets/images/" + imageName).getImage();
    }

    public static void playAudio(String fileName) {
        Media sound = new Media(new File("assets/audios/" + fileName).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }
}
