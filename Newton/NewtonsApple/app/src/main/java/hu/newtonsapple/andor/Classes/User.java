package hu.newtonsapple.andor.Classes;

/**
 * Created by Andor on 2017.08.14..
 */

public class User {
    String id, name;
    int point;

    public User() {

    }

    public User(String id, String name, int point) {
        this.id = id;
        this.name = name;
        this.point = point;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPoint() {
        return point;
    }
}
