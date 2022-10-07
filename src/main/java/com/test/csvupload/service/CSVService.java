package com.test.csvupload.service;

import java.io.IOException;
import java.util.List;

import com.test.csvupload.model.BioDataStats;
import com.test.csvupload.repository.BioDataStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.test.csvupload.helper.CSVHelper;

@Service
@RequiredArgsConstructor
public class CSVService {
  private final BioDataStatsRepository bioDataStatsRepository;

  public void save(MultipartFile file) {
    try {
      List<BioDataStats> bioDataStatsList = CSVHelper.csvToBioData(file.getInputStream());
      bioDataStatsRepository.saveAll(bioDataStatsList);
    } catch (IOException e) {
      throw new RuntimeException("fail to store csv data: " + e.getMessage());
    }
  }

  public List<BioDataStats> getAllBioDataStats() {
    return bioDataStatsRepository.findAll();
  }
}
