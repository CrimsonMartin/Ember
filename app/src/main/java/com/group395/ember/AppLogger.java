package com.group395.ember;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class AppLogger {

    private Logger uiLogger;
    private Logger searchLogger;

    public AppLogger() {
        uiLogger = new Logger("UI Log", null);
        searchLogger = new Logger("SEARCH Log", null);
    }

    public void log_ui(String msg, Exception e) {
        uiLogger.log(Level.WARNING, msg, e);
    }

    public void log_search(String msg, Exception e) {
        searchLogger.log(Level.WARNING, msg, e);
    }

    public void log_search(String msg) {
        searchLogger.log(Level.INFO, msg);
    }

    public void log_error(String method, Exception e) {
        searchLogger.throwing("APP", method, e);
    }

}
