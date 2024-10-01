package org.zerock.jdbc.controller;

import lombok.extern.log4j.Log4j2;
import org.zerock.jdbc.dto.TodoDTO;
import org.zerock.jdbc.service.TodoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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
            req.getRequestDispatcher("/WEB-INF/todo/read.jsp").forward(req,resp);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new ServletException("read error");
        }
    }
}
