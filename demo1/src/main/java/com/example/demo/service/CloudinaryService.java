package com.example.demo.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;

    // Danh sách các định dạng ảnh cho phép
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/png",
            "image/jpeg",
            "image/jpg"
    );

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            ".png", ".jpg", ".jpeg"
    );

    /**
     * Upload file ảnh lên Cloudinary
     * @param file MultipartFile từ client
     * @return URL của ảnh đã upload
     * @throws IOException nếu upload thất bại
     */
    public String uploadImage(MultipartFile file) throws IOException {
        // 1. Kiểm tra file null hoặc rỗng
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Avatar file is required");
        }

        // 2. Kiểm tra kích thước file (tối đa 5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("File size must be less than 5MB");
        }

        // 3. Kiểm tra định dạng file
        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();

        boolean isValidType = ALLOWED_IMAGE_TYPES.contains(contentType);
        boolean isValidExtension = originalFilename != null &&
                ALLOWED_EXTENSIONS.stream().anyMatch(originalFilename.toLowerCase()::endsWith);

        if (!isValidType && !isValidExtension) {
            throw new IllegalArgumentException("Only PNG, JPG, JPEG files are allowed");
        }

        try {
            // 4. Upload lên Cloudinary
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "resource_type", "image",
                    "folder", "reader_avatars",
                    "public_id", System.currentTimeMillis() + "_" + originalFilename
            );

            Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), uploadParams);

            // 5. Lấy URL từ kết quả
            String url = result.get("secure_url").toString();
            log.info("File uploaded successfully: {}", url);

            return url;

        } catch (IOException e) {
            log.error("Failed to upload file: {}", e.getMessage());
            throw new IOException("Failed to upload avatar: " + e.getMessage());
        }
    }
}