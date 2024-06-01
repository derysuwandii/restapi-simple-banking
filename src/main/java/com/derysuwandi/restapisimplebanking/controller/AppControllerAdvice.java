package com.derysuwandi.restapisimplebanking.controller;

import com.derysuwandi.restapisimplebanking.exception.ErrorLevelException;
import com.derysuwandi.restapisimplebanking.exception.InfoLevelException;
import com.derysuwandi.restapisimplebanking.exception.JsonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class AppControllerAdvice {
    @ExceptionHandler({InfoLevelException.class})
    @ResponseBody
    public ResponseEntity<?> infoLevelException(InfoLevelException exception, HttpServletResponse res, HttpServletRequest req) {
        return ResponseEntity.ok(exception.generateJsonResponse());
    }

    @ExceptionHandler({ErrorLevelException.class})
    @ResponseBody
    public ResponseEntity<?> errorLevelException(ErrorLevelException exception, HttpServletResponse res, HttpServletRequest req) {
        log.error(exception.getMessage());
        return ResponseEntity.status(500).body(exception.generateJsonResponse());
    }

    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ResponseEntity<?> systemErrorException(Exception exception, HttpServletResponse res, HttpServletRequest req) {
        log.error(exception.getMessage());
        return ResponseEntity.status(500)
                .body(JsonResponse.error("System Error", "5000"));
    }
}
