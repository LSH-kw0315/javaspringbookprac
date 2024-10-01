package org.zerock.jwtprac.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zerock.jwtprac.security.APIUserDetailsService;
import org.zerock.jwtprac.security.Exception.AccessTokenException;
import org.zerock.jwtprac.util.JWTUtil;

import java.io.IOException;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final APIUserDetailsService apiUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path=request.getRequestURI();

        if(!path.startsWith("/api")){
            filterChain.doFilter(request,response);
            return;
        }

        log.info("Token Check Filter...");
        log.info("JWTUtil:"+jwtUtil);
        try {
            Map<String,Object> payload=validateAccessToken(request);

            String mid=(String)payload.get("mid");
            log.info("mid:"+mid);
            UserDetails userDetails=apiUserDetailsService.loadUserByUsername(mid);

            UsernamePasswordAuthenticationToken authenticationToken=
                    new UsernamePasswordAuthenticationToken(
                      userDetails,
                      null,
                      userDetails.getAuthorities()
                    );

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);
        }catch (AccessTokenException accessTokenException){
            accessTokenException.sendResponseError(response);
        }
    }

    private Map<String,Object> validateAccessToken(HttpServletRequest request) throws AccessTokenException {
        String headerStr=request.getHeader("Authorization");

        if(headerStr==null||headerStr.length()<0){
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.UNACCEPT);
        }

        String tokenType=headerStr.substring(0,6);//0~6: type
        String tokenStr=headerStr.substring(7);//7~:token값

        if(!tokenType.equalsIgnoreCase("Bearer")){
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADTYPE);
        }

        try{
            Map<String,Object> values=jwtUtil.validateToken(tokenStr);

            return values;
        }catch (MalformedJwtException malformedJwtException){
            log.error("MalformedJWTException...");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.MALFORM);
        }catch (SignatureException signatureException){
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADSIGN);
        }catch (ExpiredJwtException expiredJwtException){
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.EXPIRED);
        }
    }
}
