package diploma.service;

import diploma.dao.DaoUserService;
import diploma.model.User;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    private final DaoUserService daoUserService;

    public UserServiceImpl(DaoUserService daoUserService) {
        this.daoUserService = daoUserService;
    }

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

    @Override
    public User createNewUser(String name, String email, String password) {
        User user = new User(name, email, password);
        daoUserService.saveUser(user);
        return user;
    }

}
