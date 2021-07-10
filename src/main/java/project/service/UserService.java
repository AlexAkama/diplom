package project.service;

import lombok.RequiredArgsConstructor;
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

import static project.model.enums.ModerationStatus.NEW;

@Service
@RequiredArgsConstructor
public class UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Value("${avatar.default}")
    private String defaultAvatar;


    public User createUser(String name, String email, String password) {
        String encodePassword = bCryptPasswordEncoder.encode(password);
        var user = new User(name, email, encodePassword);
        user.setPhoto(defaultAvatar);
        return userRepository.save(user);
    }

    public User findByEmail(String email) throws NotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format("User %s not found", email)));
    }

    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User createAndSaveUser(String name, String email, String password) {
        return save(createUser(name, email, password));
    }

    public AuthUserDto createAuthUserDto(User user) {
        var dto = new AuthUserDto();
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

    public AuthUserDto createAuthUserDtoByEmail(String email) throws NotFoundException {
        return createAuthUserDto(findByEmail(email));
    }

    public User checkUser() throws UnauthorizedException {
        try {
            var securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userRepository.findByEmail(securityUser.getUsername()).orElseThrow();
        } catch (Exception e) {
            throw new UnauthorizedException("Требуется авторизация");
        }
    }

    public User findByCode(String code) throws NotFoundException {
        return userRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

}
