package com.example.demo.validator;

import com.example.demo.annotation.ExistingBookId;
import com.example.demo.repository.BookRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookIdValidator implements ConstraintValidator<ExistingBookId, Long> {

    private final BookRepository bookRepository;

    @Override
    public boolean isValid(Long bookId, ConstraintValidatorContext context) {
        // Nếu bookId = null, validation sẽ do @NotNull xử lý
        if (bookId == null) {
            return true; // Không check ở đây, để @NotNull xử lý
        }

        // Kiểm tra bookId có tồn tại trong database không
        return bookRepository.existsById(bookId);
    }

    @Override
    public void initialize(ExistingBookId constraintAnnotation) {
        // Không cần khởi tạo gì
    }
}