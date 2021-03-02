package com.bottomupquestionphd.demo.services.error;

import com.bottomupquestionphd.demo.domains.dtos.ErrorMessageDTO;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
public class ErrorServiceImpl implements ErrorService {

  @Override
  public String buildMissingFieldErrorMessage(Object object) {
    String result = "";
    List<String> missingFields = checkInputNullFields(object);
    String errorMessage = "";
    for (String word : missingFields) {
      result += word + ", ";
    }
    if (result.length() == 0){
      return null;
    }
    errorMessage = result.substring(0, 1).toUpperCase() + result.substring(1, result.length() - 2).concat(" ") + "is required.";
    return errorMessage;
  }


  @Override
  public List<String> checkInputNullFields(Object object) {
    List<String> missingFields = new ArrayList<>();
    for (Field field : object.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      try {
        if (field.get(object) == null || field.get(object).equals("")) {
          field.setAccessible(true);
          missingFields.add(field.getName());
        }
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return missingFields;
  }
}
