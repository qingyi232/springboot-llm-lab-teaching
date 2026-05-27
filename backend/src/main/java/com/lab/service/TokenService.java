package com.lab.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lab.entity.TokenEntity;
import com.lab.mapper.TokenMapper;
import com.lab.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Autowired
    private TokenMapper tokenMapper;

    public String generateToken(Long userId, String username, String tablename, String role) {
        TokenEntity existing = tokenMapper.selectOne(
                new QueryWrapper<TokenEntity>()
                        .eq("userid", userId)
                        .eq("role", role)
        );

        String token = JwtUtils.generateToken(userId, username, role);
        Date expireTime = JwtUtils.getExpireTime();

        if (existing != null) {
            existing.setToken(token);
            existing.setExpiratedtime(expireTime);
            tokenMapper.updateById(existing);
        } else {
            TokenEntity tokenEntity = new TokenEntity();
            tokenEntity.setUserid(userId);
            tokenEntity.setUsername(username);
            tokenEntity.setTablename(tablename);
            tokenEntity.setRole(role);
            tokenEntity.setToken(token);
            tokenEntity.setAddtime(new Date());
            tokenEntity.setExpiratedtime(expireTime);
            tokenMapper.insert(tokenEntity);
        }

        return token;
    }

    public TokenEntity getTokenEntity(String token) {
        return tokenMapper.selectOne(
                new QueryWrapper<TokenEntity>().eq("token", token)
        );
    }
}
