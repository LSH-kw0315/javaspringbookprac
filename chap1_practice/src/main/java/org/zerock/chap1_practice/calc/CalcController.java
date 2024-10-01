package org.zerock.chap1_practice.calc;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="CalcController",urlPatterns = "/calc/makeResult")
public class CalcController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String num1=req.getParameter("left");
        String num2=req.getParameter("right");

        System.out.println("num1:"+num1);
        System.out.println("num2:"+num2);

        resp.sendRedirect("/index");
    }
}
