package com.juno.guestbook.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.juno.guestbook.dto.GuestbookDTO;
import com.juno.guestbook.dto.PageRequestDTO;
import com.juno.guestbook.service.GuestbookService;

import javax.validation.Valid;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor //자동 주입을 위한 Annotation
public class GuestbookController {

    private final GuestbookService service; //final로 선언

    @GetMapping("/")
    public String index() {

        return "redirect:/guestbook/list";
    }


    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){

        log.info("list............." + pageRequestDTO);

        model.addAttribute("result", service.getList(pageRequestDTO));

    }

    @GetMapping("/register")
    public void register(@ModelAttribute("guestbook") GuestbookDTO dto){
        log.info("regiser get...");
    }

    @PostMapping("/register")
    public String registerPost(
            @ModelAttribute("guestbook") @Valid GuestbookDTO dto
            , BindingResult result
            , Model model
            , RedirectAttributes redirectAttributes){

        log.info("dto..." + dto);

        if (result.hasErrors()) {
            model.addAttribute("msg", result.getAllErrors().get(0).getDefaultMessage());
            return "/guestbook/register";
        } else {
            //새로 추가된 엔티티의 번호
            Long gno = service.register(dto);

            redirectAttributes.addFlashAttribute("msg", gno);

            return "redirect:/guestbook/list";
        }

    }

    //@GetMapping("/read")

    @GetMapping({"/read", "/modify"})
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model ){

        log.info("gno: " + gno);

        GuestbookDTO dto = service.read(gno);

        model.addAttribute("dto", dto);

    }

    @PostMapping("/remove")
    public String remove(long gno, RedirectAttributes redirectAttributes){


        log.info("gno: " + gno);

        service.remove(gno);

        redirectAttributes.addFlashAttribute("msg", gno);

        return "redirect:/guestbook/list";

    }

    @PostMapping("/modify")
    public String modify(
            @ModelAttribute("guestbook") @Valid GuestbookDTO dto
            , BindingResult result
            , @ModelAttribute("requestDTO") PageRequestDTO requestDTO
            , RedirectAttributes redirectAttributes
    ){

        log.info("post modify.........................................");
        log.info("dto: " + dto);

        if (result.hasErrors()) {
            redirectAttributes.addAttribute("gno",dto.getGno());
            return "redirect:/guestbook/modify";
        } else {
            service.modify(dto);

            redirectAttributes.addAttribute("page",requestDTO.getPage());
            redirectAttributes.addAttribute("type",requestDTO.getType());
            redirectAttributes.addAttribute("keyword",requestDTO.getKeyword());

            redirectAttributes.addAttribute("gno",dto.getGno());


            return "redirect:/guestbook/read";
        }
    }




}
