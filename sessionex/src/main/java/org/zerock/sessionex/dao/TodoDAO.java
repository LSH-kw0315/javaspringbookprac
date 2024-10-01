package org.zerock.sessionex.dao;

import lombok.Cleanup;
import org.zerock.sessionex.domain.TodoVO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TodoDAO {

    public String getTime(){
        String now=null;

        try(Connection c=ConnectionUtil.INSTANCE.getConnection();
            PreparedStatement prepStmt=c.prepareStatement(
                    "select now()"
            );
            ResultSet resultSet =prepStmt.executeQuery();
        ){
            resultSet.next();

            now=resultSet.getString(1);
        }catch (Exception e){
            e.printStackTrace();
        }

        return now;
    }

    public String getTime2() throws  Exception{
        String now=null;

        @Cleanup Connection c=ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement prepStmt=c.prepareStatement(
                "select now()"
        );
        @Cleanup ResultSet resultSet =prepStmt.executeQuery();
        resultSet.next();
        now=resultSet.getString(1);

        return now;
    }

    public void insert(TodoVO vo) throws Exception{
        String sql="insert into tbl_todo (title, dueDate, finished) values (?,?,?)";

        @Cleanup Connection c=ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement prepStmt=c.prepareStatement(sql);

        prepStmt.setString(1,vo.getTitle());
        prepStmt.setDate(2,Date.valueOf(vo.getDueDate()));
        prepStmt.setBoolean(3,vo.isFinished());

        prepStmt.executeUpdate();
    }

    public List<TodoVO> selectAll() throws Exception{
        String sql="select * from tbl_todo";
        @Cleanup Connection c=ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement p=c.prepareStatement(sql);
        @Cleanup ResultSet list=p.executeQuery();

        List<TodoVO> result=new ArrayList<>();

        while(list.next()){
            TodoVO vo=TodoVO.builder()
                    .tno(list.getLong("tno"))
                    .title(list.getString("title"))
                    .dueDate(list.getDate("dueDate").toLocalDate())
                    .finished(list.getBoolean("finished"))
                    .build();
            result.add(vo);
        }

        return result;

    }

    public TodoVO selectOne(Long tno) throws Exception{
        String sql="select * from tbl_todo where tno=?";

        @Cleanup Connection c=ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement p=c.prepareStatement(sql);

        p.setLong(1,tno);

        @Cleanup ResultSet resultset=p.executeQuery();

        resultset.next();

        TodoVO vo=TodoVO.builder()
                .tno(resultset.getLong("tno"))
                .title(resultset.getString("title"))
                .dueDate(resultset.getDate("dueDate").toLocalDate())
                .finished(resultset.getBoolean("finished"))
                .build();

        return vo;

    }

    public void deleteOne(Long tno) throws Exception{
        String sql="delete from tbl_todo where tno=?";

        @Cleanup Connection c=ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement p=c.prepareStatement(sql);

        p.setLong(1,tno);
        p.executeUpdate();
    }

    public void updateOne(TodoVO vo) throws Exception{
        String sql="update tbl_todo set title=?, dueDate=?, finished=? where tno=?";

        @Cleanup Connection c =ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement p=c.prepareStatement(sql);

        p.setString(1,vo.getTitle());
        p.setDate(2,Date.valueOf(vo.getDueDate()));
        p.setBoolean(3,vo.isFinished());
        p.setLong(4,vo.getTno());

        p.executeUpdate();

    }
}
