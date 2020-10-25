package webserver.user;

import db.DataBase;
import model.User;

import java.util.Collection;
import java.util.Map;

public class UserService {
    public static User join(Map<String, String> params) {
        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        DataBase.addUser(user);
        return user;
    }

    public static Collection<User> findUsers() {
        return DataBase.findAll();
    }
}
