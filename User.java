public class user {
    private String username;
    private Calendar calendar;
    private Settings settings;

    public user(String username) {
        this.username = username;
        this.calendar = new Calendar();
        this.settings = new Settings(calendar);
    }

    public String getUsername() {
        return username;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public Settings getSettings() {
        return settings;
    }
}
