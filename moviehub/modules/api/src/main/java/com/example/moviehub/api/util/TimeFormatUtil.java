package com.example.moviehub.api.util;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class TimeFormatUtil {

    private TimeFormatUtil() {} // util - brak tworzenia instancji

    // Formatter daty w polskim formacie + strefa systemowa
    private static final DateTimeFormatter PL_FMT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
                    .withZone(ZoneId.systemDefault());

    // Zwraca datę w formacie PL lub "-" jeśli null
    public static String formatPl(Instant instant) {
        if (instant == null) return "-";
        return PL_FMT.format(instant);
    }

    // Zwraca tekst typu "5 min temu" na podstawie różnicy do teraz
    public static String ago(Instant instant) {
        if (instant == null) return "-";

        // Różnica czasu (instant -> teraz)
        Duration d = Duration.between(instant, Instant.now());
        if (d.isNegative()) d = Duration.ZERO; // jeśli data jest w przyszłości, nie pokazuj ujemnych wartości

        long seconds = d.getSeconds();
        if (seconds < 60) return seconds + " s temu";

        long minutes = seconds / 60;
        if (minutes < 60) return minutes + " min temu";

        long hours = minutes / 60;
        if (hours < 24) return hours + " h temu";

        long days = hours / 24;
        return days + " dni temu";
    }
}
