package kr.co.adamsoft.club.controller;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import kr.co.adamsoft.club.security.dto.*;

@Controller
@Log4j2
@RequestMapping("/sample/")
public class SampleController{

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/all")
    public void exAll(){

        log.info("exAll.........." + passwordEncoder );

    }

    @GetMapping("/member")
    public void exMember(@AuthenticationPrincipal ClubAuthMember clubAuthMember){

        log.info("exMember..........");

        log.info("-------------------------------");
        log.info(clubAuthMember);
    }


    @GetMapping("/admin")
    public void exAdmin(){
        log.info("exAdmin..........");
    }

}
