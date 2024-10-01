package org.zerock.jwtprac.service;

import jakarta.transaction.Transactional;
import org.zerock.jwtprac.domain.Todo;
import org.zerock.jwtprac.dto.PageRequestDTO;
import org.zerock.jwtprac.dto.PageResponseDTO;
import org.zerock.jwtprac.dto.TodoDTO;

@Transactional
public interface TodoService {
    Long register(TodoDTO todoDTO);
    TodoDTO read(Long tno);

    PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO);

    void remove(Long tno);
    void modify(TodoDTO todoDTO);
}
