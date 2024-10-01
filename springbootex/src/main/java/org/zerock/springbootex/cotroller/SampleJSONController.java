package org.zerock.springbootex.cotroller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.log4j.Log4j2;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Log4j2
@RestController
public class SampleJSONController {

    @GetMapping("/api/helloArr")
    public String[] helloArr(){
        log.info("hello Arr");

        return new String[]{"aaa","bbb","ccc"};
    }

}
