package org.zerock.springbootex.cotroller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.engine.jdbc.mutation.spi.BindingGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.springbootex.dto.*;
import org.zerock.springbootex.service.BoardService;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/board") //board경로로 오는 건 일단 이 컨트롤러를 호출하게 된다
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        //PageResponseDTO<BoardDTO> responseDTO=boardService.list(pageRequestDTO);

        PageResponseDTO<BoardListAllDTO> responseDTO=boardService.listWithAll(pageRequestDTO);

        log.info(responseDTO);

        model.addAttribute("responseDTO",responseDTO);

    }

    @GetMapping("/register")
    @PreAuthorize("hasRole('USER')")
    public void registerGET(){

    }

    @PostMapping("/register")
    public String registerPost(@Valid BoardDTO boardDTO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes){
        log.info("board POST register");

        if(bindingResult.hasErrors()){
            log.info("has error");
            redirectAttributes.addFlashAttribute("errors",bindingResult.getAllErrors());
            return "redirect:/board/register";
        }

        log.info(boardDTO);

        Long bno = boardService.register(boardDTO);

        redirectAttributes.addFlashAttribute("result",bno);

        return "redirect:/board/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping({"/read","/modify"})
    public void read(Long bno, PageRequestDTO pageRequestDTO,Model model){
        BoardDTO boardDTO=boardService.readOne(bno);

        log.info(boardDTO);

        model.addAttribute("dto",boardDTO);
    }

    @PostMapping("/modify")
    @PreAuthorize("principal.username==#boardDTO.writer")
    public String modify(PageRequestDTO pageRequestDTO,
                         @Valid BoardDTO boardDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes){
        log.info("board modify post...."+boardDTO);

        if(bindingResult.hasErrors()){
            log.info("has error...");
            String link= pageRequestDTO.getLink();
            redirectAttributes.addFlashAttribute("errors",bindingResult.getAllErrors());
            redirectAttributes.addAttribute("bno",boardDTO.getBno());
            return "redirect:/board/modify?"+link;
        }
        log.info("before modify");
        boardService.modify(boardDTO);
        log.info("after modify");
        redirectAttributes.addFlashAttribute("result","modified");
        redirectAttributes.addAttribute("bno",boardDTO.getBno());

        return "redirect:/board/read";
    }

    @PostMapping("/remove")
    @PreAuthorize("principal.username==#dto.writer")
    public String remove(BoardDTO dto, RedirectAttributes redirectAttributes){
        log.info("remove post..."+dto.getBno());
        boardService.remove(dto.getBno());

        log.info(dto.getFileNames());
        List<String> fileList=dto.getFileNames();

        if(fileList!=null&& fileList.size()>0){
            removeFiles(fileList);
        }

        redirectAttributes.addFlashAttribute("result","removed");
        return "redirect:/board/list";
    }

    public void removeFiles(List<String> fileList){
        for(String file:fileList){
            Resource resource=new FileSystemResource(uploadPath+ File.separator+file);
            String resourceName=resource.getFilename();

            try{
                String contentType= Files.probeContentType(resource.getFile().toPath());

                resource.getFile().delete();

                if(contentType.startsWith("image")){
                    File thumbnailFile=new File(uploadPath+File.separator+"s_"+file);
                    thumbnailFile.delete();
                }
            }catch (Exception e){
                log.error(e.getMessage());
            }
        }
    }
}
