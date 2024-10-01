package mapper;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.zerock.demo.dto.PageRequestDTO;
import org.zerock.demo.dto.PageResponseDTO;
import org.zerock.demo.dto.TodoDTO;
import org.zerock.demo.service.TodoService;

import java.time.LocalDate;

@Log4j2
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/root-context.xml")
public class TodoServiceTest {
    @Autowired
    private TodoService todoService;

    @Test
    public void testRegister(){
        TodoDTO dto=TodoDTO.builder()
                .title("Test...")
                .dueDate(LocalDate.now())
                .writer("user01")
                .build();
        todoService.Register(dto);
    }

    @Test
    public void testPaging(){
        PageRequestDTO pageRequestDTO= PageRequestDTO.builder()
                .size(10)
                .page(1)
                .build();
        PageResponseDTO<TodoDTO> pageResponseDTO=todoService.getList(pageRequestDTO);

        log.info(pageRequestDTO);

        pageResponseDTO.getDtoList().forEach(i->log.info(i));
    }
}
