import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final String filename;

    public Storage(String username) {
        this.filename = username + "_events.ser";
    }

    public void saveEvents(List<CalendarEvent> events) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(events);
            System.out.println("Events saved to storage.");
        } catch (IOException e) {
            System.out.println("Error saving events: " + e.getMessage());
        }
    }

    public List<CalendarEvent> loadEvents() {
        List<CalendarEvent> events = new ArrayList<>();
        File file = new File(filename);

        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                events = (List<CalendarEvent>) in.readObject();
                System.out.println("Events loaded from storage.");
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading events: " + e.getMessage());
            }
        } else {
            System.out.println("No stored events found for the user.");
        }

        return events;
    }
}
