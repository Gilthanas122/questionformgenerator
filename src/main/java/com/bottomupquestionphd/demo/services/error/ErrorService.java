package com.bottomupquestionphd.demo.services.error;

import org.springframework.stereotype.Service;

@Service
public interface ErrorService {

  String buildMissingFieldErrorMessage(Object object);
}
