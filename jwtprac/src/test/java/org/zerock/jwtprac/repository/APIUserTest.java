package org.zerock.jwtprac.repository;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.jwtprac.domain.APIUser;
import org.zerock.jwtprac.repository.APIUserRepository;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class APIUserTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private APIUserRepository apiUserRepository;

    @Test
    public void testInsert(){
        IntStream.rangeClosed(1,100).forEach(
                i->apiUserRepository.save(
                 APIUser.builder()
                         .mid("apiuser"+i)
                         .mpw(passwordEncoder.encode("1111"))
                         .build()
                )
        );
    }
}
