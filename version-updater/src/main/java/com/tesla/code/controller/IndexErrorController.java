package com.tesla.code.controller;


import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Need this controller to ensure the default spring boot error page is not rendered. That gives away sensitive information
 * about the server stack.
 */
@RestController
public class IndexErrorController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String error() {
        // this ensures that our API never returns any unnecessary html template down in the response.
        return null;
    }

    public String getErrorPath() {
        return PATH;
    }
}

