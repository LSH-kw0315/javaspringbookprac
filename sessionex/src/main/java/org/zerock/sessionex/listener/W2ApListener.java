package org.zerock.sessionex.listener;


import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
@Log4j2
public class W2ApListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
       log.info("Server Started!!");
        log.info("Server Started!!");
        log.info("Server Started!!");

        sce.getServletContext().setAttribute("appName","W2");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("Server ended!!");
        log.info("Server ended!!");
        log.info("Server ended!!");
    }
}
