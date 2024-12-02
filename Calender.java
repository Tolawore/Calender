import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Calendar {
    private List<CalendarEvent> events;

    public Calendar() {
        this.events = new ArrayList<>();
    }

    public void addEvent(CalendarEvent event) {
        this.events.add(event);
    }

    public void removeEvent(CalendarEvent event) {
        this.events.remove(event);
    }

    public List<CalendarEvent> getEvents() {
        return this.events;
    }

    public void setEvents(List<CalendarEvent> events) {
        this.events = events;
    }

    public void sortEvents() {
        Collections.sort(this.events, (a, b) -> a.getStart().compareTo(b.getStart()));
    }
}
