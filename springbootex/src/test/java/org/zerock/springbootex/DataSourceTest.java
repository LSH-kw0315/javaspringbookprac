package org.zerock.springbootex;


import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootTest
@Log4j2
public class DataSourceTest {

    @Autowired
    private DataSource ds;

    @Test
    public void testConnection() throws Exception{

        @Cleanup Connection c=ds.getConnection();

        log.info(c);

        Assertions.assertNotNull(c);
    }
}
