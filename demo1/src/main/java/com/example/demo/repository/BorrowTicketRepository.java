package com.example.demo.repository;

import com.example.demo.entity.BorrowTicket;
import com.example.demo.entity.BorrowTicket.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowTicketRepository extends JpaRepository<BorrowTicket, Long> {

    Optional<BorrowTicket> findByBorrowCode(String borrowCode);

    List<BorrowTicket> findByStatus(BorrowStatus status);

    List<BorrowTicket> findByReaderIdAndStatus(Long readerId, BorrowStatus status);
}