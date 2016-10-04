package com.tesla.code.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

public class IndexErrorControllerTest {

    @InjectMocks
    IndexErrorController indexErrorController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testErrorResponse() throws Exception {
        assertNull(indexErrorController.error());
    }

    @Test
    public void testErrorPath() throws Exception {
        assertEquals(indexErrorController.getErrorPath(), "/error");
    }
}