package com.speedcubers.speedcubing.util;

import org.springframework.stereotype.Component;

@Component
public class TimeUtils {

    public String format(Integer timeMs) {
        if (timeMs == null) return "-";
        return format(timeMs.intValue());
    }

    public String format(int timeMs) {
        if (timeMs < 60_000) {
            return String.format("%.2f", timeMs / 1000.0);
        }
        int mins = timeMs / 60_000;
        int secs = (timeMs % 60_000) / 1000;
        int cents = (timeMs % 1000) / 10;
        return String.format("%d:%02d.%02d", mins, secs, cents);
    }

    public String format(Double timeMs) {
        if (timeMs == null || timeMs.isNaN()) return "-";
        if (timeMs < 60_000) {
            return String.format("%.2f", timeMs / 1000.0);
        }
        int totalSecs = (int) (timeMs / 1000);
        int mins = totalSecs / 60;
        double secs = timeMs / 1000.0 - mins * 60;
        return String.format("%d:%05.2f", mins, secs);
    }
}
