package project.service;

import project.model.User;

public interface UserService {

    User createUser(String name, String email, String password);

    User findByEmail (String email);

    boolean existByEmail(String email);

    User save(User user);

    User createAndSaveUser(String name, String email, String password);

}
