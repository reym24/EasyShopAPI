package org.yearup.security;

import org.yearup.data.UserDao;
import org.yearup.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class UserModelDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserModelDetailsService.class);

    private final UserDao userDao;

    public UserModelDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        log.debug("Authenticating user '{}'", username);
        String lowercaseUsername = username.toLowerCase();
        User user = userDao.getByUserName(lowercaseUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseUsername + " was not found"));

        return createSpringSecurityUser(lowercaseUsername, user);
    }

    private User createSpringSecurityUser(String lowercaseUsername, User user) {
        if (!user.isActivated()) {
            throw new UserNotActivatedException("User " + lowercaseUsername + " was not activated");
        }
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());
        return new User(
                user.getUsername(),
                user.getPassword(),
                grantedAuthorities
        );
    }
}