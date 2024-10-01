package org.zerock.jwtprac.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.zerock.jwtprac.dto.PageRequestDTO;
import org.zerock.jwtprac.dto.PageResponseDTO;
import org.zerock.jwtprac.dto.TodoDTO;
import org.zerock.jwtprac.service.TodoService;

import java.util.Map;

@RestController
@RequestMapping("/api/todo")
@Log4j2
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @PostMapping(value = "/",consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Long> register(@RequestBody TodoDTO dto){
        log.info(dto);
        Long tno=todoService.register(dto);
        return Map.of("tno",tno);
    }

    @GetMapping("/{tno}")
    public TodoDTO read(@PathVariable("tno") Long tno){
        log.info("read tno:"+tno);

        return todoService.read(tno);
    }

    @GetMapping(value = "/list",produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO){
        return todoService.list(pageRequestDTO);
    }

    @DeleteMapping(value="/{tno}")
    public Map<String,String> delete(@PathVariable Long tno){
        todoService.remove(tno);
        return Map.of("result","success");
    }

    @PutMapping(value = "/{tno}")
    public Map<String,String> modify(@PathVariable Long tno,@RequestBody TodoDTO todoDTO){
        todoDTO.setTno(tno);

        todoService.modify(todoDTO);

        return Map.of("result","success");
    }
}
