package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test-logs")
@Slf4j
public class LogTestController {

    // ... (các method cũ)

    // ===== API GÂY LỖI 500 - CHIA CHO 0 =====
    @GetMapping("/divide/{number}")
    public ApiResponse<String> testDivideByZero(@PathVariable int number) {
        log.info("🔵 Testing divide by zero with number: {}", number);

        // ⚠️ Gây lỗi chia cho 0
        int result = 100 / number;  // Nếu number = 0 → ArithmeticException

        log.info("✅ Result: {}", result);
        return ApiResponse.success("Division successful", "Result: " + result);
    }

    // ===== API GÂY LỖI 500 - NULL POINTER =====
    @GetMapping("/null-pointer")
    public ApiResponse<String> testNullPointer() {
        log.info("🔵 Testing NullPointerException");

        String str = null;
        int length = str.length();  // ⚠️ Gây lỗi NullPointerException

        return ApiResponse.success("Success", "Length: " + length);
    }

    // ===== API GÂY LỖI 500 - ARRAY INDEX OUT OF BOUNDS =====
    @GetMapping("/array-index")
    public ApiResponse<String> testArrayIndex() {
        log.info("🔵 Testing ArrayIndexOutOfBoundsException");

        int[] arr = {1, 2, 3};
        int value = arr[10];  // ⚠️ Gây lỗi ArrayIndexOutOfBoundsException

        return ApiResponse.success("Success", "Value: " + value);
    }

    // ===== API GÂY LỖI 500 - CUSTOM EXCEPTION =====
    @GetMapping("/custom-error")
    public ApiResponse<String> testCustomError() {
        log.info("🔵 Testing custom error");

        try {
            // Mô phỏng xử lý nghiệp vụ
            processBusinessLogic();
        } catch (Exception e) {
            log.error("❌ Error in business logic: {}", e.getMessage());
            throw new RuntimeException("Business logic failed: " + e.getMessage(), e);
        }

        return ApiResponse.success("Success", "Process completed");
    }

    // Mô phỏng nghiệp vụ có lỗi
    private void processBusinessLogic() {
        // Giả sử có lỗi trong business logic
        if (true) {
            throw new IllegalArgumentException("Invalid data: user not found");
        }
    }
}