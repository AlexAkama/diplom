package diploma.service;

import diploma.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User getTestUser(String name);

    User getTestModerator(String name);

}
