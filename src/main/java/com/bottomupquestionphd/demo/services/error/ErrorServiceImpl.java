package com.bottomupquestionphd.demo.services.error;

import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
public class ErrorServiceImpl  {

  public static String buildMissingFieldErrorMessage(Object object) throws MissingParamsException {
    if (object == null){
      throw new NullPointerException("Object to be verified for null or empty fields is null");
    }
    String result = "";
    List<String> missingFields = checkInputNullFields(object);
    String errorMessage = "";
    for (String word : missingFields) {
      result += word + ", ";
    }
    if (result.length() != 0){
      errorMessage = result.substring(0, 1).toUpperCase() + result.substring(1, result.length() - 2).concat(" ") + "is required.";
      throw new MissingParamsException(errorMessage);
    }

    return errorMessage;
  }

  private static List<String> checkInputNullFields(Object object) {
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
