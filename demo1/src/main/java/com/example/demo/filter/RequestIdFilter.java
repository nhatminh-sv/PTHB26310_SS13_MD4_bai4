package com.example.demo.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class RequestIdFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID_KEY = "requestId";
    private static final String CLIENT_IP_KEY = "clientIp";
    private static final String USER_AGENT_KEY = "userAgent";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Sinh mã UUID ngẫu nhiên cho request
        String requestId = UUID.randomUUID().toString();

        // 2. Lấy thông tin client
        String clientIp = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");

        try {
            // 3. Gắn thông tin vào MDC
            MDC.put(REQUEST_ID_KEY, requestId);
            MDC.put(CLIENT_IP_KEY, clientIp);
            MDC.put(USER_AGENT_KEY, userAgent != null ? userAgent : "Unknown");

            // 4. Ghi log request bắt đầu
            log.info("=== REQUEST START ===");
            log.info("Method: {}, URI: {}", request.getMethod(), request.getRequestURI());
            log.info("Client IP: {}, User-Agent: {}", clientIp, userAgent);

            // 5. Thực hiện request
            long startTime = System.currentTimeMillis();
            filterChain.doFilter(request, response);
            long duration = System.currentTimeMillis() - startTime;

            // 6. Ghi log request kết thúc
            log.info("=== REQUEST END ===");
            log.info("Status: {}, Duration: {}ms", response.getStatus(), duration);

        } finally {
            // 7. Xóa MDC sau khi request hoàn thành (quan trọng để không bị rò rỉ)
            MDC.clear();
        }
    }

    /**
     * Lấy địa chỉ IP thực của client (khi có proxy)
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // Trường hợp có nhiều IP, lấy IP đầu tiên
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}