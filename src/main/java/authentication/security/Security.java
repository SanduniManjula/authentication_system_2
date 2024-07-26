package authentication.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import authentication.handler.CustomAuthenticationSuccessHandler;
import org.springframework.security.authentication.AuthenticationManager;

import authentication.service.UserService;

@Configuration
@EnableWebSecurity
public class Security extends WebSecurityConfigurerAdapter {

	private UserService userService;
	private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;


	 @Autowired
     public void setService(UserService userService) {
	        this.userService = userService;
	    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/auth/signup", "/api/auth/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
//                .loginProcessingUrl("/api/auth/login").permitAll()
//                .successHandler(successHandler)
                .permitAll();
              // .defaultSuccessUrl("/home", true)
                // .and()
             //  .logout();
               
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
