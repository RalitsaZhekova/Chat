package com.fmi.chat_app.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;

public class AppResponse {

    private static HashMap<String, Object> response;

    public static AppResponse success() {
        response = new HashMap<>();
        response.put("status", "success");
        response.put("code", HttpStatus.OK.value());
        return new AppResponse();
    }

    public static AppResponse error() {
        response = new HashMap<>();
        response.put("status", "error");
        response.put("code", HttpStatus.BAD_REQUEST.value());
        return new AppResponse();
    }

    // method for status code
    public AppResponse withCode(HttpStatus code) {
        response.put("code", code.value());
        return this;
    }

    //method for messages
    public AppResponse withMessage(String message) {
        response.put("message", message);
        return this;
    }

    //method for data
    public AppResponse withData(Object data) {
        response.put("data", data);
        return this;
    }

    public AppResponse withDataAsArray(Object data) {
        ArrayList<Object> list = new ArrayList<>();
        list.add(data);

        return withData(list);
    }

    public ResponseEntity<Object> build() {
        int code = (Integer) response.get("code");
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(code));
    }

}
