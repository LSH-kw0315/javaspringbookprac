package org.zerock.springbootex.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.springbootex.domain.Board;
import org.zerock.springbootex.dto.BoardDTO;
import org.zerock.springbootex.dto.BoardListAllDTO;
import org.zerock.springbootex.dto.BoardListReplyCountDTO;

public interface BoardSearch {
    Page<Board> search1(Pageable pageable);
    Page<Board> searchAll(String[] types,String keyword,Pageable pageable);

    Page<BoardListReplyCountDTO>  searchWithReplyCount(String[] types,String keyword,Pageable pageable);

    Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable);
}
