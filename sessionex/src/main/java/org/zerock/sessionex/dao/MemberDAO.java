package org.zerock.sessionex.dao;

import lombok.Cleanup;
import org.zerock.sessionex.domain.MemberVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MemberDAO {

    public MemberVO getWithPassword(String mid,String mpw) throws Exception{
        String query="select * from tbl_member where mid=? and mpw=?";

        MemberVO vo=null;

        @Cleanup Connection c=ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement p=c.prepareStatement(query);

        p.setString(1,mid);
        p.setString(2,mpw);

        @Cleanup ResultSet resultSet=p.executeQuery();

        resultSet.next();

        vo=MemberVO.builder()
                .mname(resultSet.getString("mname"))
                .mid(resultSet.getString("mid"))
                .mpw(resultSet.getString("mpw"))
                .build();

        return vo;

    }

    public void updateUUID(String mid,String uuid) throws Exception{
        String sql="update tbl_member set uuid=? where mid=?";

        @Cleanup Connection c=ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement p=c.prepareStatement(sql);

        p.setString(1,uuid);
        p.setString(2,mid);

        p.executeUpdate();

    }

    public MemberVO selectUUID(String uuid) throws Exception{
        String query="select mid,mpw,mname from tbl_member where uuid=?";
        @Cleanup Connection c=ConnectionUtil.INSTANCE.getConnection();
        @Cleanup PreparedStatement p=c.prepareStatement(query);

        p.setString(1,uuid);

        @Cleanup ResultSet resultSet=p.executeQuery();

        resultSet.next();

        return MemberVO.builder()
                .mid(resultSet.getString("mid"))
                .mpw(resultSet.getString("mpw"))
                .mname(resultSet.getString("mname"))
                .build();
    }
}
