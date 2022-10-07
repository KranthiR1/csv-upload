package com.test.csvupload.controller;

import java.util.List;

import com.test.csvupload.helper.CSVHelper;
import com.test.csvupload.message.ResponseMessage;
import com.test.csvupload.model.BioDataStats;
import com.test.csvupload.service.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin("http://localhost:8081")
@Controller
@RequestMapping("/api/csv")
public class CSVController {

  @Autowired
  CSVService fileService;

  @PostMapping("/upload")
  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
    String message = "Please upload a valid csv file!";
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    if (CSVHelper.hasCSVFormat(file)) {
      try {
        fileService.save(file);

        message = "Uploaded the file successfully: " + file.getOriginalFilename();
        httpStatus = HttpStatus.OK;
      } catch (Exception e) {
        message = "Could not upload the file: " + file.getOriginalFilename() + "!";
        httpStatus = HttpStatus.EXPECTATION_FAILED;
      }
    }

    ResponseMessage responseMessage = new ResponseMessage(message);
    return ResponseEntity.status(httpStatus).body(responseMessage);
  }

  @GetMapping("/biodata")
  public ResponseEntity<List<BioDataStats>> getAllBioData() {
    try {
      List<BioDataStats> bioDataStatsList = fileService.getAllBioDataStats();

      if (bioDataStatsList.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(bioDataStatsList, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
