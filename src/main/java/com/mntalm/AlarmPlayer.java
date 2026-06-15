package com.mntalm;

import javax.sound.sampled.*;
import java.io.File;

/**
 * Alarm Sound Player
 */
public class AlarmPlayer {
    
    /**
     * Play alarm sound
     */
    public static void play(String soundPath) {
        try {
            File soundFile = new File(soundPath);
            if (!soundFile.exists()) {
                throw new RuntimeException("Sound file not found: " + soundPath);
            }
            
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            
            // Keep playing until done
            while (clip.isRunning()) {
                Thread.sleep(100);
            }
            
            clip.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to play alarm sound", e);
        }
    }
    
    /**
     * Play alarm asynchronously (non-blocking)
     */
    public static void playAsync(String soundPath) {
        new Thread(() -> play(soundPath)).start();
    }
}
