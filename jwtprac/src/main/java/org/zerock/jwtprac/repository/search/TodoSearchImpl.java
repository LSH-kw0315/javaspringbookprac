package org.zerock.jwtprac.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.jwtprac.domain.QTodo;
import org.zerock.jwtprac.domain.Todo;
import org.zerock.jwtprac.dto.PageRequestDTO;
import org.zerock.jwtprac.dto.TodoDTO;

import java.util.List;

public class TodoSearchImpl extends QuerydslRepositorySupport implements TodoSearch{

    public TodoSearchImpl(){
        super(Todo.class);
    }

    @Override
    public Page<TodoDTO> list(PageRequestDTO pageRequestDTO) {
        QTodo todo=QTodo.todo;
        JPQLQuery<Todo> query=from(todo);
        if(pageRequestDTO.getFrom()!=null && pageRequestDTO.getTo()!=null){
            BooleanBuilder fromBuilder=new BooleanBuilder();
            fromBuilder.and(todo.dueDate.goe(pageRequestDTO.getFrom()));
            fromBuilder.and(todo.dueDate.loe(pageRequestDTO.getTo()));

            query.where(fromBuilder);
        }

        if(pageRequestDTO.getCompleted()!=null){
            query.where(todo.complete.eq(pageRequestDTO.getCompleted()));
        }

        if(pageRequestDTO.getKeyword()!=null){
            query.where(todo.title.contains(pageRequestDTO.getKeyword()));
        }

        this.getQuerydsl().applyPagination(pageRequestDTO.getPageable("tno"),query);

        JPQLQuery<TodoDTO> dtojpqlQuery=
                query.select(
                        Projections.bean(TodoDTO.class,
                                todo.tno,
                                todo.title,
                                todo.dueDate,
                                todo.complete,
                                todo.writer)
                );

        List<TodoDTO> dtoList=dtojpqlQuery.fetch();
        long cnt=dtojpqlQuery.fetchCount();

        return new PageImpl<>(dtoList,pageRequestDTO.getPageable("tno"),cnt);

    }
}
