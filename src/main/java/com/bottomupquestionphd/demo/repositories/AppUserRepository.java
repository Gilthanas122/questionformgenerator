package com.bottomupquestionphd.demo.repositories;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

  Optional<AppUser> findByUsername(String username);

  boolean existsByUsername(String username);
}
