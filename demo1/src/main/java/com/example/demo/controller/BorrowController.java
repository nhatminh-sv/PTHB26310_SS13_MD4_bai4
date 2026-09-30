package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.BorrowTicketResponseDTO;
import com.example.demo.exception.BookAlreadyReturnedException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.BorrowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/borrows")
@RequiredArgsConstructor
@Slf4j
public class BorrowController {

    private final BorrowService borrowService;

    /**
     * API: Trả sách
     * PATCH /api/borrows/{id}/return
     */
    @PatchMapping("/{id}/return")
    public ResponseEntity<ApiResponse<BorrowTicketResponseDTO>> returnBook(
            @PathVariable("id") Long ticketId) {

        try {
            // Xử lý trả sách
            BorrowTicketResponseDTO result = borrowService.returnBook(ticketId);

            // Trả về thành công
            ApiResponse<BorrowTicketResponseDTO> response = ApiResponse.success(
                    "Book returned successfully",
                    result
            );
            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException e) {
            // Lỗi 404 - Không tìm thấy phiếu mượn
            ApiResponse<BorrowTicketResponseDTO> response = ApiResponse.notFound(
                    e.getMessage(),
                    "/api/borrows/" + ticketId + "/return"
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (BookAlreadyReturnedException e) {
            // Lỗi 400 - Sách đã được trả
            ApiResponse<BorrowTicketResponseDTO> response = ApiResponse.badRequest(
                    e.getMessage(),
                    "/api/borrows/" + ticketId + "/return"
            );
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            // Lỗi 500 - Lỗi server
            log.error("Error returning book: {}", e.getMessage());
            ApiResponse<BorrowTicketResponseDTO> response = ApiResponse.internalError(
                    "Failed to return book: " + e.getMessage(),
                    "/api/borrows/" + ticketId + "/return"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}