package com.bottomupquestionphd.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextAnswerVoteRepository extends JpaRepository<TextAnswerVoteRepository, Long> {
}
