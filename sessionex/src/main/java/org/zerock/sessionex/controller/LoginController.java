package org.zerock.sessionex.controller;

import lombok.extern.log4j.Log4j2;
import org.zerock.sessionex.dto.MemberDTO;
import org.zerock.sessionex.service.MemberService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.UUID;

@Log4j2
@WebServlet(name="loginController",value = "/login")
public class LoginController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("login GET..");
        req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String mid=req.getParameter("mid");
        String mpw=req.getParameter("mpw");
        String auto=req.getParameter("auto");
        boolean rememberMe=auto!=null&&auto.equals("on");

        try{
            MemberDTO memberDTO= MemberService.INSTANCE.login(mid,mpw);

            if(rememberMe){//자동로그인을 원한다면
              String uuid= UUID.randomUUID().toString();//uuid 발급

              MemberService.INSTANCE.updateUUID(mid,uuid);//member DB에 uuid 업데이트
              memberDTO.setUuid(uuid);//얻어온 dto에도 반영

                Cookie rememeberCookie=new Cookie("remember-me",uuid);
                rememeberCookie.setMaxAge(60*60*24*7);
                rememeberCookie.setPath("/");
                resp.addCookie(rememeberCookie);
            }
            HttpSession session=req.getSession();
            session.setAttribute("loginInfo",memberDTO);
            resp.sendRedirect("/todo/list");
        }catch (Exception e){
            resp.sendRedirect("/login?result=error");
        }
    }
}
