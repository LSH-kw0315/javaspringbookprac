package org.zerock.jwtprac.repository.search;

import org.springframework.data.domain.Page;
import org.zerock.jwtprac.dto.PageRequestDTO;
import org.zerock.jwtprac.dto.TodoDTO;

public interface TodoSearch {

    Page<TodoDTO> list(PageRequestDTO pageRequestDTO);
}
