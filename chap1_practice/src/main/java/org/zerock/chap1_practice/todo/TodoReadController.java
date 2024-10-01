package org.zerock.chap1_practice.todo;

import org.zerock.chap1_practice.todo.dto.TodoDTO;
import org.zerock.chap1_practice.todo.service.TodoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="TodoReadController",urlPatterns = "/todo/read")
public class TodoReadController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long tno=Long.parseLong(req.getParameter("tno"));
        TodoDTO dto= TodoService.INSTANCE.getTodoDTO(tno);
        req.setAttribute("DTO",dto);
        req.getRequestDispatcher("/WEB-INF/todo/read.jsp").forward(req,resp);
    }
}
