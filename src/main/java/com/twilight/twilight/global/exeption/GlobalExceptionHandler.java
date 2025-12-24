package com.twilight.twilight.global.exeption;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

//예외가 다른 핸들러에서 안잡혔을때, 마지막 안전장치
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Object handleGlobalException(Exception e, Model model, HttpServletRequest request ) {
       String accept = request.getHeader("Accept");
        boolean wantsJson = accept != null && accept.contains("application/json")
                || request.getRequestURI().startsWith("/api/");

        log.info("에러메세지:{}" , e.getMessage());

        if (wantsJson) {
            return handleApiError(e, request);
        }

        return handleSsrError(e, model, request);
    }

    private ResponseEntity<ProblemDetail> handleApiError(Exception e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        if (e instanceof NoSuchElementException) status = HttpStatus.NOT_FOUND;
        else if (e instanceof IllegalArgumentException) status = HttpStatus.BAD_REQUEST;

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(status.getReasonPhrase());
        problem.setDetail(e.getMessage());
        problem.setType(URI.create("https://api.twilight.com/problems/" + status.value()));
        problem.setProperty("instance", request.getRequestURI());
        problem.setProperty("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.status(status).body(problem);
    }

    private String handleSsrError(Exception e, Model model, HttpServletRequest request) {
        String errorMessage = e.getMessage();
        model.addAttribute("errorMessage", errorMessage);
        return "error-custom";
    }


}
