package org.zerock.sessionex.filter;

import lombok.extern.log4j.Log4j2;
import org.zerock.sessionex.dto.MemberDTO;
import org.zerock.sessionex.service.MemberService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Log4j2
@WebFilter(urlPatterns = {"/todo/*"})
public class LoginCheckFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("Login Check Filter...");

        HttpServletRequest req=(HttpServletRequest)servletRequest;
        HttpServletResponse resp=(HttpServletResponse)servletResponse;


        HttpSession session=req.getSession();

        if(session.getAttribute("loginInfo")!=null){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }

        Cookie cookie=findCookie(req.getCookies(), "remember-me");
        if(cookie==null){
            resp.sendRedirect("/login");
            return;
        }

        log.info("auto login");

        String uuid=cookie.getValue();

        try {
            MemberDTO dto= MemberService.INSTANCE.getByUUID(uuid);

            if(dto==null){
                throw new Exception("cookie value is invalid");
            }

            session.setAttribute("loginInfo",dto);
            filterChain.doFilter(req,resp);
        }catch (Exception e){
            e.printStackTrace();
            resp.sendRedirect("/login");
        }

    }

    private  Cookie findCookie(Cookie[] cookies,String name){
        if(cookies==null || cookies.length==0){
            return null;
        }

        Optional<Cookie> result = Arrays.stream(cookies)
                .filter(c->c.getName().equals(name))
                .findFirst();

        return result.isPresent()?result.get():null;

    }
}
