package com.tesla.code.service;

import com.tesla.code.beans.RollOut;
import com.tesla.code.beans.RollOutReport;
import com.tesla.code.exceptions.MissingDataException;
import com.tesla.code.exceptions.UniquenessException;
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

    public RollOut createRollOut(RollOut rollOut) throws UniquenessException, MissingDataException {
        if(rollOut.getName() == null) {
            throw new MissingDataException("Required attribute \'name\' is missing");
        }
        rollOut.setDate_created(Instant.now().getEpochSecond());
        RollOut existingRollOut = rollOutRepository.nameExists(rollOut.getName());
        if(existingRollOut != null) {
            throw new UniquenessException("Rollout with name " + rollOut.getName() + " already exists");
        }
        rollOutRepository.save(rollOut);
        return rollOut;
    }

    public void deleteRollOut(String id) {
        rollOutRepository.deleteRollOut(id);
    }

    public RollOutReport generateReport(String id) throws MissingDataException {
        RollOut rollOut = rollOutRepository.findOne(id);
        if(rollOut == null) {
            throw new MissingDataException("RollOut with identifier " + id + " does not exist");
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
