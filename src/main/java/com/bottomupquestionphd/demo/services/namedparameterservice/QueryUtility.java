package com.bottomupquestionphd.demo.services.namedparameterservice;

import java.util.ArrayList;
import java.util.List;

public class QueryUtility {

  public static List<Query<String>> queryList = new ArrayList<>();

  static {

    queryList.add(() -> "select * from my_world");


  }

  {


  }

  private QueryUtility() {
  }

  ;

  public static String query1 = "select all from hello";

  public static String query2 = "mind1";


}
