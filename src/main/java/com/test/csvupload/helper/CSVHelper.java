package com.test.csvupload.helper;

import com.test.csvupload.model.BioDataStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class CSVHelper {
  public static String TYPE = "text/csv";
  static String[] HEADERS = { "Id", "Name", "Sex", "Age" , "Height", "Weight"};

  public static boolean hasCSVFormat(MultipartFile file) {

    if (!TYPE.equals(file.getContentType())) {
      return false;
    }

    return true;
  }

  public static List<BioDataStats> csvToBioData(InputStream is) {
    try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
         CSVParser csvParser = new CSVParser(fileReader,
                 CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

      List<BioDataStats> bioDataList = new ArrayList<BioDataStats>();

      Iterable<CSVRecord> csvRecords = csvParser.getRecords();

      for (CSVRecord csvRecord : csvRecords) {
        BioDataStats bioDataStats = new BioDataStats();
        bioDataStats.setId(parseLong(csvRecord.get("Id")));
        bioDataStats.setName(csvRecord.get("Name"));
        bioDataStats.setSex(csvRecord.get("Sex"));
        bioDataStats.setAge(parseInt(csvRecord.get("Age")));
        bioDataStats.setHeight(parseInt(csvRecord.get("Height")));
        bioDataStats.setWeight(parseInt(csvRecord.get("Weight")));
        bioDataList.add(bioDataStats);
      }

      return bioDataList;
    } catch (IOException e) {
      throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
    }
  }
}
