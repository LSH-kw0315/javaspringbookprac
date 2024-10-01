package org.zerock.sessionex.service;

import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.zerock.sessionex.dao.TodoDAO;
import org.zerock.sessionex.domain.TodoVO;
import org.zerock.sessionex.dto.TodoDTO;
import org.zerock.sessionex.util.MapperUtil;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public enum TodoService {
    INSTANCE;

    private TodoDAO todoDAO;
    private ModelMapper modelMapper;

    TodoService(){
        todoDAO=new TodoDAO();
        modelMapper= MapperUtil.INSTANCE.get();
    }

    public void register(TodoDTO todoDTO) throws Exception{
        TodoVO todoVO=modelMapper.map(todoDTO,TodoVO.class);

        log.info(todoVO);

        todoDAO.insert(todoVO);
    }

    public List<TodoDTO> listAll() throws Exception{
        List<TodoVO> list=todoDAO.selectAll();

        log.info("VoList...");
        log.info(list);

        List<TodoDTO> dtoList=list.stream()//vo 스트림
                .map(vo->modelMapper.map(vo,TodoDTO.class))//vo 스트림에서 dto 스트림으로 변환
                .collect(Collectors.toList());//dto 스트림을 dto list로 변환

        return dtoList;

    }

    public TodoDTO get(Long tno) throws Exception{
        log.info("tno:"+tno);
        TodoVO vo=todoDAO.selectOne(tno);
        return modelMapper.map(vo,TodoDTO.class);
    }

    public void remove(Long tno) throws Exception{
        log.info("tno:"+tno);
        todoDAO.deleteOne(tno);
    }

    public void modify(TodoDTO todoDTO) throws Exception{
        log.info("todoDTO:"+todoDTO);
        TodoVO todoVO=modelMapper.map(todoDTO,TodoVO.class);
        todoDAO.updateOne(todoVO);
    }
}
