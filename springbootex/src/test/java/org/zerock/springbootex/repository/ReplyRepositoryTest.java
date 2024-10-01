package org.zerock.springbootex.repository;


import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.springbootex.domain.Board;
import org.zerock.springbootex.domain.Reply;

@SpringBootTest
@Log4j2
public class ReplyRepositoryTest {

    @Autowired
    ReplyRepository replyRepository;

    @Test
    public void testInsert(){
        Long bno=1L;

        Board board=Board.builder().bno(bno).build();

        replyRepository.save(
                Reply.builder()
                        .board(board)
                        .replyer("replyer1")
                        .replyText("섹스")
                        .build()
        );

    }

    @Test
    public void testListOfReply(){
        Long bno=100L;

        Pageable pageable= PageRequest.of(0,10, Sort.by("rno").descending());

        Page<Reply> listOfReply=replyRepository.listOfReply(bno,pageable);

        listOfReply.getContent().stream().forEach(i->log.info(i));
    }


}
