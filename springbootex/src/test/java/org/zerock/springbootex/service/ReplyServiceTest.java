package org.zerock.springbootex.service;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.springbootex.dto.PageRequestDTO;
import org.zerock.springbootex.dto.ReplyDTO;

@SpringBootTest
@Log4j2
public class ReplyServiceTest {
    @Autowired
    private ReplyService replyService;

    @Test
    public void testRegister(){
        ReplyDTO replyDTO=ReplyDTO.builder()
                .bno(105L)
                .replyText("Test Reply")
                .replyer("replyer1")
                .build();

        log.info(replyService.register(replyDTO));
    }

    @Test
    public void testList(){

        PageRequestDTO pageRequestDTO=
                PageRequestDTO.builder()
                        .page(0)
                        .size(10)
                        .build();
        replyService.getListOfBoard(100L,pageRequestDTO).getDtoList()
                .stream()
                .forEach(i->log.info(i));
    }
}
