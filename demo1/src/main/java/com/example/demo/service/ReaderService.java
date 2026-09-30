package com.example.demo.service;

import com.example.demo.dto.ReaderCreateDTO;
import com.example.demo.dto.ReaderResponseDTO;
import com.example.demo.entity.Reader;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ReaderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReaderService {

    private final ReaderRepository readerRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional(rollbackFor = Exception.class)
    public ReaderResponseDTO createReader(ReaderCreateDTO dto) throws Exception {

        // 1. Kiểm tra email đã tồn tại
        if (readerRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + dto.getEmail());
        }

        // 2. Kiểm tra file ảnh
        MultipartFile avatarFile = dto.getAvatarFile();
        if (avatarFile == null || avatarFile.isEmpty()) {
            throw new IllegalArgumentException("Avatar file is required");
        }

        // 3. Upload file lên Cloudinary
        String avatarUrl;
        try {
            avatarUrl = cloudinaryService.uploadImage(avatarFile);
        } catch (IOException e) {
            log.error("Failed to upload avatar: {}", e.getMessage());
            throw new Exception("Failed to upload avatar: " + e.getMessage());
        }

        // 4. Tạo Entity Reader
        Reader reader = new Reader();
        reader.setEmail(dto.getEmail());
        reader.setFullName(dto.getFullName());
        reader.setPhoneNumber(dto.getPhoneNumber());
        reader.setAddress(dto.getAddress());
        reader.setAvatar(avatarUrl);

        // 5. Lưu vào database
        Reader savedReader = readerRepository.save(reader);
        log.info("Reader created successfully with ID: {}", savedReader.getId());

        // 6. Chuyển đổi sang ResponseDTO
        return mapToResponseDTO(savedReader);
    }

    // Chuyển đổi Entity → ResponseDTO
    private ReaderResponseDTO mapToResponseDTO(Reader reader) {
        return ReaderResponseDTO.builder()
                .id(reader.getId())
                .email(reader.getEmail())
                .fullName(reader.getFullName())
                .phoneNumber(reader.getPhoneNumber())
                .address(reader.getAddress())
                .avatar(reader.getAvatar())
                .createdAt(reader.getCreatedAt())
                .build();
    }
}