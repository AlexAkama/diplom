package diploma.service;

import diploma.model.User;

public interface UserService {

    User getTestUser(String name);

    User getTestModerator(String name);

    User createNewUser(String name, String email, String password);

}
