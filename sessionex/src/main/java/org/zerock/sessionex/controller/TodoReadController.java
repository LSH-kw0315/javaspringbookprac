package org.zerock.sessionex.controller;

import lombok.extern.log4j.Log4j2;
import org.zerock.sessionex.dto.TodoDTO;
import org.zerock.sessionex.service.TodoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@WebServlet(name="todoReadController", value = "/todo/read")
public class TodoReadController extends HttpServlet {

    private TodoService todoService=TodoService.INSTANCE;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            Long tno = Long.parseLong(req.getParameter("tno"));
            TodoDTO dto=todoService.get(tno);
            req.setAttribute("dto",dto);

            Cookie viewTodoCookie=findCookie(req.getCookies(),"viewTodos");
            String todoListStr=viewTodoCookie.getValue();

            if(todoListStr !=null && !todoListStr.contains(tno+"-")){
                todoListStr +=tno+"-";
                viewTodoCookie.setValue(todoListStr);
                viewTodoCookie.setMaxAge(60*60*24);
                viewTodoCookie.setPath("/");
                resp.addCookie(viewTodoCookie);
            }

            req.getRequestDispatcher("/WEB-INF/todo/read.jsp").forward(req,resp);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new ServletException("read error");
        }
    }

    private Cookie findCookie(Cookie[] cookies,String cookieName){
        Cookie target=null;
        if(cookies!=null && cookies.length>0) {
            for (Cookie c : cookies) {
                if(c.getName().equals(cookieName)){
                    target=c;
                    break;
                }
            }
        }
        if(target==null){
            target=new Cookie(cookieName,"");//쿠기의 key-value 설정
            target.setPath("/");//경로
            target.setMaxAge(60*60*24);//유효시간 설정
        }
        return target;
    }
}
