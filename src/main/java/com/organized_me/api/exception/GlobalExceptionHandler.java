package com.organized_me.api.exception;

import com.organized_me.api.util.ResponseHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, Object>>
    handleMaxSizeException(MaxUploadSizeExceededException exc, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("File size exceeds the maximum limit!");
        Map<String, Object> error = new HashMap<>();
        error.put("error", "File size exceeds the maximum limit!");
        return ResponseHelper.buildErrorResponse(error, (String)error.get("error"), HttpStatus.PAYLOAD_TOO_LARGE);
    }
}