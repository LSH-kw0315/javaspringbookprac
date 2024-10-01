package org.zerock.demo.service;

import com.sun.tools.javac.comp.Todo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.zerock.demo.domain.TodoVO;
import org.zerock.demo.dto.PageRequestDTO;
import org.zerock.demo.dto.PageResponseDTO;
import org.zerock.demo.dto.TodoDTO;
import org.zerock.demo.mapper.TodoMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoMapper todoMapper;
    private final ModelMapper modelMapper;

    @Override
    public void Register(TodoDTO todoDTO) {
        log.info(modelMapper);
        TodoVO vo=modelMapper.map(todoDTO,TodoVO.class);
        todoMapper.insert(vo);
        log.info(vo);
    }

    @Override
    public PageResponseDTO<TodoDTO> getList(PageRequestDTO pageRequestDTO) {
        List<TodoDTO> dtos=todoMapper.selectList(pageRequestDTO).stream()
                .map(vo->modelMapper.map(vo,TodoDTO.class))
                .collect(Collectors.toList());
        int total=todoMapper.getCount(pageRequestDTO);

        PageResponseDTO<TodoDTO> pageResponseDTO=PageResponseDTO
                .<TodoDTO>withAll()
                .total(total)
                .dtoList(dtos)
                .pageRequestDTO(pageRequestDTO)
                .build();
        return pageResponseDTO;
    }

    @Override
    public TodoDTO getOne(Long tno) {
        return modelMapper.map(todoMapper.selectOne(tno),TodoDTO.class);
    }

    @Override
    public void delete(Long tno) {
        todoMapper.delete(tno);
    }

    @Override
    public void update(TodoDTO dto) {
        todoMapper.update(modelMapper.map(dto,TodoVO.class));
    }
}
