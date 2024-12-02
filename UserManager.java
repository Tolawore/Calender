import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<user> users;

    public UserManager() {
        this.users = new ArrayList<>();
    }

    public void addUser(String username) {
        user user = new user(username);
        users.add(user);
        System.out.println("User '" + username + "' added successfully.");
    }

    public user getUser(String username) {
        for (user user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void removeUser(String username) {
        user user = getUser(username);
        if (user != null) {
            users.remove(user);
            System.out.println("User '" + username + "' removed successfully.");
        } else {
            System.out.println("User '" + username + "' not found.");
        }
    }

    public List<user> getUsers() {
        return users;
    }
}
