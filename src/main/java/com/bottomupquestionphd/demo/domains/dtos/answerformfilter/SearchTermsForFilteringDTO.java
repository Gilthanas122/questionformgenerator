package com.bottomupquestionphd.demo.domains.dtos.answerformfilter;

import java.util.ArrayList;
import java.util.List;

public class SearchTermsForFilteringDTO {
  private List<String> searchTerms = new ArrayList<>();

  public SearchTermsForFilteringDTO() {
  }

  public SearchTermsForFilteringDTO(List<String> searchTerms) {
    this.searchTerms = searchTerms;
  }

  public List<String> getSearchTerms() {
    return searchTerms;
  }

  public void setSearchTerms(List<String> searchTerms) {
    this.searchTerms = searchTerms;
  }
}
