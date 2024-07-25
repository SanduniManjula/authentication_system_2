package authentication.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import authentication.model.Userz;
import authentication.service.UserService;



@RestController
@RequestMapping("/api/auth")

public class AuthenticationController{

	 @Autowired
	    private UserService userDetailService;

	    @PostMapping("/signup")
	    public ResponseEntity<String> signup(@RequestBody Userz users) {
			System.out.println("username: ");
			System.out.print(users.getUsername());

			try {
				userDetailService.saveUser(users);
 			} catch (Exception exception) {
				if(exception instanceof UsernameNotFoundException) {
					return  ResponseEntity.badRequest().body("Username already exits");
				}

				return ResponseEntity.internalServerError().body("Internal server error");
			}
	        return ResponseEntity.ok("User registered successfully");
	    }

	@PostMapping("/login")
	public ResponseEntity<String> login() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		return ResponseEntity.ok("Users " + username + " logged successfully");
	}

	   
	}



