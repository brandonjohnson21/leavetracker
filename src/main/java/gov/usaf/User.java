package gov.usaf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {
    ArrayList<User> subordinates=new ArrayList<>();
    Integer id;
    int permissionLevel;
    public Integer getId() {
        return id;
    }
    User() {
        this.id = UserTracker.getNextId();
    }

    public User setPermissionLevel(int permissionLevel) {
        this.permissionLevel = permissionLevel;
        return this;
    }

    public User addSubordinate(User user) {
        this.subordinates.add(user);
        return this;
    }

    public List<User> getSubordinates() {
        return Collections.unmodifiableList(this.subordinates);
    }
}
