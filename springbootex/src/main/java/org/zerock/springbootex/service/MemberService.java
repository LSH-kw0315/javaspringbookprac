package org.zerock.springbootex.service;

import org.springframework.data.jpa.repository.EntityGraph;
import org.zerock.springbootex.dto.MemberJoinDTO;

public interface MemberService {
    static class MidExistException extends Exception{

    }

    void join(MemberJoinDTO dto) throws MidExistException;

}
