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

import java.util.HashMap;
import java.util.Map;

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
	public ResponseEntity<Map<String, String>> signup(@RequestBody Userz users) {
		try {
			userDetailService.saveUser(users);

//			UsernamePasswordAuthenticationToken authenticationToken =
//					new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword());
//			Authentication authentication = authenticationManager.authenticate(authenticationToken);
//			SecurityContextHolder.getContext().setAuthentication(authentication);

			final String jwt = jwtUtil.generateToken(users.getUsername());
			Map<String, String> response = new HashMap<>();
			response.put("jwt_token", jwt);

			return ResponseEntity.ok(response);

		} catch (UserAlreadyExistsException exception) {
			exception.printStackTrace();
			return ResponseEntity.badRequest().body(Map.of("error", "Username already exists."));
		} catch (Exception exception) {
			exception.printStackTrace();
			return ResponseEntity.internalServerError().body(Map.of("error", "Internal server error. No Token generated."));
		}
	}

	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(@RequestBody Userz users) {
		System.out.println("========= REACHED HERE ===========");

		try {
			UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(users.getUsername(), users.getPassword());
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);

			final String jwt = jwtUtil.generateToken(users.getUsername());
			Map<String, String> response = new HashMap<>();
			response.put("jwt_token", jwt);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
		}
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<Map<String, String>> handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
		return ResponseEntity.badRequest().body(Map.of("error", exception.getMessage()));
	}
}
