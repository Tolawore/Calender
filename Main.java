import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class Main {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Timer timer = new Timer();
    private static UserManager userManager = new UserManager();

    public static void main(String[] args) {
        boolean running = true;
        Scanner scanner = new Scanner(System.in);

        while (running) {
            System.out.println("Select an option:");
            System.out.println("1. Add User");
            System.out.println("2. Select User");
            System.out.println("3. Remove User");
            System.out.println("4. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    userManager.addUser(username);
                    break;
                case 2:
                    System.out.print("Enter username: ");
                    String selectedUsername = scanner.nextLine();
                    user selectedUser = userManager.getUser(selectedUsername);
                    if (selectedUser != null) {
                        userMenu(scanner, selectedUser);
                    } else {
                        System.out.println("User not found.");
                    }
                    break;
                case 3:
                    System.out.print("Enter username: ");
                    String usernameToRemove = scanner.nextLine();
                    userManager.removeUser(usernameToRemove);
                    break;
                case 4:
                    running = false;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }

        scanner.close();
        timer.cancel();
    }

    private static void userMenu(Scanner scanner, user user) {
        Calendar calendar = user.getCalendar();
        Settings settings = user.getSettings();
        Storage storage = new Storage(calendar.toString());
        Notification notification = new Notification();

        boolean userMenuRunning = true;

        while (userMenuRunning) {
            System.out.println("Select an option:");
            System.out.println("1. Add Event");
            System.out.println("2. Remove Event");
            System.out.println("3. View Events");
            System.out.println("4. Set Reminder");
            System.out.println("5. Change View");
            System.out.println("6. Toggle Notifications");
            System.out.println("7. Save Events");
            System.out.println("8. Load Events");
            System.out.println("9. Back to User Selection");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter event title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter start date and time (YYYY-MM-DD hh:mm:ss): ");
                    String startInput = scanner.nextLine();
                    System.out.print("Enter end date and time (YYYY-MM-DD hh:mm:ss): ");
                    String endInput = scanner.nextLine();
                    LocalDateTime start = LocalDateTime.parse(startInput.trim(), formatter);
                    LocalDateTime end = LocalDateTime.parse(endInput.trim(), formatter);
                    CalendarEvent newEvent = new CalendarEvent(title, start, end);
                    calendar.addEvent(newEvent);
                    System.out.println("Event added successfully.");
                    break;
                case 2:
                    System.out.print("Enter the index of the event to remove: ");
                    int index = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    if (index >= 0 && index < calendar.getEvents().size()) {
                        calendar.removeEvent(calendar.getEvents().get(index));
                        System.out.println("Event removed successfully.");
                    } else {
                        System.out.println("Invalid event index.");
                    }
                    break;
                case 3:
                    System.out.println("Upcoming Events:");
                    calendar.sortEvents();
                    for (int i = 0; i < calendar.getEvents().size(); i++) {
                        CalendarEvent event = calendar.getEvents().get(i);
                        System.out.printf("%d. %s (%s - %s)%n", i, event.getTitle(), event.getStart(), event.getEnd());
                    }
                    break;
                case 4:
                    System.out.print("Enter the index of the event to set a reminder for: ");
                    String reminderIndexInput = scanner.nextLine();
                    try {
                        int reminderIndex = Integer.parseInt(reminderIndexInput);
                        if (reminderIndex >= 0 && reminderIndex < calendar.getEvents().size()) {
                            CalendarEvent eventToRemind = calendar.getEvents().get(reminderIndex);
                            System.out.print("Enter the reminder time (in minutes): ");
                            String reminderTimeInput = scanner.nextLine();
                            int reminderTime = Integer.parseInt(reminderTimeInput);
                            scheduleReminder(eventToRemind, reminderTime, notification);
                        } else {
                            System.out.println("Invalid event index.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter an integer.");
                    }
                    break;
                case 5:
                    System.out.print("Enter the new calendar view: ");
                    String newView = scanner.nextLine();
                    settings.changeView(newView);
                    break;
                case 6:
                    System.out.print("Enable or disable notifications (true/false): ");
                    boolean notificationsEnabled = scanner.nextBoolean();
                    scanner.nextLine(); // Consume newline
                    settings.toggleNotifications(notificationsEnabled);
                    break;
                case 7:
                    storage.saveEvents(calendar.getEvents());
                    System.out.println("Events saved to storage.");
                    break;
                case 8:
                    storage.loadEvents();
                    System.out.println("Events loaded from storage.");
                    break;
                case 9:
                    userMenuRunning = false;
                    System.out.println("Returning to user selection...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private static void scheduleReminder(CalendarEvent event, int reminderMinutes, Notification notification) {
        LocalDateTime reminderTime = event.getStart().minusMinutes(reminderMinutes);
        long delay = LocalDateTime.now().until(reminderTime, java.time.temporal.ChronoUnit.MILLIS);

        if (delay > 0) {
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.schedule(() -> {
                notification.sendNotification(event);
                scheduler.shutdown();
            }, delay, TimeUnit.MILLISECONDS);
            System.out.println("Reminder set for event '" + event.getTitle() + "' at " + reminderMinutes + " minutes before.");
        } else {
            System.out.println("Reminder time has been set successfully for event '" + event.getTitle() + "'.");
        }
    }

    private static class ReminderTask extends TimerTask {
        private final CalendarEvent event;
        private final Notification notification;

        ReminderTask(CalendarEvent event, Notification notification) {
            this.event = event;
            this.notification = notification;
        }

        @Override
        public void run() {
            notification.sendNotification(event);
            scheduleNotification(event, notification);
        }
    }

    private static void scheduleNotification(CalendarEvent event, Notification notification) {
        long delay = event.getStart().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli() - System.currentTimeMillis();

        if (delay > 0) {
            timer.schedule(new NotificationTask(event, notification), delay);
        } else {
            System.out.println("Notification time has already passed for event '" + event.getTitle() + "'.");
        }
    }

    private static class NotificationTask extends TimerTask {
        private final CalendarEvent event;
        private final Notification notification;

        NotificationTask(CalendarEvent event, Notification notification) {
            this.event = event;
            this.notification = notification;
        }

        @Override
        public void run() {
            notification.sendNotification(event);
        }
    }
}
