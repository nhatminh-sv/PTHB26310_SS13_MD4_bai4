package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.DoctorUpdateDTO;
import com.example.demo.dto.PageMeta;
import com.example.demo.entity.Doctor;
import com.example.demo.exception.DoctorNotFoundException;
import com.example.demo.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
@Slf4j
public class DoctorController {

    private final DoctorService doctorService;

    // ===== API LẤY DANH SÁCH BÁC SĨ (CÓ PHÂN TRANG) =====
    @GetMapping
    public ResponseEntity<ApiResponse<Page<Doctor>>> getAllDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        // 1. Tạo Pageable
        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        // 2. Lấy dữ liệu từ Service
        Page<Doctor> doctorPage = doctorService.getAllDoctors(pageable);

        // 3. Tạo metadata phân trang
        PageMeta meta = PageMeta.builder()
                .page(doctorPage.getNumber())
                .size(doctorPage.getSize())
                .totalElements(doctorPage.getTotalElements())
                .totalPages(doctorPage.getTotalPages())
                .first(doctorPage.isFirst())
                .last(doctorPage.isLast())
                .hasNext(doctorPage.hasNext())
                .hasPrevious(doctorPage.hasPrevious())
                .build();

        // 4. Trả về response chuẩn
        ApiResponse<Page<Doctor>> response = ApiResponse.success(
                "Lấy danh sách bác sĩ thành công",
                doctorPage,
                meta
        );

        return ResponseEntity.ok(response);
    }

    // ===== API LẤY BÁC SĨ THEO ID =====
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Doctor>> getDoctor(@PathVariable Long id) {
        try {
            Doctor doctor = doctorService.getDoctorById(id);

            ApiResponse<Doctor> response = ApiResponse.success(
                    "Lấy thông tin bác sĩ thành công",
                    doctor
            );

            return ResponseEntity.ok(response);

        } catch (DoctorNotFoundException e) {
            ApiResponse<Doctor> response = ApiResponse.notFound(
                    e.getMessage(),
                    "/doctors/" + id
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // ===== API CẬP NHẬT BÁC SĨ =====
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody DoctorUpdateDTO dto,
            BindingResult bindingResult) {

        // 1. Kiểm tra validation
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }

            ApiResponse<Map<String, String>> response = ApiResponse.badRequest(
                    "Validation failed",
                    "/doctors/" + id
            );
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // 2. Update doctor
            Doctor updatedDoctor = doctorService.updateDoctor(id, dto);

            // 3. Trả về response thành công
            ApiResponse<Doctor> response = ApiResponse.success(
                    "Cập nhật bác sĩ thành công",
                    updatedDoctor
            );
            return ResponseEntity.ok(response);

        } catch (DoctorNotFoundException e) {
            // 4. ID không tồn tại
            ApiResponse<Doctor> response = ApiResponse.notFound(
                    e.getMessage(),
                    "/doctors/" + id
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {
            // 5. Lỗi khác
            log.error("Error updating doctor: {}", e.getMessage());
            ApiResponse<Doctor> response = ApiResponse.internalError(
                    "Failed to update doctor: " + e.getMessage(),
                    "/doctors/" + id
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}