package com.example.demo.exception;

import com.example.demo.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j  // ✅ Thêm annotation để có thể log
public class GlobalExceptionHandler {

    // ===== XỬ LÝ RESOURCE NOT FOUND (404) =====
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        // Ghi log WARN (không cần stack trace)
        log.warn("Resource not found: {}", ex.getMessage());

        ApiResponse<Object> response = ApiResponse.notFound(
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // ===== XỬ LÝ DOCTOR NOT FOUND (404) =====
    @ExceptionHandler(DoctorNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleDoctorNotFound(
            DoctorNotFoundException ex,
            HttpServletRequest request) {

        log.warn("Doctor not found: {}", ex.getMessage());

        ApiResponse<Object> response = ApiResponse.notFound(
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // ===== XỬ LÝ BOOK ALREADY RETURNED (400) =====
    @ExceptionHandler(BookAlreadyReturnedException.class)
    public ResponseEntity<ApiResponse<Object>> handleBookAlreadyReturned(
            BookAlreadyReturnedException ex,
            HttpServletRequest request) {

        log.warn("Book already returned: {}", ex.getMessage());

        ApiResponse<Object> response = ApiResponse.badRequest(
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // ===== XỬ LÝ VALIDATION ERROR (400) =====
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        log.warn("Validation failed: {}", errors);

        ApiResponse<Map<String, String>> response = ApiResponse.badRequest(
                "Validation failed: " + errors.toString(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // ===== XỬ LÝ ILLEGAL ARGUMENT (400) =====
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        log.warn("Illegal argument: {}", ex.getMessage());

        ApiResponse<Object> response = ApiResponse.badRequest(
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // ===== XỬ LÝ FILE QUÁ LỚN (400) =====
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Object>> handleMaxUploadSizeExceeded(
            MaxUploadSizeExceededException ex,
            HttpServletRequest request) {

        log.warn("File size exceeds limit: {}", ex.getMessage());

        ApiResponse<Object> response = ApiResponse.badRequest(
                "File size exceeds maximum limit (10MB)",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // ============================================================
    // ⭐ QUAN TRỌNG: XỬ LÝ LỖI CHUNG (500) - CÓ LOG STACK TRACE
    // ============================================================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        // ✅ GHI LOG ĐẦY ĐỦ STACK TRACE
        // - Dùng log.error với tham số exception để in toàn bộ stack trace
        // - Phân biệt rõ ràng để dễ tìm kiếm trong log
        log.error("========================================");
        log.error("🚨 SYSTEM ERROR OCCURRED 🚨");
        log.error("========================================");
        log.error("Request URI: {}", request.getRequestURI());
        log.error("Request Method: {}", request.getMethod());
        log.error("Client IP: {}", request.getRemoteAddr());
        log.error("Error Message: {}", ex.getMessage());
        log.error("Error Type: {}", ex.getClass().getName());
        log.error("========================================");
        log.error("FULL STACK TRACE:", ex);  // 👈 In toàn bộ stack trace
        log.error("========================================");

        // ✅ TRẢ VỀ CLIENT THÔNG BÁO AN TOÀN (KHÔNG LỘ STACK TRACE)
        ApiResponse<Object> response = ApiResponse.internalError(
                "An unexpected error occurred. Please contact support.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}