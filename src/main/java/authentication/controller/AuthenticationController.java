package authentication.controller;
import authentication.exception.UserAlreadyExistsException;
import authentication.model.Userz;
import authentication.service.UserService;
import authentication.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	@Autowired
	private UserService userDetailService;
	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody Userz users) {
		try {
			userDetailService.saveUser(users);

			UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword());
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);

			final String jwt = jwtUtil.generateToken(users.getUsername());

			return ResponseEntity.ok("User registered and logged in successfully. Token: " + jwt);
		} catch (UserAlreadyExistsException exception) {
			return ResponseEntity.badRequest().body("Username already exists");
		} catch (Exception exception) {
			return ResponseEntity.internalServerError().body("Internal server error");
		}
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Userz users) {
		System.out.println("========= REACHED HERE ===========");


		try {
			UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword());
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);

			final String jwt = jwtUtil.generateToken(users.getUsername());

			return ResponseEntity.ok("User " + users.getUsername() + " logged in successfully. Token: " + jwt);
		} catch (Exception e) {
			return ResponseEntity.status(401).body("Invalid username or password");
		}
	}
	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
		return ResponseEntity.badRequest().body(exception.getMessage());
	}

}
