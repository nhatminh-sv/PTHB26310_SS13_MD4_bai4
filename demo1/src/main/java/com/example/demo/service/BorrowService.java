package com.example.demo.service;

import com.example.demo.dto.BorrowCreateDTO;
import com.example.demo.dto.BorrowTicketResponseDTO;
import com.example.demo.entity.Book;
import com.example.demo.entity.Borrow;
import com.example.demo.entity.BorrowTicket;
import com.example.demo.entity.BorrowTicket.BorrowStatus;
import com.example.demo.exception.BookAlreadyReturnedException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.BorrowRepository;
import com.example.demo.repository.BorrowTicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class BorrowService {

    private final BorrowTicketRepository borrowTicketRepository;
    private final BookRepository bookRepository;
    private final BorrowRepository borrowRepository;  // Thêm repository mới

    // ... (các method cũ: returnBook, calculateLateFee, mapToResponseDTO)

    /**
     * Tạo phiếu mượn mới
     * @param dto DTO chứa thông tin mượn
     * @return Thông tin phiếu mượn đã tạo
     */
    @Transactional
    public Borrow createBorrow(BorrowCreateDTO dto) {
        // 1. Tìm sách để kiểm tra tồn kho
        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book", "ID", dto.getBookId()));

        // 2. Kiểm tra số lượng sách có sẵn
        if (book.getAvailableQuantity() <= 0) {
            throw new IllegalArgumentException("Book is out of stock: " + book.getTitle());
        }

        // 3. Tạo phiếu mượn
        Borrow borrow = new Borrow();
        borrow.setUsername(dto.getUsername());
        borrow.setBookId(dto.getBookId());

        // 4. Giảm số lượng sách
        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookRepository.save(book);

        // 5. Lưu phiếu mượn
        Borrow savedBorrow = borrowRepository.save(borrow);
        log.info("Borrow created successfully with ID: {}", savedBorrow.getId());

        return savedBorrow;
    }

    // ... (các method cũ)
}