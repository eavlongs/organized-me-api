package com.organized_me.api.util;

import com.organized_me.api.constants.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHelper {
    public static ResponseEntity<Map<String, Object>> buildSuccessResponse(Map<String, Object> data, String message) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", message);
        responseBody.put("data", data);
        responseBody.put("success", true);

        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<Map<String, Object>> buildSuccessResponse(Map<String, Object> data) {
        return buildSuccessResponse(data, ResponseMessage.REQUEST_SUCCESSFUL);
    }

    public static ResponseEntity<Map<String, Object>> buildSuccessResponse() {
        return buildSuccessResponse(null, ResponseMessage.REQUEST_SUCCESSFUL);
    }

    public static ResponseEntity<Map<String, Object>> buildErrorResponse(Map<String, Object> error, String message, HttpStatus status) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", message);
        responseBody.put("error", error);
        responseBody.put("success", false);

        return ResponseEntity.status(status).body(responseBody);
    }

    public static ResponseEntity<Map<String, Object>> buildNotFoundResponse(Map<String, Object> error) {
        return buildErrorResponse(error, ResponseMessage.NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<Map<String, Object>> buildNotFoundResponse() {
        return buildNotFoundResponse(null);
    }

    public static ResponseEntity<Map<String, Object>> buildInternalServerErrorResponse(Map<String, Object> error) {
        return buildErrorResponse(error, ResponseMessage.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseEntity<Map<String, Object>> buildInternalServerErrorResponse() {
        return buildInternalServerErrorResponse(null);
    }

    public static ResponseEntity<Map<String, Object>> buildBadRequestResponse(Map<String, Object> error) {
        return buildErrorResponse(error, ResponseMessage.BAD_REQUEST, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<Map<String, Object>> buildBadRequestResponse() {
        return buildBadRequestResponse(null);
    }

    public static ResponseEntity<Map<String, Object>> buildUnauthorizedResponse(Map<String, Object> error) {
        return buildErrorResponse(error, ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<Map<String, Object>> buildUnauthorizedResponse() {
        return buildUnauthorizedResponse(null);
    }
}
