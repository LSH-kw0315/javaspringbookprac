package org.zerock.sessionex.controller;

import lombok.extern.log4j.Log4j2;
import org.zerock.sessionex.dto.TodoDTO;
import org.zerock.sessionex.service.TodoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Log4j2
@WebServlet(name="todoModifyController",value = "/todo/modify")
public class TodoModifyController extends HttpServlet {

    private TodoService todoService=TodoService.INSTANCE;
    private final DateTimeFormatter DATETIMEFORMATTER=DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            Long tno=Long.parseLong(req.getParameter("tno"));
            TodoDTO dto=todoService.get(tno);
            req.setAttribute("dto",dto);
            req.getRequestDispatcher("/WEB-INF/todo/modify.jsp").forward(req,resp);

        }catch (Exception e){
            log.error(e.getMessage());
            throw new ServletException("modify get... error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String finishedstr=req.getParameter("finished");

        TodoDTO dto=TodoDTO.builder()
                .tno(Long.parseLong(req.getParameter("tno")))
                .title(req.getParameter("title"))
                .dueDate(LocalDate.parse(req.getParameter("dueDate"),DATETIMEFORMATTER))
                .finished(finishedstr!=null && finishedstr.equals("on"))
                .build();

        log.info("/todo/modify POST..");
        log.info(dto);

        try{
            todoService.modify(dto);
        }catch (Exception e){
            e.printStackTrace();
        }

        resp.sendRedirect("/todo/list");
    }
}
