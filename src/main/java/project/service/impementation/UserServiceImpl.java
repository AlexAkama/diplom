package project.service.impementation;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.dto.auth.user.AuthUserDto;
import project.exception.UnauthorizedException;
import project.exception.UserNotFoundException;
import project.model.User;
import project.repository.UserRepository;
import project.service.PostService;
import project.service._UserService;

@Service
public class UserServiceImpl implements _UserService {

    private final UserRepository userRepository;
    private final PostService postService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public UserServiceImpl(UserRepository userRepository,
                           PostService postService) {
        this.userRepository = userRepository;
        this.postService = postService;
    }

    @Override
    public User createUser(String name, String email, String password) {
        String encodePassword = bCryptPasswordEncoder.encode(password);
        User user = new User(name, email, encodePassword);
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(String.format("User %s not found", email)));
    }

    @Override
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User createAndSaveUser(String name, String email, String password) {
        return save(createUser(name, email, password));
    }

    @Override
    public AuthUserDto createAuthUserDto(User user) {
        AuthUserDto dto = new AuthUserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setPhoto(user.getPhoto());
        dto.setEmail(user.getEmail());
        if (user.isModerator()) {
            dto.setModeration(true);
            dto.setSettings(true);
            dto.setModerationCounter(postService.getModerationCounter());
        } else {
            dto.setModeration(false);
            dto.setSettings(false);
            dto.setModerationCounter(0);
        }
        return dto;
    }

    @Override
    public AuthUserDto createAuthUserDtoByEmail(String email) throws UserNotFoundException {
        return createAuthUserDto(findByEmail(email));
    }

    public User checkUser() throws UnauthorizedException, UserNotFoundException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails != null) {
            String email = userDetails.getUsername();
            return findByEmail(email);
        } else {
            throw new UnauthorizedException("Требуется авторизация");
        }
    }

}
