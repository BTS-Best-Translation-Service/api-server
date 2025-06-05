package project.bts.user.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import project.bts.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(UUID userId);
}