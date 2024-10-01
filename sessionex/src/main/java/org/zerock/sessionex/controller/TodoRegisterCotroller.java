package org.zerock.sessionex.controller;

import lombok.extern.log4j.Log4j2;
import org.zerock.sessionex.dto.TodoDTO;
import org.zerock.sessionex.service.TodoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet(name="todoRegisterController",value="/todo/register")
@Log4j2
public class TodoRegisterCotroller extends HttpServlet {

    private TodoService todoService=TodoService.INSTANCE;
    private final DateTimeFormatter DATETIMEFOMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("/todo/register GET ...");
        HttpSession session=req.getSession();

        if(session.isNew()){
            log.info("this user got JSESSIONID cookie just now");
            resp.sendRedirect("/login");
            return;
        }

        if(session.getAttribute("loginInfo")==null){
            log.info("this user has not logined.");
            resp.sendRedirect("/login");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/todo/register.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("/todo/register POST..");
        TodoDTO todoDTO=TodoDTO.builder()
                .title(req.getParameter("title"))
                .dueDate(LocalDate.parse(req.getParameter("dueDate"),DATETIMEFOMATTER))
                .build();
        log.info(todoDTO);
        try {
            todoService.register(todoDTO);
        }catch (Exception e){
            log.info(e.getMessage());
        }

        resp.sendRedirect("/todo/list");
    }
}
