package com.bottomupquestionphd.demo.repositories;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

  AppUser findByUsername(String username);

  boolean existsByUsername(String username);
}
