package com.mntalm;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;

/**
 * Configuration Manager
 */
public class Config {
    private static final Logger log = LoggerFactory.getLogger(Config.class);
    private static final String CONFIG_FILE = "config.json";
    
    // Default values
    private int checkIntervalMs = 2000;          // Check every 2 seconds
    private int searchTimeoutMs = 1000;          // Pattern search timeout
    private double patternSimilarity = 0.8;      // Pattern matching similarity
    private boolean ocrEnabled = false;          // Enable OCR text detection
    private String alarmSoundPath = "alarm.mp3"; // Alarm sound file
    private boolean desktopNotification = true;  // Show desktop notifications
    private String telegramWindow = "Telegram";  // Window title to focus
    
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    public Config() {
        // Use defaults
    }
    
    /**
     * Load configuration from file
     */
    public static Config load(String configDir) {
        Config config = new Config();
        File configFile = new File(configDir, CONFIG_FILE);
        
        if (configFile.exists()) {
            try {
                String json = Files.readString(configFile.toPath());
                Config loaded = gson.fromJson(json, Config.class);
                
                // Merge with defaults
                if (loaded.checkIntervalMs > 0) {
                    config.checkIntervalMs = loaded.checkIntervalMs;
                }
                if (loaded.searchTimeoutMs > 0) {
                    config.searchTimeoutMs = loaded.searchTimeoutMs;
                }
                if (loaded.patternSimilarity > 0 && loaded.patternSimilarity <= 1.0) {
                    config.patternSimilarity = loaded.patternSimilarity;
                }
                if (loaded.alarmSoundPath != null) {
                    config.alarmSoundPath = loaded.alarmSoundPath;
                }
                if (loaded.telegramWindow != null) {
                    config.telegramWindow = loaded.telegramWindow;
                }
                config.ocrEnabled = loaded.ocrEnabled;
                config.desktopNotification = loaded.desktopNotification;
                
                log.info("Configuration loaded from: {}", configFile.getAbsolutePath());
                
            } catch (IOException e) {
                log.error("Failed to load config, using defaults", e);
            }
        } else {
            log.info("Config file not found, creating with defaults");
            config.save(configDir);
        }
        
        return config;
    }
    
    /**
     * Save configuration to file
     */
    public void save(String configDir) {
        File configFile = new File(configDir, CONFIG_FILE);
        try {
            String json = gson.toJson(this);
            Files.writeString(configFile.toPath(), json);
            log.info("Configuration saved to: {}", configFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("Failed to save config", e);
        }
    }
    
    // Getters
    
    public int getCheckIntervalMs() {
        return checkIntervalMs;
    }
    
    public int getSearchTimeoutMs() {
        return searchTimeoutMs;
    }
    
    public double getPatternSimilarity() {
        return patternSimilarity;
    }
    
    public boolean isOcrEnabled() {
        return ocrEnabled;
    }
    
    public String getAlarmSoundPath() {
        return alarmSoundPath;
    }
    
    public boolean isDesktopNotificationEnabled() {
        return desktopNotification;
    }
    
    public String getTelegramWindow() {
        return telegramWindow;
    }
    
    // Setters
    
    public void setCheckIntervalMs(int checkIntervalMs) {
        this.checkIntervalMs = checkIntervalMs;
    }
    
    public void setSearchTimeoutMs(int searchTimeoutMs) {
        this.searchTimeoutMs = searchTimeoutMs;
    }
    
    public void setPatternSimilarity(double patternSimilarity) {
        this.patternSimilarity = patternSimilarity;
    }
    
    public void setOcrEnabled(boolean ocrEnabled) {
        this.ocrEnabled = ocrEnabled;
    }
    
    public void setAlarmSoundPath(String alarmSoundPath) {
        this.alarmSoundPath = alarmSoundPath;
    }
    
    public void setDesktopNotification(boolean desktopNotification) {
        this.desktopNotification = desktopNotification;
    }
    
    public void setTelegramWindow(String telegramWindow) {
        this.telegramWindow = telegramWindow;
    }
}
