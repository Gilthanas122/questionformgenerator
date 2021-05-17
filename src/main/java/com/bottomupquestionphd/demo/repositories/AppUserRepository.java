package com.bottomupquestionphd.demo.repositories;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

  Optional<AppUser> findByUsername(String username);

  boolean existsByUsername(String username);

  @Transactional
  @Modifying
  @Query("UPDATE AppUser a SET a.disabled = 1 WHERE a.id = ?1")
  void setUserToDisabled(long id);

  @Query("SELECT CASE WHEN (COUNT(a)> 0) THEN true ELSE false END FROM AppUser a WHERE a.emailId = ?1")
  boolean existByEmailId(String emailId);

  @Query("SELECT a FROM AppUser a WHERE a.emailId = ?1")
  AppUser findByEmailId(String userEmail);

  @Query("SELECT a from AppUser a WHERE a.confirmationToken = ?1")
  AppUser findByConfirmationToken(String token);
}
