package com.bottomupquestionphd.demo.services.namedparameterservice;

@FunctionalInterface
public interface Query<T> {

  T getQuery();
}
