package com.tesla.code.controller;

import com.tesla.code.beans.RollOut;
import com.tesla.code.beans.RollOutReport;
import com.tesla.code.exceptions.MissingDataException;
import com.tesla.code.exceptions.UniquenessException;
import com.tesla.code.service.RollOutService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static com.tesla.code.util.Helpers.churnRollOut;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class RollOutControllerTest {

    @Mock
    RollOutService rollOutService;

    @InjectMocks
    RollOutController rollOutController = new RollOutController(rollOutService);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = MissingDataException.class)
    public void testCreateRollOutException() throws Exception, MissingDataException {
        when(rollOutService.createRollOut(any(RollOut.class))).thenThrow(new MissingDataException("name attribute is missing"));
        rollOutController.createRollOut(new RollOut());
    }

    @Test(expected = UniquenessException.class)
    public void testCreateRollOutExistingConflict() throws Exception, MissingDataException {
        when(rollOutService.createRollOut(any(RollOut.class))).thenThrow(new UniquenessException("rollout with same name already exists"));
        rollOutController.createRollOut(new RollOut());

    }

    @Test
    public void testDeleteRollOut() throws Exception {
        doNothing().when(rollOutService).deleteRollOut(any(String.class));
        rollOutController.deleteRollOut("1");
    }

    @Test
    public void testRollOutList() throws Exception {
        List<RollOut> rollOutList = new ArrayList<>();
        rollOutList.add(churnRollOut());
        rollOutList.add(churnRollOut());
        rollOutList.add(churnRollOut());
        PageImpl<RollOut> rollOutPage = new PageImpl<>(rollOutList);

        when(rollOutService.listRollOuts(any(Pageable.class))).thenReturn(rollOutPage);
        assertNotNull(rollOutController.listRollOuts(null));
        assertEquals(rollOutController.listRollOuts(null), rollOutPage);

    }

    @Test(expected = MissingDataException.class)
    public void testRollOutReportException() throws Exception, MissingDataException {
        when(rollOutService.generateReport(any(String.class))).thenThrow(new MissingDataException("could not find roll out with id"));
        rollOutController.reportSummary("1");
    }

    @Test
    public void testRollOutReportValidInput() throws Exception, MissingDataException {
        RollOutReport report = new RollOutReport();
        when(rollOutService.generateReport(any(String.class))).thenReturn(report);
        assertEquals(rollOutController.reportSummary("1"), report);

    }
}