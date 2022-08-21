package com.sigma.clotheswarehouse.controller;

import com.sigma.clotheswarehouse.payload.ApiResponse;
import com.sigma.clotheswarehouse.payload.MaterialPostDTO;
import com.sigma.clotheswarehouse.payload.MaterialUpdateDTO;
import com.sigma.clotheswarehouse.service.MaterialService;
import com.sigma.clotheswarehouse.utils.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.sigma.clotheswarehouse.utils.AppConstant.DEFAULT_PAGE;
import static com.sigma.clotheswarehouse.utils.AppConstant.DEFAULT_SIZE;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstant.BASE_PATH + "/material")
@CrossOrigin(maxAge = 3600)
public class MaterialController {

    private final MaterialService materialService;
    private static final String ALL_MATERIALS = "/all";

    private static final String ALL_MATERIALS_WITHOUT_PAGE = "/allWithoutPage";

    private static final String MATERIAL_BY_ID = "/getById";

    private static final String UPDATE_MATERIAL_BY_ID = "/updateById";

    private static final String DELETE_MATERIAL_BY_ID = "/deleteById";


    @PostMapping
    public HttpEntity<?> addMaterial(@RequestBody MaterialPostDTO materialPostDTO) {
        ApiResponse apiResponse = materialService.addMaterial(materialPostDTO);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping(ALL_MATERIALS)
    public HttpEntity<?> getAllMaterials(
            @RequestParam(value = "page", defaultValue = DEFAULT_PAGE) Integer page,
            @RequestParam(value = "size", defaultValue = DEFAULT_SIZE) Integer size
    ) {
        ApiResponse apiResponse = materialService.getAllMaterials(page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 400).body(apiResponse);
    }

    @GetMapping(ALL_MATERIALS_WITHOUT_PAGE)
    public HttpEntity<?> getAllMaterialsWithoutPage() {
        ApiResponse apiResponse = materialService.getAllMaterialsWithoutPage();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping(MATERIAL_BY_ID)
    public HttpEntity<?> getMaterialById(@RequestParam(name = "materialId") UUID materialId) {
        ApiResponse apiResponse = materialService.getMaterialById(materialId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 404).body(apiResponse);
    }

    @PutMapping(UPDATE_MATERIAL_BY_ID)
    public HttpEntity<?> editMaterialById(@RequestParam(name = "materialId") UUID materialId,
                                          @RequestBody MaterialUpdateDTO materialUpdateDTO) {
        ApiResponse apiResponse = materialService.editMaterialById(materialId, materialUpdateDTO);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 404).body(apiResponse);
    }

    @DeleteMapping(DELETE_MATERIAL_BY_ID)
    public HttpEntity<?> deleteMaterialById(@RequestParam(name = "materialId") UUID materialId) {
        ApiResponse apiResponse = materialService.deleteById(materialId);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 404).body(apiResponse);
    }
}
