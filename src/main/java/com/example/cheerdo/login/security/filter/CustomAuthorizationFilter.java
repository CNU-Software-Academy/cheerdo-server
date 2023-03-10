package com.example.cheerdo.login.security.filter;

import com.example.cheerdo.login.security.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

/**
 * 인증필터 토큰이 제대로된 토큰인지 확인
 * app에 들어오는 모든 요청을 가로채 해당 특정 토큰을 찾은 다음
 * 사용자가 특정 리소스에 엑세스 할 수 있는지 여부를 결정
 * 이게 token의 장점 token가지고만 유효한지만 검증하면 되기에 세션DB가 필요없음
 */
@Component
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(CustomAuthorizationFilter.class);
    private TokenProvider jwtUtil;
    public CustomAuthorizationFilter(TokenProvider jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // 여기서  url 가로챔
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getMethod().equals("OPTIONS")) {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Allow-Headers", "authorization, content-type, xsrf-token");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            String token = jwtUtil.resolveToken(request);
            if (token != null && jwtUtil.validateToken(token)) {
                try {
                    UsernamePasswordAuthenticationToken authentication = jwtUtil.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response);

                } catch (Exception exception) { // when the token is valid expired or something else
                    logger.error("An error occurred during token verification: {} ", exception.getMessage());
                    response.setStatus(FORBIDDEN.value());
                    Map<String, String> error = new HashMap<>();
                    error.put("error_message", exception.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }

}
