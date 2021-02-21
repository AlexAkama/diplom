package diploma.service;

import diploma.model.User;

import java.util.Random;

public class UserServiceImpl implements UserService {

    @Override
    public User getTestUser(String name) {
        User user = new User();
        user.setId(new Random().nextInt(999));
        user.setName(name);
        user.setPhoto("/img/avatars/" + name + ".jpg");
        user.setEmail(name + "@bk.ru");
        user.setModerator(false);
        return user;
    }

    @Override
    public User getTestModerator(String name) {
        User moderator = getTestUser(name);
        moderator.setModerator(true);
        return moderator;
    }

}
