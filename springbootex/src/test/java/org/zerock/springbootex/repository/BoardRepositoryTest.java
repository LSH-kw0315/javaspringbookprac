package org.zerock.springbootex.repository;


import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.zerock.springbootex.domain.Board;
import org.zerock.springbootex.dto.BoardListAllDTO;
import org.zerock.springbootex.dto.BoardListReplyCountDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@Log4j2
@SpringBootTest
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void testInsert(){
        IntStream.rangeClosed(1,100).forEach(
                i->{
                    Board board=Board.builder()
                            .title("title..."+i)
                            .content("content..."+i)
                            .writer("user"+(i%10))
                            .build();
                    Board result=boardRepository.save(board);
                    log.info("BNO:"+result.getBno());
                }
        );
    }

    @Test
    public void testSelect(){
        Long bno=100L;

        Optional<Board> result=boardRepository.findById(bno);

        Board board=result.orElseThrow();

        log.info(board);
    }

    @Test
    public void testUpdate(){
        Long bno=100L;
        Optional<Board> result=boardRepository.findById(bno);

        Board board=result.orElseThrow();

        board.change("update..title 100","update content 100");

        boardRepository.save(board);
    }

    @Test
    public void testDelete(){
        Long bno=1L;
        boardRepository.deleteById(bno);
    }

    @Test
    public void testPaging(){
        Pageable pageable= PageRequest.of(0,10, Sort.by("bno").descending());
        Page<Board> result=boardRepository.findAll(pageable);

        log.info("total count:"+result.getTotalElements());
        log.info("total page:"+result.getTotalPages());
        log.info("page number:"+result.getNumber());
        log.info("page size:"+result.getSize());

        List<Board> todoList=result.getContent();

        todoList.forEach(i->log.info(i));
    }

    @Test
    public void testSearch1(){
        Pageable pageable=PageRequest.of(1,10,Sort.by("bno").descending());

        boardRepository.search1(pageable);
    }

    @Test
    public void testSearchAll(){
        Pageable pageable=PageRequest.of(0,10,Sort.by("bno").descending());

        Page<Board> result=boardRepository.searchAll(new String[]{},"",pageable);

        log.info(result.getTotalPages());
        log.info(result.getSize());
        log.info(result.getNumber());
        log.info(result.hasPrevious()+":"+result.hasNext());

        result.getContent().forEach(board->log.info(board));

    }

    @Test
    public void testSearchReply(){
        Pageable pageable=PageRequest.of(0,10,Sort.by("bno").descending());

        Page<BoardListReplyCountDTO> result=boardRepository.searchWithReplyCount(new String[]{"t","c","w"},"1",pageable);

        log.info(result.getTotalPages());
        log.info(result.getSize());
        log.info(result.getNumber());
        log.info(result.hasPrevious()+":"+result.hasNext());

        result.getContent().forEach(board->log.info(board));

    }

    @Test
    public void testInsertithImage(){
        Board board=Board.builder()
                .title("Image test")
                .content("범부파일 테스트")
                .writer("tester")
                .build();

        for(int i=0;i<3;i++){
            board.addImage(UUID.randomUUID().toString(),"file"+i+".jpg");
        }

        boardRepository.save(board);

    }

    @Test
    public void testReadWithImage(){
        Optional<Board> result=boardRepository.findByIdWithImage(1L);

        Board board=result.orElseThrow();

        log.info(board);
        log.info(board.getImageSet());
    }

    @Test
    public void testModifyImages(){

        Optional<Board> result=boardRepository.findByIdWithImage(1L);

        Board board=result.orElseThrow();

        board.clearImage();

        for (int i=0;i<2;i++){
            board.addImage(UUID.randomUUID().toString(),"updateFile"+i+".jpg");
        }

        boardRepository.save(board);


    }

    @Test
    @Transactional
    @Commit
    public void testRemoveAll(){
        Long bno=1L;
        replyRepository.deleteByBoard_Bno(bno);
        boardRepository.deleteById(bno);
    }

    @Test
    public void testInsertAll(){

        for(int i=0;i<100;i++){

            Board board=Board.builder()
                    .title("Title.."+i)
                    .writer("writer.."+i)
                    .content("content..."+i)
                    .build();

            for(int j=0;j<3;j++){
                if(i%5==0){
                    break;
                }
                board.addImage(UUID.randomUUID().toString(),i+"file"+j+".jpg");
            }
            boardRepository.save(board);
        }

    }

    @Transactional
    @Test
    public void testSearchImageReplyCount(){

        Pageable pageable= PageRequest.of(0,10, Sort.by("bno").descending());
        Page<BoardListAllDTO> result=boardRepository.searchWithAll(null,null,pageable);

        log.info("---------------");
        log.info(result.getTotalElements());

        result.getContent().forEach(i->log.info(i));
    }
}
