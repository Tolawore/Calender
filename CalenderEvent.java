import java.io.Serializable;
import java.time.LocalDateTime;

class CalendarEvent implements Serializable {
    private String title;
    private LocalDateTime start;
    private LocalDateTime end;

    public CalendarEvent(String title, LocalDateTime start, LocalDateTime end) {
        this.title = title;
        this.start = start;
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }
} 
