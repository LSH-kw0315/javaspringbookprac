package org.zerock.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.demo.dto.PageRequestDTO;
import org.zerock.demo.dto.TodoDTO;
import org.zerock.demo.service.TodoService;

import javax.validation.Valid;

@Controller
@Log4j2
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {


    private final TodoService todoService;

    @RequestMapping("/list")
    public void list(@Valid PageRequestDTO pageRequestDTO,BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            pageRequestDTO=PageRequestDTO.builder().page(1).size(10).build();
        }
        log.info("todo list...");
        model.addAttribute("pageResponseDTO",todoService.getList(pageRequestDTO));
    }

    //@RequestMapping(value = "/register",method = RequestMethod.GET)
    @GetMapping("/register")
    public void registerGet(){
        log.info("GET todo register...");
    }

    @PostMapping("/register")
    public String registerPost(@Valid TodoDTO todoDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        log.info("POST todo register...");
        log.info(todoDTO);

        if(bindingResult.hasErrors()){
            log.info("invalid value error");
            redirectAttributes.addFlashAttribute("errors",bindingResult.getAllErrors());
            return "redirect:/todo/register";
        }

        todoService.Register(todoDTO);

        return "redirect:/todo/list";
    }

    @GetMapping({"/read","/modify"})
    public void read(Long tno,PageRequestDTO pageRequestDTO,Model model){
        TodoDTO dto=todoService.getOne(tno);
        model.addAttribute("dto",dto);
    }

    @PostMapping("/remove")
    public String remove(Long tno,PageRequestDTO pageRequestDTO,RedirectAttributes redirectAttributes){
        log.info("--------");
        log.info("tno:"+tno);
        todoService.delete(tno);

        return "redirect:/todo/list?"+pageRequestDTO.getLink();
    }

    @PostMapping("/modify")
    public String modify(@Valid TodoDTO todoDTO,
                         PageRequestDTO pageRequestDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            log.info("has error..");
            redirectAttributes.addFlashAttribute("errors",bindingResult.getAllErrors());
            redirectAttributes.addAttribute("tno",todoDTO.getTno());

            return "redirect:/todo/modify";
        }

        log.info(todoDTO);

        todoService.update(todoDTO);
        redirectAttributes.addAttribute("tno",todoDTO.getTno());
        return "redirect:/todo/read";
    }





}
