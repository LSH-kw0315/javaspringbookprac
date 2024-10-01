package org.zerock.chap1_practice.todo;

import org.zerock.chap1_practice.todo.dto.TodoDTO;
import org.zerock.chap1_practice.todo.service.TodoService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name="TodoListController",urlPatterns = "/todo/list")
public class TodoListController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       List<TodoDTO> dotoList= TodoService.INSTANCE.getList();

       req.setAttribute("List",dotoList);
       req.getRequestDispatcher("/WEB-INF/todo/list.jsp").forward(req,resp);
    }
}
