package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * API Response Wrapper - Định dạng chuẩn cho tất cả API responses
 * @param <T> Kiểu dữ liệu của data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private String status;          // "success" hoặc "error"
    private int code;               // HTTP status code (200, 404, 500...)
    private String message;         // Thông báo chi tiết
    private T data;                 // Dữ liệu trả về (có thể là Object, List, Page...)
    private PageMeta meta;          // Thông tin phân trang (nếu có)
    private String timestamp;       // Thời gian response
    private String path;            // Đường dẫn API (nếu có lỗi)

    // ===== SUCCESS RESPONSE =====

    // Success - Không có dữ liệu
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .status("success")
                .code(200)
                .message(message)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    // Success - Có dữ liệu
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .status("success")
                .code(200)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    // Success - Có dữ liệu và phân trang
    public static <T> ApiResponse<T> success(String message, T data, PageMeta meta) {
        return ApiResponse.<T>builder()
                .status("success")
                .code(200)
                .message(message)
                .data(data)
                .meta(meta)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    // Success - Created (201)
    public static <T> ApiResponse<T> created(String message, T data) {
        return ApiResponse.<T>builder()
                .status("success")
                .code(201)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

    // ===== ERROR RESPONSE =====

    // Error - Bad Request (400)
    public static <T> ApiResponse<T> badRequest(String message, String path) {
        return ApiResponse.<T>builder()
                .status("error")
                .code(400)
                .message(message)
                .timestamp(LocalDateTime.now().toString())
                .path(path)
                .build();
    }

    // Error - Not Found (404)
    public static <T> ApiResponse<T> notFound(String message, String path) {
        return ApiResponse.<T>builder()
                .status("error")
                .code(404)
                .message(message)
                .timestamp(LocalDateTime.now().toString())
                .path(path)
                .build();
    }

    // Error - Internal Server Error (500)
    public static <T> ApiResponse<T> internalError(String message, String path) {
        return ApiResponse.<T>builder()
                .status("error")
                .code(500)
                .message(message)
                .timestamp(LocalDateTime.now().toString())
                .path(path)
                .build();
    }

    // Error - Tùy chỉnh
    public static <T> ApiResponse<T> error(int code, String message, String path) {
        return ApiResponse.<T>builder()
                .status("error")
                .code(code)
                .message(message)
                .timestamp(LocalDateTime.now().toString())
                .path(path)
                .build();
    }
}