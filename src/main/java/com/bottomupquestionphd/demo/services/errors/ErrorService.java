package com.bottomupquestionphd.demo.services.errors;

import com.bottomupquestionphd.demo.domains.dtos.ErrorMessageDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ErrorService {

  ErrorMessageDTO defaultExceptionResponse(Exception exception);

  List<String> checkInputNullFields(Object object);

  String buildMissingFieldErrorMessage(Object object);
}
