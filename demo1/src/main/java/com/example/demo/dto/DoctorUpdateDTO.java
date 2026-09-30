package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorUpdateDTO {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^[0-9]{10,11}$", message = "Phone number must be 10-11 digits")
    private String phoneNumber;

    @NotBlank(message = "Specialization is required")
    private String specializationName;

    private String departmentName;

    @NotNull(message = "Years of experience is required")
    private Integer yearsOfExperience;

    private List<WorkingHourDTO> workingHours;

    private String address;

    private Boolean isActive;

    private Boolean isAvailable;
}