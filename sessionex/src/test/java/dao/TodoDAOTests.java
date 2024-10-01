package dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zerock.sessionex.dao.TodoDAO;
import org.zerock.sessionex.domain.TodoVO;

import java.time.LocalDate;
import java.util.List;


public class TodoDAOTests {
    private TodoDAO todoDAO;

    @BeforeEach
    public void Ready(){
        todoDAO=new TodoDAO();
    }

    @Test
    public void testTime() throws Exception{
        System.out.println(todoDAO.getTime());
    }

    @Test
    public void testTime2() throws Exception{
        System.out.println(todoDAO.getTime2());
    }

    @Test
    public void testInsert() throws  Exception{
        TodoVO todoVO=TodoVO.builder()
                .title("Sample Title")
                .dueDate(LocalDate.of(2021,12,31))
                .build();
        todoDAO.insert(todoVO);
    }

    @Test
    public void testSelectAll() throws Exception{
        List<TodoVO> list=todoDAO.selectAll();

        list.forEach(i->System.out.println(i.toString()));
    }

    @Test
    public void testSelectOne() throws Exception{
        TodoVO vo=todoDAO.selectOne(3L);

        System.out.println(vo.toString());
    }

    @Test
    public void testDeleteOne() throws Exception{
        todoDAO.deleteOne(4L);

        List<TodoVO> list=todoDAO.selectAll();

        list.forEach(i->System.out.println(i.toString()));
    }

    @Test
    public void testUpdateOne() throws Exception{
        TodoVO vo=TodoVO.builder()
                .tno(3L)
                .title("update Test")
                .dueDate(LocalDate.of(2023,1,3))
                .finished(true)
                .build();
        todoDAO.updateOne(vo);
        List<TodoVO> list=todoDAO.selectAll();

        list.forEach(i->System.out.println(i.toString()));
    }

}
