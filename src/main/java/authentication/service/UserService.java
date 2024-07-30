package authentication.service;

import authentication.exception.UserAlreadyExistsException;
import authentication.model.Userz;
import authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public void saveUser(Userz users) throws UserAlreadyExistsException {
        checkIfNewUser(users.getUsername());

        users.setPassword(passwordEncoder.encode(users.getPassword()));
        userRepository.save(users);
    }

    public Optional<Userz> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void checkIfNewUser(String username) throws UserAlreadyExistsException {
        Optional<Userz> userzOptional = userRepository.findByUsername(username);

        if (userzOptional.isPresent()) {
            throw new UserAlreadyExistsException("User Already Exists");
        }
    }
    public void authenticateUser(Userz users) {
        UserDetails userDetails = loadUserByUsername(users.getUsername());
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, users.getPassword(), userDetails.getAuthorities());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Userz> user = userRepository.findByUsername(username);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException("Username Not Found");
        }

        return user.get();
    }
    public String generateRefreshToken(Userz user) {
        String refreshToken = UUID.randomUUID().toString();
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
        return refreshToken;
    }

    public Optional<Userz> findByRefreshToken(String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken);
    }
}
