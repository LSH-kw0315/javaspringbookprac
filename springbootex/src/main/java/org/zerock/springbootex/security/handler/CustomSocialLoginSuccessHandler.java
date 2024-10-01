package org.zerock.springbootex.security.handler;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.zerock.springbootex.security.dto.MemberSecurityDTO;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final PasswordEncoder passwordEncoder;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("-----");
        log.info("CustomLoginSuccessHandler onAuthenticationSuccess..");
        log.info(authentication.getPrincipal());

        MemberSecurityDTO memberSecurityDTO=(MemberSecurityDTO) authentication.getPrincipal();

        if(memberSecurityDTO.isSocial() &&(memberSecurityDTO.getMpw().equals("1111")||passwordEncoder.matches("1111",memberSecurityDTO.getMpw()))){
            log.info("Should change Password");
            log.info("Redirect to Member modify");
            response.sendRedirect("/member/redirect");
            return;
        }else{
            response.sendRedirect("/board/list");
        }
    }
}
