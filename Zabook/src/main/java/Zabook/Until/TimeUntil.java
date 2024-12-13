package Zabook.Until;
import java.time.Duration;

import java.time.LocalDateTime;

public class TimeUntil {
    public static String timeAgo(LocalDateTime dateTime) {
        Duration duration = Duration.between(dateTime, LocalDateTime.now());

        if (duration.toMinutes() < 1) {
            return "Vừa xong";
        } else if (duration.toMinutes() < 60) {
            return duration.toMinutes() + " phút trước";
        } else if (duration.toHours() < 24) {
            return duration.toHours() + " giờ trước";
        } else if (duration.toDays() < 30) {
            return duration.toDays() + " ngày trước";
        } else if (duration.toDays() < 365) {
            return duration.toDays() / 30 + " tháng trước";
        } else {
            return duration.toDays() / 365 + " năm trước";
        }
    }

}
