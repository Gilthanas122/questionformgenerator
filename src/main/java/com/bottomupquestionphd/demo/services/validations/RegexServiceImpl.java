package com.bottomupquestionphd.demo.services.validations;

import com.bottomupquestionphd.demo.exceptions.appuser.InvalidRegexParameterException;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RegexServiceImpl {

  private static String passwordErrorMessage = "Password must be at least 8 and max 20 characters long, at least one uppercase letter and one number and special character of the following ones: @#$%^&+-=";
  private static String emailErrorMessage = "Not valid email format";
  private static String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=-])(?=\\S+$).{8,}$";
  private static String emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

  public static void checkRegex(String toValidate, String type) throws InvalidRegexParameterException {
    String errorMessage = "";
    String regExpn = "";
    if (type.equals("password")) {
      errorMessage =passwordErrorMessage;
      regExpn = passwordRegex;
    } else if (type.equals("email")) {
      regExpn = emailRegex;
      errorMessage = emailErrorMessage;
    } else {
      throw new InvalidParameterException("Validation can not happen with given type");
    }
    CharSequence chr = toValidate;
    Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(chr);
    if (!matcher.matches()) {
      throw new InvalidRegexParameterException(errorMessage);
    }
  }
}
