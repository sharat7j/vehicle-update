package com.tesla.code.controller;


import com.tesla.code.beans.RollOut;
import com.tesla.code.service.RollOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/v1")
public class RollOutController {
    private final Logger logger = Logger.getLogger(String.valueOf(RollOutController.class));

    RollOutService rollOutService;

    @Autowired
    public RollOutController(RollOutService rollOutService) {
        this.rollOutService = rollOutService;
    }

    @RequestMapping(value = "/rollOut", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RollOut createRollOut(@RequestBody RollOut rollOut) {
        return rollOutService.createRollOut(rollOut);
    }

    @RequestMapping(value = "/rollOut", method = RequestMethod.DELETE)
    public void deleteRollOut(@RequestParam(value = "id") String identifier) {
        // cascaded delete of all jobs that belong to this roll out.
        rollOutService.deleteRollOut(identifier);
    }

    @RequestMapping(value = "/rollOuts", method = RequestMethod.GET)
    public Page<RollOut> listRollOuts(Pageable pageable) {
        // cascaded delete of all jobs that belong to this roll out.
        return rollOutService.listRollOuts(pageable);
    }

    @RequestMapping(value = "/rollOutReport", method = RequestMethod.DELETE)
    public ResponseStatus reportSummary(@RequestParam(value = "id") String identifier) {
        // cascaded delete of all jobs that belong to this roll out.
        return null;
    }

}
