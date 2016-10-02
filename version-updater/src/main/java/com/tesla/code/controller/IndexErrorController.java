package com.tesla.code.controller;


import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexErrorController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String error() {
        // this ensures that our API never returns any unnecessary html template down in the response.
        // The API response should be dictated purely bit its error codes
        return null;
    }

    public String getErrorPath() {
        return PATH;
    }
}

