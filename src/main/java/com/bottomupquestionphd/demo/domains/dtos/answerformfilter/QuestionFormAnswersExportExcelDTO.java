package com.bottomupquestionphd.demo.domains.dtos.answerformfilter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QuestionFormAnswersExportExcelDTO {
  private XSSFWorkbook workbook;
  private XSSFSheet sheet;
  private List<String> questionTexts = new ArrayList<>();
  private List<List<String>> answerTexts = new ArrayList<>();

  public QuestionFormAnswersExportExcelDTO() {
  }

  public QuestionFormAnswersExportExcelDTO(List<String> questionTexts, List<List<String>> answerTexts) {
    this.questionTexts = questionTexts;
    this.answerTexts = answerTexts;
    this.workbook = new XSSFWorkbook();
    this.sheet = workbook.createSheet("Answer Forms Answers");
  }

  private void writeHeaderRow(){
    int rownum = 0;
    int cellnum = 0;
    ///create first row for questions texts
    Row row = sheet.createRow(rownum);
    for (int i = 0; i < questionTexts.size(); i++) {
      String temp = questionTexts.get(i);
      Cell cell = row.createCell(cellnum++);
      cell.setCellValue(temp);
    }
  }

  private void writeDataRows(){
    int rownum = 1;
    int cellnum = 0;
    Iterator<List<String>> i = answerTexts.iterator();
    while (i.hasNext()) {
      List<String> templist = (List<String>) i.next();
      Iterator<String> tempIterator = templist.iterator();
      Row row = sheet.createRow(rownum++);
      cellnum = 0;
      while (tempIterator.hasNext()) {
        String temp = (String) tempIterator.next();
        Cell cell = row.createCell(cellnum++);
        cell.setCellValue(temp);
      }
    }
  }

  public void export(HttpServletResponse httpServletResponse) throws IOException {
    writeHeaderRow();
    writeDataRows();
    ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
    workbook.write(servletOutputStream);
    servletOutputStream.close();
    workbook.close();
  }

  public XSSFWorkbook getWorkbook() {
    return workbook;
  }

  public void setWorkbook(XSSFWorkbook workbook) {
    this.workbook = workbook;
  }

  public XSSFSheet getSheet() {
    return sheet;
  }

  public void setSheet(XSSFSheet sheet) {
    this.sheet = sheet;
  }

  public List<String> getQuestionTexts() {
    return questionTexts;
  }

  public void setQuestionTexts(List<String> questionTexts) {
    this.questionTexts = questionTexts;
  }

  public List<List<String>> getAnswerTexts() {
    return answerTexts;
  }

  public void setAnswerTexts(List<List<String>> answerTexts) {
    this.answerTexts = answerTexts;
  }
}
