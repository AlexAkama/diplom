package project.service.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.dto.auth.user.AuthUserDto;
import project.exception.NotFoundException;
import project.exception.UnauthorizedException;
import project.model.User;
import project.repository.PostRepository;
import project.repository.UserRepository;
import project.security.SecurityUser;
import project.service.UserService;

import static project.model.emun.ModerationStatus.NEW;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    @Value("${avatar.default}")
    private String defaultAvatar;

    public UserServiceImpl(UserRepository userRepository,
                           PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Override
    public User createUser(String name, String email, String password) {
        String encodePassword = bCryptPasswordEncoder.encode(password);
        User user = new User(name, email, encodePassword);
        user.setPhoto(defaultAvatar);
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) throws NotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format("User %s not found", email)));
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
            dto.setModerationCounter(postRepository.countAllByActiveAndModerationStatus(true, NEW));
        } else {
            dto.setModeration(false);
            dto.setSettings(false);
            dto.setModerationCounter(0);
        }
        return dto;
    }

    @Override
    public AuthUserDto createAuthUserDtoByEmail(String email) throws NotFoundException {
        return createAuthUserDto(findByEmail(email));
    }

    public User checkUser() throws UnauthorizedException, NotFoundException {
        try {
            SecurityUser securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userRepository.findByEmail(securityUser.getUsername()).orElseThrow();
        } catch (Exception e) {
            throw new UnauthorizedException("Требуется авторизация");
        }
    }

}
