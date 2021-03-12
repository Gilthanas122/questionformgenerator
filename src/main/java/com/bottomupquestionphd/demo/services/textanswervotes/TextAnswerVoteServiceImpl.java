package com.bottomupquestionphd.demo.services.textanswervotes;

import com.bottomupquestionphd.demo.repositories.TextAnswerVoteRepository;
import org.springframework.stereotype.Service;

@Service
public class TextAnswerVoteServiceImpl implements TextAnswerVoteService {
    private final TextAnswerVoteRepository textAnswerVoteRepository;

    public TextAnswerVoteServiceImpl(TextAnswerVoteRepository textAnswerVoteRepository) {
        this.textAnswerVoteRepository = textAnswerVoteRepository;
    }
}
