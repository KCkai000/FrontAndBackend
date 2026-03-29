package com.example.demo.common.util;

import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.regex.Pattern;

/**
 * 安全工具：密碼加鹽雜湊、XSS 防護輸入清理
 */
@Component
public class SecurityUtils {

    private static final int SALT_BYTES   = 32;
    private static final int HASH_ROUNDS  = 100_000;
    private static final Pattern PHONE_PATTERN = Pattern.compile("^09\\d{8}$");

    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * 產生隨機 salt (Base64 encoded)
     */
    public String generateSalt() {
        byte[] saltBytes = new byte[SALT_BYTES];
        secureRandom.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    /**
     * 使用 SHA-256 + salt 進行多輪雜湊
     */
    public String hashPassword(String rawPassword, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = (salt + rawPassword).getBytes(StandardCharsets.UTF_8);
            for (int i = 0; i < HASH_ROUNDS; i++) {
                hash = digest.digest(hash);
            }
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    /**
     * 驗證密碼（constant-time 比較防止 timing attack）
     */
    public boolean verifyPassword(String rawPassword, String salt, String storedHash) {
        String computed = hashPassword(rawPassword, salt);
        return MessageDigest.isEqual(
                computed.getBytes(StandardCharsets.UTF_8),
                storedHash.getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * 跳脫 HTML 特殊字元，防止 XSS
     */
    public String sanitize(String input) {
        if (input == null) return null;
        return HtmlUtils.htmlEscape(input.trim());
    }

    // ────────── Validation ──────────

    public boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }
}
