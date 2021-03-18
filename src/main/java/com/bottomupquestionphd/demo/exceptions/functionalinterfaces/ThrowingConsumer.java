package com.bottomupquestionphd.demo.exceptions.functionalinterfaces;

import com.bottomupquestionphd.demo.exceptions.MissingParamsException;

import java.util.function.Consumer;

@FunctionalInterface
public interface ThrowingConsumer<T, E extends Exception> {
  void accept(T t) throws E;

  static <T> Consumer<T> throwingConsumerWrapper(
          ThrowingConsumer<T, Exception> throwingConsumer, Class<MissingParamsException> missingParamsExceptionClass) {

    return i -> {
      try {
        throwingConsumer.accept(i);
      } catch (Exception ex) {
        throw new NullPointerException(ex.getMessage());
      }
    };
  }
}
