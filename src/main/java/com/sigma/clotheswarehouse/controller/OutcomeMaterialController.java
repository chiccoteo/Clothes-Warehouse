package com.sigma.clotheswarehouse.controller;

import com.sigma.clotheswarehouse.payload.ApiResponse;
import com.sigma.clotheswarehouse.payload.OutcomeMaterialPostDTO;
import com.sigma.clotheswarehouse.service.OutcomeMaterialService;
import com.sigma.clotheswarehouse.utils.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.sigma.clotheswarehouse.utils.AppConstant.DEFAULT_PAGE;
import static com.sigma.clotheswarehouse.utils.AppConstant.DEFAULT_SIZE;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstant.BASE_PATH + "/outcomeMaterial")
@CrossOrigin(maxAge = 3600)
public class OutcomeMaterialController {

    private final OutcomeMaterialService outcomeMaterialService;

    private static final String UPDATE_OUTCOME_MATERIAL_BY_ID = "/updateById";

    private static final String DELETE_OUTCOME_MATERIAL_BY_ID = "/deleteById";

    @PostMapping
    public HttpEntity<?> addOutcomeMaterial(@RequestBody OutcomeMaterialPostDTO outcomeMaterialPostDTO) {
        ApiResponse apiResponse = outcomeMaterialService.addOutcomeMaterial(outcomeMaterialPostDTO);
        return ResponseEntity.status(apiResponse.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getAllOutcomeMaterials(
            @RequestParam(value = "page", defaultValue = DEFAULT_PAGE) Integer page,
            @RequestParam(value = "size", defaultValue = DEFAULT_SIZE) Integer size
    ) {
        ApiResponse apiResponse = outcomeMaterialService.getAllOutcomeMaterials(page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PutMapping(UPDATE_OUTCOME_MATERIAL_BY_ID)
    public HttpEntity<?> editOutcomeMaterialById(
            @RequestParam(name = "outcomeMaterialId") UUID id,
            @RequestBody OutcomeMaterialPostDTO outcomeMaterialUpdateDTO
    ) {
        ApiResponse apiResponse = outcomeMaterialService.editOutcomeMaterialById(id, outcomeMaterialUpdateDTO);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 404).body(apiResponse);
    }

    @DeleteMapping(DELETE_OUTCOME_MATERIAL_BY_ID)
    public HttpEntity<?> deleteOutcomeMaterialId(@RequestParam(name = "outcomeMaterialId") UUID id) {
        ApiResponse apiResponse = outcomeMaterialService.deleteOutcomeMaterialId(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 404).body(apiResponse);
    }
}
