package com.boltblazers.erp.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KeepAliveScheduler {

    private static final Logger log = LoggerFactory.getLogger(KeepAliveScheduler.class);

    private final JdbcTemplate jdbcTemplate;
    private final RestTemplate restTemplate;

    @Value("${app.self-url:https://bb-erp-be.onrender.com}")
    private String selfUrl;

    public KeepAliveScheduler(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(8000);
        factory.setReadTimeout(8000);
        this.restTemplate = new RestTemplate(factory);
    }

    /**
     * Database Keep-Alive:
     * Pings the database every 2 minutes with SELECT 1.
     * Keeps the TCP socket and cloud NAT / gateway connection fresh so Aiven / cloud provider never drops idle connections.
     */
    @Scheduled(fixedDelay = 120000, initialDelay = 30000)
    public void pingDatabase() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            log.debug("Database keep-alive ping successful (SELECT 1)");
        } catch (Exception e) {
            log.warn("Database keep-alive ping encountered an issue: {}", e.getMessage());
        }
    }

    /**
     * Render Web Service Self-Ping:
     * Render free tier puts services to sleep after 15 minutes of inbound HTTP inactivity.
     * Pinging our public /api/health every 9 minutes ensures Render's idle counter never reaches 15 minutes.
     */
    @Scheduled(fixedDelay = 540000, initialDelay = 60000)
    public void pingSelf() {
        if (selfUrl == null || selfUrl.isBlank()) {
            return;
        }

        // Avoid pinging localhost in local development
        if (selfUrl.contains("localhost") || selfUrl.contains("127.0.0.1")) {
            return;
        }

        try {
            String targetUrl = selfUrl.replaceAll("/+$", "") + "/api/health";
            String response = restTemplate.getForObject(targetUrl, String.class);
            log.info("Render keep-alive self-ping sent to {} -> Response: {}", targetUrl, response);
        } catch (Exception e) {
            log.warn("Render keep-alive self-ping failed: {}", e.getMessage());
        }
    }
}
