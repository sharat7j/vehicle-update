package com.tesla.code.service;

import com.tesla.code.beans.RollOut;
import com.tesla.code.beans.RollOutReport;
import com.tesla.code.repository.RollOutRepository;
import com.tesla.code.utils.JobState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class RollOutService {

    private RollOutRepository rollOutRepository;

    @Autowired
    public RollOutService(RollOutRepository repository) {
        this.rollOutRepository = repository;
    }

    public Page<RollOut> listRollOuts(Pageable pageable) {
        return rollOutRepository.findAll(pageable);
    }

    public RollOut createRollOut(RollOut rollOut) {
        rollOut.setDate_created(Instant.now().getEpochSecond());
        rollOutRepository.save(rollOut);
        return rollOut;
    }

    public void deleteRollOut(String id) {
        rollOutRepository.deleteRollOut(id);
    }

    public RollOutReport generateReport(String id) {
        RollOut rollOut = rollOutRepository.findOne(id);
        if(rollOut == null) {
            return null;
        }
        RollOutReport report = new RollOutReport();
        report.setTotalJobCount(rollOutRepository.getJobCount(id));
        report.setTotalJobStatusCount(rollOutRepository.getJobStatusCount(id));
        Map<JobState, Integer> jobStateCountMap = new HashMap<>();
        for(JobState state: JobState.values()) {
            // get count for each state in the roll up
            jobStateCountMap.put(state, rollOutRepository.getStateCount(state, id));
        }
        report.setCurrentState(jobStateCountMap);
        return report;
    }
}
