package com.mntalm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main Entry Point
 */
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    
    public static void main(String[] args) {
        log.info("===========================================");
        log.info("  Telegram Web Monitor Alarm v1.0");
        log.info("===========================================");
        
        // Determine working directory
        String workDir = System.getProperty("user.dir");
        log.info("Working directory: {}", workDir);
        
        // Load configuration
        Config config = Config.load(workDir);
        log.info("Check interval: {} ms", config.getCheckIntervalMs());
        log.info("OCR enabled: {}", config.isOcrEnabled());
        log.info("Alarm sound: {}", config.getAlarmSoundPath());
        
        // Verify alarm sound exists
        if (!java.nio.file.Paths.get(config.getAlarmSoundPath()).toFile().exists()) {
            log.warn("Alarm sound file not found: {}", config.getAlarmSoundPath());
            log.warn("Please place alarm.mp3 in the working directory");
        }
        
        // Create monitor
        TelegramMonitor monitor = new TelegramMonitor(config);
        
        // Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down...");
            monitor.stopMonitoring();
        }));
        
        // Start monitoring
        log.info("Starting monitoring...");
        log.info("Please open Telegram Web in your browser");
        log.info("Press Ctrl+C to stop");
        
        try {
            monitor.startMonitoring();
        } catch (Exception e) {
            log.error("Monitoring failed", e);
            System.exit(1);
        }
    }
}
