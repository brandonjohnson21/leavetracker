package gov.usaf;

import java.util.ArrayList;

public class UserTracker {
    static ArrayList<User> USERS = new ArrayList<>();
    public static int getNextId() {
        int id=0;
        for (User u:USERS) {
            id=Math.max(u.getId(),id);
        }
        return id+1;
    }
    public static User generateUser() {
        User user = new User();
        USERS.add(user);
        return user;
    }
}
