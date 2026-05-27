package com.lab.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;

import java.util.Date;
import java.util.UUID;

public class JwtUtils {

    public static String generateToken(Long userId, String username, String role) {
        String raw = userId + "_" + username + "_" + role + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID();
        return DigestUtil.md5Hex(raw);
    }

    public static boolean isTokenExpired(Date expireTime) {
        return expireTime != null && expireTime.before(new Date());
    }

    public static Date getExpireTime() {
        return DateUtil.offsetHour(new Date(), 24);
    }
}
