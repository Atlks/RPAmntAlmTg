package com.mntalm;

import org.sikuli.script.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.Image;
import java.io.File;

import static java.awt.SystemTray.*;
import static org.sikuli.script.Screen.*;

/**
 * Telegram Web Monitor using SikuliX
 * Monitors for mentions (@) and direct messages
 */
public class TelegramMonitor {
    private static final Logger log = LoggerFactory.getLogger(TelegramMonitor.class);
    public static final String SCOT = "scot";

  //  private Screen screen;
    private final Config config;
    private boolean isRunning = false;
    private long lastCheckTime = 0;
    private String lastMessageTime = "";
    
    // Pattern files (relative to resources directory)
    private static final String PATTERN_MENTION = "c:/prj/img/mention_icon.png.jpg";
    private static final String PATTERN_UNREAD = "patterns/unread_badge.png";
    private static final String PATTERN_DM_ICON = "patterns/dm_icon.png";
    
    public TelegramMonitor(Config config) {
        this.config = config;
        //is.screen = new Screen();
   //     Settings.MinSimilarity = config.getPatternSimilarity();
        
        // Initialize SikuliX
        try {
            log.info("Initializing SikuliX...");
           // Screen.init();
            log.info("SikuliX initialized successfully");
        } catch (Exception e) {
            log.error("Failed to initialize SikuliX", e);
            throw new RuntimeException("SikuliX initialization failed", e);
        }
    }
    
    /**
     * Start monitoring
     */
    public void startMonitoring() {
        isRunning = true;
        log.info("Starting Telegram monitoring...");
        
        while (isRunning) {
            try {
                System.out.println(System.currentTimeMillis());
                checkForNotifications();
                Thread.sleep(config.getCheckIntervalMs());
            } catch (InterruptedException e) {
                log.warn("Monitoring interrupted", e);
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("Error during monitoring", e);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        
        log.info("Telegram monitoring stopped");
    }
    
    /**
     * Stop monitoring
     */
    public void stopMonitoring() {
        isRunning = false;
    }
    
    /**
     * Check for notifications
     */
    private void checkForNotifications() {
        log.debug("Checking for notifications...");
        
        boolean hasMention = false;
        boolean hasDM = false;
        boolean hasUnread = false;
        
        try {
            // Check for mention icon
            if (exists(PATTERN_MENTION)) {
                hasMention = true;
                log.info("🔔 Mention detected!");
            }
            
//            // Check for unread badge
//            if (exists(PATTERN_UNREAD)) {
//                hasUnread = true;
//                log.debug("Unread message badge detected");
//            }
//
//            // Check for DM icon
//            if (exists(PATTERN_DM_ICON)) {
//                hasDM = true;
//                log.info("💬 Direct message detected!");
//            }
            
            // Also check screen text for @ symbol in recent messages
            if (config.isOcrEnabled()) {
                String screenText = getScreenText();
                if (screenText != null && screenText.contains("@")) {
                    log.info("📝 '@' symbol detected in screen text");
                    hasMention = true;
                }
            }
            
            // Trigger alarm if needed
            if (hasMention ) {
                String notificationType =  "mention"  ;
                triggerAlarm(notificationType);
            }
            
            lastCheckTime = System.currentTimeMillis();
            
        } catch (Exception e) {
            log.error("Error checking notifications", e);
        }
    }
    
    /**
     * Check if pattern exists on screen
     */
    private boolean exists(String patternPath) {
        try {
            Pattern pattern = new Pattern(patternPath);
              Screen screen = new Screen();
            captureScreenshot("c:/img",screen);
            int searchTimeoutSec = config.searchTimeoutSec;
            Match exists = screen.exists(pattern, searchTimeoutSec);
            return exists != null;
        } catch (Exception e) {
            log.debug("Pattern not found: {}", patternPath, e);
            return false;
        }
    }
    
    /**
     * Get text from screen using OCR
     */
    private String getScreenText() {
        try {
              Screen screen= new Screen();
            return screen.findText(SCOT).text();
        } catch (Exception e) {
            log.debug("OCR failed", e);
            return null;
        }
    }


    /**
     * Trigger alarm
     */
    private void triggerAlarm(String type) {
        log.info("🚨 Triggering alarm for: {}", type);
        
        // Play alarm sound
        try {
            AlarmPlayer.play(config.getAlarmSoundPath());
            log.info("Alarm sound played");
        } catch (Exception e) {
            log.error("Failed to play alarm sound", e);
        }
        
        // Optional: Send notification
        if (config.isDesktopNotificationEnabled()) {
            sendDesktopNotification(type);
        }
    }
    
    /**
     * Send desktop notification
     */
    private void sendDesktopNotification(String type) {
        try {
            String title = "Telegram Alert";
            String message = type.equals("mention") ? "Someone mentioned you!" : 
                           type.equals("dm") ? "New direct message!" : "Unread message!";
            
            // Use Java TrayIcon for notification
            if (isSupported()) {
                SystemTray tray = SystemTray.getSystemTray();
                Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png"));
                TrayIcon trayIcon = new TrayIcon(icon, "Telegram Monitor");
                trayIcon.setImageAutoSize(true);
                tray.add(trayIcon);
                trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
                tray.remove(trayIcon);
            }
        } catch (Exception e) {
            log.debug("Desktop notification failed", e);
        }
    }
    
    /**
     * Capture screenshot for debugging
     */
    public void captureScreenshot(String saveDir,Screen screen) {
        try {
             // Screen screen=new Screen();
            ScreenImage img = screen.capture();
            img.save( saveDir);
            log.info("Screenshot saved: {}", saveDir);
        } catch (Exception e) {
            log.error("Failed to capture screenshot", e);
        }
    }

}
