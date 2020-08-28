package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.DevUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DevUserRepository extends JpaRepository<DevUser, Long> {

    DevUser findByUserName(String username);
}
