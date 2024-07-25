package authentication.service;
import authentication.model.Userz;
import authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService  {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveUser(Userz users) throws UsernameNotFoundException {
        checkIfNewUser(users.getUsername());

    	users.setPassword(passwordEncoder.encode(users.getPassword()));
    }


    public Optional<Userz> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void checkIfNewUser(String username) throws UsernameNotFoundException {
        Optional<Userz> userzOptional = userRepository.findByUsername(username);

        if(userzOptional.isPresent()) {
            throw new UsernameNotFoundException("User Already Exists");
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Userz> user = userRepository.findByUsername(username);

        if(!user.isPresent()) {
            throw new UsernameNotFoundException("Username Not Found");
        }

        return user.get();
    }
}
