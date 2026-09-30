package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.ReaderCreateDTO;
import com.example.demo.dto.ReaderResponseDTO;
import com.example.demo.service.ReaderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/readers")
@RequiredArgsConstructor
@Slf4j
public class ReaderController {

    private final ReaderService readerService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<ReaderResponseDTO>> createReader(
            @Valid @ModelAttribute ReaderCreateDTO dto,
            BindingResult bindingResult) {

        // 1. Kiểm tra validation
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }

            ApiResponse<ReaderResponseDTO> response = ApiResponse.badRequest(
                    "Validation failed: " + errors.toString(),
                    "/api/readers"
            );
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // 2. Tạo mới reader
            ReaderResponseDTO createdReader = readerService.createReader(dto);

            // 3. Trả về thành công
            ApiResponse<ReaderResponseDTO> response = ApiResponse.created(
                    "Reader created successfully",
                    createdReader
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            // 4. Lỗi validation (email trùng, file không hợp lệ)
            ApiResponse<ReaderResponseDTO> response = ApiResponse.badRequest(
                    e.getMessage(),
                    "/api/readers"
            );
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            // 5. Lỗi server
            log.error("Error creating reader: {}", e.getMessage());
            ApiResponse<ReaderResponseDTO> response = ApiResponse.internalError(
                    "Failed to create reader: " + e.getMessage(),
                    "/api/readers"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}