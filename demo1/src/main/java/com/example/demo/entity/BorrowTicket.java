package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "borrow_tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "borrow_code", nullable = false, unique = true, length = 20)
    private String borrowCode;  // Mã phiếu mượn

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reader_id", nullable = false)
    private Reader reader;  // Độc giả mượn

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;  // Sách được mượn

    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate;  // Ngày mượn

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;  // Hạn trả

    @Column(name = "return_date")
    private LocalDate returnDate;  // Ngày trả thực tế

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BorrowStatus status;  // BORROWING, RETURNED

    @Column(name = "late_fee")
    private Double lateFee;  // Phí trả muộn

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = BorrowStatus.BORROWING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Enum trạng thái phiếu mượn
    public enum BorrowStatus {
        BORROWING,  // Đang mượn
        RETURNED    // Đã trả
    }
}