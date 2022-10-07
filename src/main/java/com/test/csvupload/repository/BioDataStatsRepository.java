package com.test.csvupload.repository;

import com.test.csvupload.model.BioDataStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BioDataStatsRepository extends JpaRepository<BioDataStats, Long> {
}
