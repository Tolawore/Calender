class Settings {
    private Calendar calendar;

    public Settings(Calendar calendar) {
        this.calendar = calendar;
    }

    public void changeView(String view) {
        // Change the calendar view
        System.out.println("Calendar view changed to " + view + ".");
    }

    public void toggleNotifications(boolean enabled) {
        // Enable or disable notifications
        System.out.println("Notifications " + (enabled ? "enabled" : "disabled") + ".");
    }
}
