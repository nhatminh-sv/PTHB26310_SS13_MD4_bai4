package com.example.demo.dto;

import com.example.demo.annotation.ExistingBookId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowCreateDTO {

    @NotBlank(message = "Username is required")
    private String username;

    @NotNull(message = "Book ID is required")
    @ExistingBookId  // Custom validation - Kiểm tra sách tồn tại
    private Long bookId;
}
