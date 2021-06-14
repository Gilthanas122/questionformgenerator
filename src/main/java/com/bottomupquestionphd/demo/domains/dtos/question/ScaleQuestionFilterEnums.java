package com.bottomupquestionphd.demo.domains.dtos.question;

import java.util.HashMap;
import java.util.Map;

public enum ScaleQuestionFilterEnums {
  LARGEREQUALS(">="),
  SMALLEREQUALS("<="),
  LARGER(">"),
  SMALLER("<"),
  DOESNOTEQUAL("!=");

  public final String label;

  ScaleQuestionFilterEnums(String label) {
    this.label = label;
  }

  private static final Map<String, ScaleQuestionFilterEnums> BY_SCALEFILTER = new HashMap<>();

  static {
    for (ScaleQuestionFilterEnums e: values()) {
      BY_SCALEFILTER.put(e.label, e);
    }
  }

  public static String valueOfLabel(String label) {
    return BY_SCALEFILTER.get(label).label;
  }

  public static String checkIfSearchTermContainsOperator(String searchTerm){
    for (ScaleQuestionFilterEnums e: values()){
      if (searchTerm.contains(e.label)){
        return e.label;
      }
    }
    return "=";
  }
}
