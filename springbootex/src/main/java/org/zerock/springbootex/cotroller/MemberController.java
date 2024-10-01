package org.zerock.springbootex.cotroller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.springbootex.dto.MemberJoinDTO;
import org.zerock.springbootex.service.MemberService;

@Controller
@Log4j2
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public void loginGET(String error,String logout){
        log.info("login Get..");
        log.info("logout:"+logout);

        if(logout!=null){
            log.info("log out..");
        }
    }

    @GetMapping("/join")
    public void joinGet(){
        log.info("join...GET");

    }

    @PostMapping("/join")
    public String joinPost(MemberJoinDTO dto, RedirectAttributes redirectAttributes){
        log.info("join...POST");
        log.info("memberJoinDTO");

        try{
            memberService.join(dto);
        }catch (MemberService.MidExistException e){
            redirectAttributes.addFlashAttribute("error","mid");
            return "redirect:/member/join";
        }

        redirectAttributes.addFlashAttribute("result","success");
        return "redirect:/board/list";
    }

}
