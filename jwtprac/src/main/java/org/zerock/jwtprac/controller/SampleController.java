package org.zerock.jwtprac.controller;


import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SampleController {

    @Operation(summary = "Sample GET doA")
    @GetMapping("/doA")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<String> doA(){
        return Arrays.asList("AAA","BBB","CCC");
    }

    @Operation(summary = "Sample GET doB")
    @GetMapping("/doB")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<String> doB(){
        return Arrays.asList("aaa","bbb","ccc");
    }
}
