package com.tesla.code.controller;


import com.tesla.code.beans.RollOut;
import com.tesla.code.beans.RollOutReport;
import com.tesla.code.beans.request.RollOutRequest;
import com.tesla.code.exceptions.MissingDataException;
import com.tesla.code.exceptions.UniquenessException;
import com.tesla.code.service.RollOutService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "RollOut Controller", tags = "RollOut Controller", description = "Interface definitions that support all " +
        "operations required to manage a roll out")
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
     * @param rollOutRequest The roll out request object serialized into a JSON on the post request.
     * @return The RollOut object confirming that its stored in the persistence store.
     * @throws UniquenessException  Thrown if the roll out with a similar name already exists
     * @throws MissingDataException Thrown if required attributes are missing from the JSON payload
     */
    @ApiOperation(value = "Create a new RollOut", notes = "Create a new roll out that is unique by name")
    @RequestMapping(value = "/rollOut", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = RollOut.class),
            @ApiResponse(code = 400, message = "Bad request if either a roll out already exists with the same name or " +
                    "if the name attribute is empty", response = String.class)
    })
    public RollOut createRollOut(@ApiParam(value = "RollOutRequest payload as JSON for creation of a roll out")
                                 @RequestBody RollOutRequest rollOutRequest) throws UniquenessException, MissingDataException {
        return rollOutService.createRollOut(RollOut.getRollOutFromRequest(rollOutRequest));
    }

    /**
     * Delete a specific roll out based on an identifier
     *
     * @param identifier The unique identifier for a roll out
     */
    @ApiOperation(value = "Delete an existing RollOut", notes = "Remove an exisitng RollOut and all its associated Job and Statuses")
    @RequestMapping(value = "/rollOut", method = RequestMethod.DELETE)
    @ApiResponse(code = 200, message = "Success")
    public void deleteRollOut(@RequestParam(value = "id") String identifier) {
        // cascaded delete of all jobs that belong to this roll out.
        rollOutService.deleteRollOut(identifier);
    }

    /**
     * List all roll outs in the system
     *
     * @param pageable Pagination object that contains page attributes to filter on listing
     * @return The list of RollOut objects available in the system
     */
    @ApiOperation(value = "list all RollOuts in the system", notes = "Paginated list of all roll outs in the system",
            response = RollOut.class, responseContainer = "List")
    @RequestMapping(value = "/rollOuts", method = RequestMethod.GET)
    @ApiResponse(code = 200, message = "Paginated list of RollOut")
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
    @ApiOperation(value = "Generate RollOut Report", notes = "Provide current report of a roll out")
    @RequestMapping(value = "/rollOutReport", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Snapshot of current state of the Roll Out", response = RollOutReport.class),
            @ApiResponse(code = 400, message = "Bad request if a roll out does not exist for the identifier provided",
                    response = String.class)
    })
    public RollOutReport reportSummary(@RequestParam(value = "id") String identifier) throws MissingDataException {
        // cascaded delete of all jobs that belong to this roll out.
        return rollOutService.generateReport(identifier);
    }

}
