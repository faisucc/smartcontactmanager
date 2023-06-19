package com.smart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;

@Controller
public class ForgotController {

    @RequestMapping("/forgot")
    public String openEmailForm(){

        return "forgot_email_form";
    }

    @PostMapping("/send_otp")
    public String sendOTP(@RequestParam("email") String email){
        System.out.println(email);
        Random random = new Random(100000);
        int OTP = random.nextInt(999999);
        System.out.println(OTP);
        return "verify_otp";
    }
}
