/**
 * The exception package is responsible for providing a consistent exception handling framework for the entire service.
 *
 * The classes all extend the base Exception class but with Spring's ControllerAdvice directive the exceptions are applied
 * globally so any part of the service that's throwing these exceptions would be caught and the corresponding HTTP status
 * code and response message would be rendered.
 */
package com.tesla.code.exceptions;