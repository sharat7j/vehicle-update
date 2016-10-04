package com.tesla.code.exceptions;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class BaseExceptionTest {

    @Test
    public void testGetMessage() throws Exception {
        BaseException exception = new BaseException("sample test");
        assertEquals(exception.getMessage(), "sample test");
    }

    @Test
    public void testSetMessage() throws Exception {
        BaseException exception = new BaseException(null);
        exception.setMessage("set now");
        assertEquals(exception.getMessage(), "set now");
    }
}