package diploma.dao;

import diploma.model.User;

import java.util.Optional;

public interface DaoUserService {

    boolean isEmailExist(String email);

    boolean isCodeCorrect(String code, String secret);

    void saveUser(User user);

    Optional <User> findByEmail(String email);

}
