package org.zerock.jwtprac.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.zerock.jwtprac.domain.Todo;
import org.zerock.jwtprac.dto.PageRequestDTO;
import org.zerock.jwtprac.dto.PageResponseDTO;
import org.zerock.jwtprac.dto.TodoDTO;
import org.zerock.jwtprac.repository.TodoRepository;

import java.util.Optional;


@RequiredArgsConstructor
@Service
@Log4j2
public class TodoServiceImpl implements TodoService{

    private final TodoRepository todoRepository;
    private final ModelMapper modelMapper;
    @Override
    public Long register(TodoDTO todoDTO) {
        return todoRepository.save(modelMapper.map(todoDTO,Todo.class)).getTno();
    }

    @Override
    public TodoDTO read(Long tno) {
        Optional<Todo> result=todoRepository.findById(tno);

        return modelMapper.map(result.orElseThrow(),TodoDTO.class);
    }

    @Override
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO) {
        Page<TodoDTO> page=todoRepository.list(pageRequestDTO);

        return PageResponseDTO.<TodoDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(page.toList())
                .total((int)page.getTotalElements())
                .build();
    }

    @Override
    public void remove(Long tno) {
        todoRepository.deleteById(tno);
    }

    @Override
    public void modify(TodoDTO todoDTO) {
        Optional<Todo> result=todoRepository.findById(todoDTO.getTno());

        Todo todo=result.orElseThrow();

        todo.changeComplete(todoDTO.isComplete());
        todo.changeTitle(todoDTO.getTitle());
        todo.changeDueDate(todoDTO.getDueDate());

        todoRepository.save(todo);
    }
}
