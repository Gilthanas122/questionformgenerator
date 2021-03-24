package com.bottomupquestionphd.demo.repositories;

import com.bottomupquestionphd.demo.domains.daos.tokens.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, String> {

  ConfirmationToken findByConfirmationToken(String confirmationToken);
}
