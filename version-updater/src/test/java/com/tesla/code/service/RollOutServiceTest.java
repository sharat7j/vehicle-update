package com.tesla.code.service;

import com.tesla.code.beans.RollOut;
import com.tesla.code.beans.RollOutReport;
import com.tesla.code.exceptions.MissingDataException;
import com.tesla.code.exceptions.UniquenessException;
import com.tesla.code.repository.JobRepository;
import com.tesla.code.repository.RollOutRepository;
import com.tesla.code.utils.JobState;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jmx.export.annotation.ManagedOperation;

import java.util.Collections;
import java.util.Map;

import static com.tesla.code.util.Helpers.churnRollOut;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class RollOutServiceTest {

    @Mock
    RollOutRepository rollOutRepository;

    @Mock
    JobRepository jobRepository;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @InjectMocks
    RollOutService rollOutService = new RollOutService(rollOutRepository, jobRepository);

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testListRollOuts() throws Exception {
        when(rollOutRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));
        assertEquals(rollOutService.listRollOuts(null).getTotalElements(), 0);
    }

    @Test(expected = MissingDataException.class)
    public void testCreateRollOutMissingException() throws Exception, MissingDataException {
        rollOutService.createRollOut(null);
    }

    @Test(expected = UniquenessException.class)
    public void testCreateRollOutExistingException() throws Exception, MissingDataException {
        RollOut rollOut = churnRollOut();
        when(rollOutRepository.nameExists(any(String.class))).thenReturn(churnRollOut());
        rollOutService.createRollOut(rollOut);
    }

    @Test
    public void testCreateRollOut() throws Exception, MissingDataException {
        RollOut rollOut = churnRollOut();
        when(rollOutRepository.nameExists(any(String.class))).thenReturn(null);
        when(rollOutRepository.save(any(RollOut.class))).thenReturn(rollOut);
        RollOut result = rollOutService.createRollOut(rollOut);
        assertEquals(result, rollOut);
    }

    @Test
    public void testDeleteRollOuts() throws Exception {
        doNothing().when(rollOutRepository).deleteRollOut(any(String.class));
        rollOutService.deleteRollOut("1");
    }

    @Test(expected = MissingDataException.class)
    public void testGenerateReportException() throws Exception, MissingDataException {
        when(rollOutRepository.findOne(any(String.class))).thenReturn(null);
        rollOutService.generateReport("1");
    }

    @Test
    public void testGenerateReport() throws Exception, MissingDataException {
        when(rollOutRepository.findOne(any(String.class))).thenReturn(churnRollOut());
        when(rollOutRepository.getJobCount(any(String.class))).thenReturn(2L);
        when(rollOutRepository.getStateCount(any(JobState.class), any(String.class))).thenReturn(1);
        RollOutReport report = rollOutService.generateReport("1");
        assertNotNull(report);
        assertTrue(report.getTotalJobCount() == 2L);
        for(Map.Entry<JobState, Integer> entry: report.getCurrentState().entrySet()) {
            assertTrue(entry.getValue() == 1);
        }
    }
}