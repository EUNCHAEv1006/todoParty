package com.thesun4sky.todoparty.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thesun4sky.todoparty.CommonResponseDto;
import com.thesun4sky.todoparty.user.UserDetailsService;
import com.thesun4sky.todoparty.user.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        String token = jwtUtil.resolveToken(request);

        if (token != null) {
            if(jwtUtil.validateToken(token)) {

                Claims info = jwtUtil.getUserInfoFromToken(token);

                // 인증정보에 유저정보(username) 넣기
                // username -> user 조회 -> userDetails에 담고 -> authentication의 principal에 담고
                String username = info.getSubject();
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UserDetails userDetails = userDetailsService.getUserDetails(username);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                // -> securityContent에 담고
                context.setAuthentication(authentication);
                // -> SecurityContextHolder에 담고
                SecurityContextHolder.setContext(context);
                // -> 이제 @AuthenticationPrincipal로 조회 가능

            } else {
                // 인증정보가 존재하지 않을때
                CommonResponseDto commonResponseDto = new CommonResponseDto("토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST.value());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json; charset=UTF-8");
                response.getWriter().write(objectMapper.writeValueAsString(commonResponseDto)); // 응답 Body가 Json형태로 바껴서 들어감
                return;
            }
        }

        // 로그인 처리 끝났으면 다음 필터로 넘어갈 수 있도록 처리해줘야함
        filterChain.doFilter(request, response);
    }
}
