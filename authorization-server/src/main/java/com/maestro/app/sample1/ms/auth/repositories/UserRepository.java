package com.maestro.app.sample1.ms.auth.repositories;

import java.util.Date;
import java.util.Optional;

import com.maestro.app.sample1.ms.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);

	@Modifying
	@Query(value = "UPDATE User SET lastLogon = :date WHERE id = :id")
	void updateLastLogon(@Param("id") String id, @Param("date") Date date);
}
