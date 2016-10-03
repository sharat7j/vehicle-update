package com.tesla.code.controller;


import com.tesla.code.beans.RollOut;
import com.tesla.code.beans.RollOutReport;
import com.tesla.code.exceptions.MissingDataException;
import com.tesla.code.exceptions.UniquenessException;
import com.tesla.code.service.RollOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
public class RollOutController {

    private RollOutService rollOutService;

    @Autowired
    public RollOutController(RollOutService rollOutService) {
        this.rollOutService = rollOutService;
    }

    /**
     * Create a new roll out with a unique name
     *
     * @param rollOut The roll out object serialized into a JSON on the post request.
     * @return The RollOut object confirming that its stored in the persistence store.
     * @throws UniquenessException Thrown if the roll out with a similar name already exists
     */
    @RequestMapping(value = "/rollOut", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RollOut createRollOut(@RequestBody RollOut rollOut) throws UniquenessException {
        return rollOutService.createRollOut(rollOut);
    }

    /**
     * Delete a specific roll out based on an identifier
     *
     * @param identifier The unique identifier for a roll out
     */
    @RequestMapping(value = "/rollOut", method = RequestMethod.DELETE)
    public void deleteRollOut(@RequestParam(value = "id") String identifier) {
        // cascaded delete of all jobs that belong to this roll out.
        rollOutService.deleteRollOut(identifier);
    }

    /**
     * List all roll outs in the system
     * @param pageable Pagination object that contains page attributes to filter on listing
     * @return The list of RollOut objects available in the system
     */
    @RequestMapping(value = "/rollOuts", method = RequestMethod.GET)
    public Page<RollOut> listRollOuts(Pageable pageable) {
        // cascaded delete of all jobs that belong to this roll out.
        return rollOutService.listRollOuts(pageable);
    }

    /**
     * Generate a summary report for the roll out
     *
     * @param identifier The identifier for a specific roll out
     * @return The RollOutReport report object containing the summary of the roll out
     * @throws MissingDataException Thrown when the roll out with the specified ID does not exist.
     */
    @RequestMapping(value = "/rollOutReport", method = RequestMethod.GET)
    public RollOutReport reportSummary(@RequestParam(value = "id") String identifier) throws MissingDataException {
        // cascaded delete of all jobs that belong to this roll out.
        return rollOutService.generateReport(identifier);
    }

}
