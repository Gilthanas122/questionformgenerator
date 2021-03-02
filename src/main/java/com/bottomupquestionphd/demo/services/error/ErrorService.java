package com.bottomupquestionphd.demo.services.error;

import com.bottomupquestionphd.demo.domains.dtos.ErrorMessageDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ErrorService {

  List<String> checkInputNullFields(Object object);

  String buildMissingFieldErrorMessage(Object object);
}
