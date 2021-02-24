package diploma.security;

import diploma.dao.DaoUserService;
import diploma.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final DaoUserService daoUserService;

    public UserDetailsServiceImpl(DaoUserService daoUserService) {
        this.daoUserService = daoUserService;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = daoUserService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User " + email+ " not found"));
        return SecurityUser.fromUser(user);
    }
}
