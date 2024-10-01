package org.zerock.springbootex.cotroller;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.zerock.springbootex.dto.PageRequestDTO;
import org.zerock.springbootex.dto.PageResponseDTO;
import org.zerock.springbootex.dto.ReplyDTO;
import org.zerock.springbootex.service.ReplyService;

import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
@RequestMapping("/api/replies")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping(value = "/",consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "replies post",description = "POST 방식으로 댓글 등록")
    public Map<String,Long> register(@Valid @RequestBody ReplyDTO replyDTO,
                                                     BindingResult bindingResult) throws BindException {
        log.info(replyDTO);

        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }

        Map<String,Long> resultMap= new HashMap<>();
        Long rno=replyService.register(replyDTO);

        resultMap.put("rno",rno);

        return resultMap;
    }

    @Operation(summary = "Replies of Board",description = "GET 방식으로 게시물 댓글목록")
    @GetMapping(value = "/list/{bno}")
    public PageResponseDTO<ReplyDTO> getList(@PathVariable("bno") Long bno,
                                             PageRequestDTO pageRequestDTO){
        return replyService.getListOfBoard(bno,pageRequestDTO);
    }

    @Operation(summary = "Read Reply",description = "GET 방식으로 특정 댓글 조회")
    @GetMapping(value="/{rno}")
    public ReplyDTO getReplyDTO(@PathVariable("rno")Long rno){
        return replyService.read(rno);
    }

    @Operation(summary = "Delete Reply",description = "DELETE 방식으로 삭제")
    @DeleteMapping(value = "/{rno}")
    public Map<String,Long> remove(@PathVariable("rno") Long rno){
        replyService.delete(rno);

        Map<String,Long> resultMap=new HashMap<>();

        resultMap.put("rno",rno);

        return resultMap;
    }

    @Operation(summary = "Update Reply",description = "PUT 방식으로 수정")
    @PutMapping(value="/{rno}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Long> modify(@PathVariable("rno") Long rno,
                                   @RequestBody ReplyDTO replyDTO){
        replyDTO.setRno(rno);
        replyService.update(replyDTO);

        Map<String,Long> resultMap=new HashMap<>();
        resultMap.put("rno",rno);

        return resultMap;
    }
}
