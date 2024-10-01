package org.zerock.springbootex.service;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.springbootex.domain.Board;
import org.zerock.springbootex.domain.BoardImage;
import org.zerock.springbootex.dto.BoardDTO;
import org.zerock.springbootex.dto.BoardImageDTO;
import org.zerock.springbootex.dto.PageRequestDTO;
import org.zerock.springbootex.dto.PageResponseDTO;

import java.util.Arrays;
import java.util.UUID;


@Log4j2
@SpringBootTest
public class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister(){
        log.info(boardService.getClass().getName());

        BoardDTO boardDTO= BoardDTO.builder()
                .title("Sample title")
                .content("Sample Content")
                .writer("user00")
                .build();

        Long bno=boardService.register(boardDTO);

        log.info("bno:"+bno);

    }

    @Test
    public void testModify(){
        BoardDTO boardDTO=BoardDTO.builder()
                .bno(101L)
                .title("Updated...101")
                .content("Updated...Content 101...")
                .build();

        boardService.modify(boardDTO);
    }

    @Test
    public void testList(){

        PageRequestDTO pageRequestDTO= PageRequestDTO.builder()
                .page(1)
                .size(10)
                .keyword("1")
                .type("twc")
                .build();

        PageResponseDTO<BoardDTO> responseDTO=boardService.list(pageRequestDTO);

        log.info(responseDTO);
    }

    @Test
    public void testRegisterWithImages(){
        log.info(boardService.getClass().getName());

        BoardDTO boardDTO=BoardDTO.builder()
                .content("Sample Content...")
                .title("Sample Title...")
                .writer("Sample01")
                .build();

        boardDTO.setFileNames(
                Arrays.asList(
                        UUID.randomUUID()+"_aaa.jpg",
                        UUID.randomUUID()+"_bbb.jpg",
                        UUID.randomUUID()+"_ccc.jpg"
                )
        );

        log.info("bno:"+boardService.register(boardDTO));
    }

    @Test
    public void testReadAll(){

        Long bno=101L;

        BoardDTO boardDTO=boardService.readOne(bno);

        log.info(boardDTO);

        for(String fileName:boardDTO.getFileNames()){
            log.info(fileName);
        }
    }

    @Test
    public void testModifyAll(){
        Long bno=101L;

        BoardDTO boardDTO= BoardDTO.builder()
                .bno(bno)
                .title("updated...101")
                .content("Updated content...101")
                .build();

        boardDTO.setFileNames(Arrays.asList(UUID.randomUUID()+"_zzz.jpg"));
        boardService.modify(boardDTO);
    }

    @Test
    public void testRemoveAll(){
        Long bno=102L;
        boardService.remove(bno);
    }

    @Test
    public void testListAll(){
        PageRequestDTO pageRequestDTO=
                PageRequestDTO.builder()
                        .page(1)
                        .size(10)
                        .build();
        boardService.listWithAll(pageRequestDTO)
                .getDtoList().forEach(
                        boardListAllDTO ->
                        {
                            log.info(boardListAllDTO.getBno()+":"+boardListAllDTO.getTitle());

                            if(boardListAllDTO.getBoardImages()!=null){
                                for(BoardImageDTO image:boardListAllDTO.getBoardImages()){
                                    log.info(image);
                                }
                            }

                            log.info("---------------------");
                        }
                );
    }

}
