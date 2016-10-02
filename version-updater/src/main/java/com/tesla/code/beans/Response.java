package com.tesla.code.beans;

public class Response {

    private enum ResponseStatus {
        OK("ok"),
        FAIL("fail");

        private String value;

        ResponseStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    private ResponseStatus status;
    private Object result;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }
}
