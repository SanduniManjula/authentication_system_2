package authentication.securecontroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


    @RestController
    @RequestMapping("/api")
    public class SecureController {

        @GetMapping("/secure-data")
        public String getSecureData() {
            return "This is secure data. Welcome, "+ "!";
        }
    }

