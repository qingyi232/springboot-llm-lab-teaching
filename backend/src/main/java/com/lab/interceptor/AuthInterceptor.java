package com.lab.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lab.entity.TokenEntity;
import com.lab.mapper.TokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Date;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenMapper tokenMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Token");
        if (token == null || token.isEmpty()) {
            token = request.getParameter("token");
        }

        if (token == null || token.isEmpty()) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write("{\"code\":401,\"msg\":\"请先登录\"}");
            writer.flush();
            return false;
        }

        TokenEntity tokenEntity = tokenMapper.selectOne(
                new QueryWrapper<TokenEntity>().eq("token", token)
        );

        if (tokenEntity == null || tokenEntity.getExpiratedtime().before(new Date())) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write("{\"code\":401,\"msg\":\"登录已过期，请重新登录\"}");
            writer.flush();
            return false;
        }

        request.setAttribute("userId", tokenEntity.getUserid());
        request.setAttribute("username", tokenEntity.getUsername());
        request.setAttribute("role", tokenEntity.getRole());
        request.setAttribute("tablename", tokenEntity.getTablename());

        return true;
    }
}
