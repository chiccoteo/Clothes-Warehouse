package com.sigma.clotheswarehouse.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
public class AuthController {

    @GetMapping()
    public HttpEntity<?> home() {
        return ResponseEntity.ok("Welcome Clothes-Warehouse app");
    }
}
