package project.service;

import project.dto.auth.user.AuthUserDto;
import project.exception.NotFoundException;
import project.exception.UnauthorizedException;
import project.model.User;

public interface UserService {

    User createUser(String name, String email, String password);

    User findByEmail (String email) throws NotFoundException;

    boolean existByEmail(String email);

    User save(User user);

    User createAndSaveUser(String name, String email, String password);

    AuthUserDto createAuthUserDto(User user);

    AuthUserDto createAuthUserDtoByEmail(String email) throws NotFoundException;

    User checkUser() throws UnauthorizedException, NotFoundException;

    User findByCode(String code) throws NotFoundException;

}
