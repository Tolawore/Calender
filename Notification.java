public class Notification {
    private Calendar calendar;

    public Notification(Calendar calendar) {
        this.calendar = calendar;
    }

    public Notification() {
    }

    public void setReminder(CalendarEvent event, String time) {
        // Set a reminder for the event at the specified time
        System.out.println("Reminder set for event '" + event.getTitle() + "' at " + time + ".");
    }

    public void sendNotification(CalendarEvent event) {
        // Send a notification for the event
        System.out.println("Notification sent for event '" + event.getTitle() + "'.");
    }
}
