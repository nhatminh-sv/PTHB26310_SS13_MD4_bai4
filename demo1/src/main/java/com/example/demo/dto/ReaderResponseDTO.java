package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReaderResponseDTO {
    private Long id;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String avatar;
    private LocalDateTime createdAt;
}