package ru.danil42russia.agent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Logger {
    private static final DateFormat DATE_ROW = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    private static final String LOG_FORMAT = "%s %s: %s%n";

    private static Path logFilePath;
    private static boolean inited = false;

    public static void initLogger() {
        if (inited) {
            return;
        }

        String debugPath = getDebugPath();
        if (debugPath == null) {
            return;
        }

        File logFile = new File(debugPath);
        if (!logFile.exists()) {
            try {
                boolean result = logFile.createNewFile();
                if (!result) {
                    return;
                }

            } catch (IOException e) {
                return;
            }
        }

        if (!logFile.isFile()) {
            return;
        }

        if (!logFile.canWrite()) {
            return;
        }

        logFilePath = logFile.toPath();
        inited = true;
    }

    public static void debug(String content) {
        Logger.log("DEBUG", content);
    }

    public static void error(Throwable throwable) {
        Logger.log("ERROR", throwable.toString());
    }

    private static void log(String level, String content) {
        if (!inited) {
            return;
        }

        String message = String.format(LOG_FORMAT, dateLogPrefix(), level, content);
        try {
            Files.writeString(logFilePath, message, StandardOpenOption.APPEND);
        } catch (IOException e) {
            return;
        }
    }

    private static String dateLogPrefix() {
        return DATE_ROW.format(new Date());
    }

    private static String getDebugPath() {
        String debugPath = System.getenv("GLA_DEBUG");
        if (debugPath != null && !debugPath.isBlank()) {
            return debugPath;
        }

        debugPath = System.getProperty("gla.debug");
        if (debugPath != null && !debugPath.isBlank()) {
            return debugPath;
        }

        return null;
    }
}
