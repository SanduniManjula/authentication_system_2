package authentication.repository;

import authentication.model.Userz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;




	public interface UserRepository extends JpaRepository<Userz, Long> {
	   

		Optional<Userz> findByUsername(String username);


		Optional<Userz> findByRefreshToken(String refreshToken);
	}

	

