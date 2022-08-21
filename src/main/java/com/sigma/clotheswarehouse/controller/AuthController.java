package com.sigma.clotheswarehouse.controller;

import com.sigma.clotheswarehouse.utils.AppConstant;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppConstant.BASE_PATH+"/auth")
public class AuthController {

    @GetMapping("/home")
    public HttpEntity<?> home() {
        return ResponseEntity.ok("Welcome Clothes-Warehouse app");
    }
}
