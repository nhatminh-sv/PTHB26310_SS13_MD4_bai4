package com.example.demo.service;

import com.example.demo.dto.DoctorUpdateDTO;
import com.example.demo.dto.WorkingHourDTO;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.WorkingHour;
import com.example.demo.exception.DoctorNotFoundException;
import com.example.demo.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorService {

    private final DoctorRepository doctorRepository;

    // ===== LẤY DANH SÁCH CÓ PHÂN TRANG =====
    public Page<Doctor> getAllDoctors(Pageable pageable) {
        return doctorRepository.findAll(pageable);
    }

    // ===== LẤY TẤT CẢ (Không phân trang - Giữ lại cho các API khác) =====
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with ID: " + id));
    }

    @Transactional
    public Doctor updateDoctor(Long id, DoctorUpdateDTO dto) {
        // 1. Tìm doctor
        Doctor doctor = getDoctorById(id);

        // 2. Update thông tin
        doctor.setFullName(dto.getFullName());
        doctor.setFirstName(dto.getFirstName());
        doctor.setLastName(dto.getLastName());
        doctor.setEmail(dto.getEmail());
        doctor.setPhoneNumber(dto.getPhoneNumber());
        doctor.setSpecializationName(dto.getSpecializationName());
        doctor.setDepartmentName(dto.getDepartmentName());
        doctor.setYearsOfExperience(dto.getYearsOfExperience());
        doctor.setAddress(dto.getAddress());

        if (dto.getIsActive() != null) {
            doctor.setIsActive(dto.getIsActive());
        }
        if (dto.getIsAvailable() != null) {
            doctor.setIsAvailable(dto.getIsAvailable());
        }

        // 3. Update working hours
        if (dto.getWorkingHours() != null && !dto.getWorkingHours().isEmpty()) {
            updateWorkingHours(doctor, dto.getWorkingHours());
        }

        // 4. Lưu vào database
        Doctor updatedDoctor = doctorRepository.save(doctor);
        log.info("Doctor updated successfully with ID: {}", updatedDoctor.getId());

        return updatedDoctor;
    }

    private void updateWorkingHours(Doctor doctor, List<WorkingHourDTO> workingHourDTOs) {
        // Xóa working hours cũ
        if (doctor.getWorkingHours() != null) {
            doctor.getWorkingHours().clear();
        } else {
            doctor.setWorkingHours(new ArrayList<>());
        }

        // Thêm working hours mới
        List<WorkingHour> newWorkingHours = new ArrayList<>();
        for (WorkingHourDTO dto : workingHourDTOs) {
            WorkingHour workingHour = new WorkingHour();
            workingHour.setDay(dto.getDay());
            workingHour.setStartTime(dto.getStartTime());
            workingHour.setEndTime(dto.getEndTime());
            workingHour.setDoctor(doctor);
            newWorkingHours.add(workingHour);
        }

        doctor.getWorkingHours().addAll(newWorkingHours);
    }
}