package com.datien.movie.user.repo;

import com.datien.movie.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

//    @Modifying
//    @Query("""
//           DELETE FROM User u
//           WHERE u.id = :userId
//           """)
//    void deleteByPassword(Long userId);
}
