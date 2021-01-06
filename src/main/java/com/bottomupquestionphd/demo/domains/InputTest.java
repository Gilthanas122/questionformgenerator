package com.bottomupquestionphd.demo.domains;

import java.util.ArrayList;
import java.util.List;

public class InputTest {
  private long id;
  private List<String> names = new ArrayList<>();

  public InputTest(){}

  public InputTest(long id, List<String> names) {
    this.id = id;
    this.names = names;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public List<String> getNames() {
    return names;
  }

  public void setNames(List<String> names) {
    this.names = names;
  }
}
