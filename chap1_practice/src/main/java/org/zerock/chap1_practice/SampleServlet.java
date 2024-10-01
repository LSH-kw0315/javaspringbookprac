package org.zerock.chap1_practice;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="SampleServlet",urlPatterns ="/sample")
public class SampleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("sample Get "+this);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("sample init");
    }

    @Override
    public void destroy() {
        System.out.println("sample destroy");
    }
}
