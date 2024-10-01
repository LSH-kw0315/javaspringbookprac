package org.zerock.springbootex.service;


import org.zerock.springbootex.domain.Board;
import org.zerock.springbootex.dto.*;

import java.util.List;

public interface BoardService {
    Long register(BoardDTO dto);

    BoardDTO readOne(Long bno);

    void modify(BoardDTO dto);

    void remove(Long bno);

    PageResponseDTO<BoardDTO> list(PageRequestDTO dto);

    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCont(PageRequestDTO dto);

    PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO dto);

    default Board dtoToEntity(BoardDTO dto){
        Board board= Board.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();

        if(dto.getFileNames()!=null && dto.getFileNames().size()>0){
            dto.getFileNames().stream().forEach(
                    fileName->{
                        String[] arr=fileName.split("_");
                        board.addImage(arr[0],arr[1]);
                    }
            );

        }

        return board;

    }

    default BoardDTO entityToDTO(Board board){
        BoardDTO boardDTO=
                BoardDTO.builder()
                        .bno(board.getBno())
                        .title(board.getTitle())
                        .content(board.getContent())
                        .writer(board.getWriter())
                        .regDate(board.getRegDate())
                        .modDate(board.getModDate())
                        .build();

        List<String> fileList=
                board.getImageSet().stream()
                        .sorted()
                        .map(file->
                            file.getUuid()+"_"+file.getFileName()
                        )
                        .toList();

        boardDTO.setFileNames(fileList);

        return boardDTO;
    }
}
