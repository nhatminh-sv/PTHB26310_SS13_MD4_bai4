package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageMeta {

    private int page;               // Trang hiện tại (bắt đầu từ 0)
    private int size;               // Số bản ghi trên 1 trang
    private long totalElements;     // Tổng số bản ghi
    private int totalPages;         // Tổng số trang
    private boolean first;          // Có phải trang đầu không
    private boolean last;           // Có phải trang cuối không
    private boolean hasNext;        // Có trang tiếp theo không
    private boolean hasPrevious;    // Có trang trước không
}