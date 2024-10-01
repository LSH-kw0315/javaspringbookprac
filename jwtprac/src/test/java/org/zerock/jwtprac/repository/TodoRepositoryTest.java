package org.zerock.jwtprac.repository;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.zerock.jwtprac.domain.Todo;
import org.zerock.jwtprac.dto.PageRequestDTO;
import org.zerock.jwtprac.dto.TodoDTO;

import java.time.LocalDate;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void testInsert(){
        IntStream.rangeClosed(1,100).forEach(
                i->{
                    todoRepository.save(
                            Todo.builder()
                                    .title("Todo"+i)
                                    .dueDate(LocalDate.of(2024,(i%12)+1,(i%30)+1))
                                    .writer("user"+(i%10))
                                    .complete(false)
                                    .build()
                    );
                }
        );
    }

    @Test
    public void testSearch(){
        Page<TodoDTO> result=todoRepository.list(
                PageRequestDTO.builder()
                        .from(LocalDate.of(2024,1,1))
                        .to(LocalDate.of(2024,2,1))
                        .build()
        );

        result.forEach(todoDTO -> log.info(todoDTO));
    }
}
