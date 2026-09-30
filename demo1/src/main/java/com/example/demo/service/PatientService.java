package com.example.demo.service;

import com.example.demo.entity.Patient;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService {

    private final PatientRepository patientRepository;

    // Lấy tất cả bệnh nhân
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    // Lấy bệnh nhân theo ID - NÉM EXCEPTION NẾU KHÔNG TÌM THẤY
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient",
                        "ID",
                        id
                ));
        // Hoặc dùng message tùy chỉnh:
        // .orElseThrow(() -> new ResourceNotFoundException(
        //     "Không tìm thấy bệnh nhân với ID: " + id
        // ));
    }

    // Tạo mới bệnh nhân
    public Patient createPatient(Patient patient) {
        // Kiểm tra email đã tồn tại chưa
        if (patientRepository.existsByEmail(patient.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + patient.getEmail());
        }
        return patientRepository.save(patient);
    }

    // Cập nhật bệnh nhân
    public Patient updatePatient(Long id, Patient patientDetails) {
        Patient existingPatient = getPatientById(id);

        existingPatient.setFullName(patientDetails.getFullName());
        existingPatient.setEmail(patientDetails.getEmail());
        existingPatient.setPhoneNumber(patientDetails.getPhoneNumber());
        existingPatient.setDateOfBirth(patientDetails.getDateOfBirth());
        existingPatient.setAddress(patientDetails.getAddress());
        existingPatient.setBloodType(patientDetails.getBloodType());
        existingPatient.setIsActive(patientDetails.getIsActive());

        return patientRepository.save(existingPatient);
    }

    // Xóa bệnh nhân
    public void deletePatient(Long id) {
        Patient patient = getPatientById(id);
        patientRepository.delete(patient);
    }
}