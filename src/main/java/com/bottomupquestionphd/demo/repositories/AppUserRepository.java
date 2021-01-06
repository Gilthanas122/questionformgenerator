package com.bottomupquestionphd.demo.repositories;

import com.bottomupquestionphd.demo.domains.dtos.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
}
