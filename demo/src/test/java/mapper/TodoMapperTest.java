package mapper;

import com.sun.tools.javac.comp.Todo;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.zerock.demo.domain.TodoVO;
import org.zerock.demo.dto.PageRequestDTO;
import org.zerock.demo.mapper.TodoMapper;

import java.time.LocalDate;
import java.util.List;

@Log4j2
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/root-context.xml")
public class TodoMapperTest {

    @Autowired(required = false)
    private TodoMapper todoMapper;

    @Test
    public void testGetTime(){
        log.info(todoMapper.getTime());
    }

    @Test
    public void testInsert(){
        TodoVO todoVO=TodoVO.builder()
                .title("스프링 테스트")
                .dueDate(LocalDate.of(2022,10,10))
                .writer("user00")
                .build();
        todoMapper.insert(todoVO);
    }

    @Test
    public void testSelectAll(){
        List<TodoVO> vos=todoMapper.selectAll();
        vos.forEach(vo->log.info(vo));
    }

    @Test
    public void testSelectOne(){
        TodoVO vo=todoMapper.selectOne(3L);
        log.info(vo);
    }

    @Test
    public void testSelectList(){
        PageRequestDTO pageRequestDTO=PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        List<TodoVO> voList=todoMapper.selectList(pageRequestDTO);

        voList.forEach(i->log.info(i));
    }

    @Test
    public void testGetCount(){

        PageRequestDTO pageRequestDTO= PageRequestDTO.builder().page(1).size(10).build();

        log.info(todoMapper.getCount(pageRequestDTO));

    }

    @Test
    public void testSearch(){
        PageRequestDTO pageRequestDTO= PageRequestDTO.builder()
                .page(1)
                .size(10)
                .types(new String[]{"t","w"})
                .keyword("Test")
                .build();

        List<TodoVO> voList=todoMapper.selectList(pageRequestDTO);
        voList.forEach(i->log.info(i));
        log.info(todoMapper.getCount(pageRequestDTO));
    }
}
