package com.tesla.code.service;

import com.tesla.code.beans.RollOut;
import com.tesla.code.repository.RollOutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RollOutService {

    RollOutRepository rollOutRepository;

    @Autowired
    public RollOutService(RollOutRepository repository) {
        this.rollOutRepository = repository;
    }

    public Page<RollOut> listRollOuts(Pageable pageable) {
        return rollOutRepository.findAll(pageable);
    }

    public RollOut createRollOut(RollOut rollOut) {
        rollOutRepository.save(rollOut);
        return rollOut;
    }

    public void deleteRollOut(String id) {
        rollOutRepository.deleteRollOut(id);
    }

    public void generateReport(String id) {
    }
}
