package com.auth.center.util;

import com.auth.center.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Desc:
 * @Author: zhoujianbo
 * @Version: 1.0
 * @Date: 2025/5/13 14:33
 **/
@Component
public class JwtUtil {

    @Resource
    private JwtProperties jwtProperties;

    /**
     * 基于JWT生成token
     * @param username
     * @return
     */
    public String generateToken(String username) {

        Date now = new Date();

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtProperties.getExpire() * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
                .compact();
    }

    /**
     * 解析token,获取用户名
     */
    public String getUsernameFromToken(String token) {
        try {
            Claims claimsByToken = Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret())
                    .parseClaimsJws(token)
                    .getBody();

            return claimsByToken.get("username", String.class);
        } catch (Exception e) {
            return null;
        }
    }

    public long getExpire() {
        return jwtProperties.getExpire();
    }
}
