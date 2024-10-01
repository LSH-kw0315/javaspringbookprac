package org.zerock.springbootex.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.springbootex.dto.BoardListAllDTO;
import org.zerock.springbootex.dto.PageRequestDTO;
import org.zerock.springbootex.dto.PageResponseDTO;
import org.zerock.springbootex.dto.ReplyDTO;

public interface ReplyService {

    Long register(ReplyDTO dto);
    ReplyDTO read(Long rno);
    void update(ReplyDTO dto);
    void delete(Long rno);
    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);

    PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);
}
