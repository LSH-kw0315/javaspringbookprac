package org.zerock.demo.service;

import org.zerock.demo.dto.PageRequestDTO;
import org.zerock.demo.dto.PageResponseDTO;
import org.zerock.demo.dto.TodoDTO;

import java.util.List;

public interface TodoService {

    void Register(TodoDTO todoDTO);
    PageResponseDTO<TodoDTO> getList(PageRequestDTO pageRequestDTO);

    TodoDTO getOne(Long tno);

    void delete(Long tno);

    void update(TodoDTO dto);
}
