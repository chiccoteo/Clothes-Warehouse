package com.sigma.clotheswarehouse.controller;

import com.sigma.clotheswarehouse.payload.ApiResponse;
import com.sigma.clotheswarehouse.payload.ClientPostDto;
import com.sigma.clotheswarehouse.payload.ClientUpdateDto;
import com.sigma.clotheswarehouse.service.ClientService;
import com.sigma.clotheswarehouse.utils.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstant.BASE_PATH + "/client")
@CrossOrigin(maxAge = 3600)
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public HttpEntity<?> addClient(@RequestBody ClientPostDto clientPostDto){
        ApiResponse apiResponse = clientService.addClient(clientPostDto);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/getById/{id}")
    public HttpEntity<?> getClientById(@PathVariable Long id){
        ApiResponse apiResponse = clientService.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 400).body(apiResponse);
    }

    @GetMapping("/getAll")
    public HttpEntity<?> getClients(){
        ApiResponse apiResponse = clientService.getClients();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 400).body(apiResponse);
    }

    @PutMapping("/update/{id}")
    public HttpEntity<?> updateClient(@PathVariable Long id, @RequestBody ClientUpdateDto clientUpdateDto){
        ApiResponse apiResponse = clientService.updateClient(id, clientUpdateDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 400).body(apiResponse);
    }

    @DeleteMapping("/delete/{id}")
    public HttpEntity<?> deleteClient(@PathVariable Long id){
        ApiResponse apiResponse = clientService.deleteClient(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 400).body(apiResponse);
    }

}
