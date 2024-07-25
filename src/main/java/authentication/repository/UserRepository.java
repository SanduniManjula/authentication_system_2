package authentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import authentication.model.Userz;




	public interface UserRepository extends JpaRepository<Userz, Long> {
	   

		Optional<Userz> findByUsername(String username);

		

		
	}

	

