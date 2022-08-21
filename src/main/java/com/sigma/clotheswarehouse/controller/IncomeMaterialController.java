package com.sigma.clotheswarehouse.controller;

import com.sigma.clotheswarehouse.payload.ApiResponse;
import com.sigma.clotheswarehouse.payload.IncomeMaterialDTO;
import com.sigma.clotheswarehouse.service.IncomeMaterialService;
import com.sigma.clotheswarehouse.utils.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.sigma.clotheswarehouse.utils.AppConstant.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstant.BASE_PATH + "/incomeMaterial")
@CrossOrigin(maxAge = 3600)
public class IncomeMaterialController {

    private final IncomeMaterialService incomeMaterialService;

    private static final String ALL_INCOME_MATERIALS = "/all";

    private static final String INCOME_MATERIALS_BETWEEN_TIMES = "/between";

    @PostMapping
    public HttpEntity<?> addIncomeMaterials(@RequestBody List<IncomeMaterialDTO> incomeMaterialDTOList) {
        ApiResponse apiResponse = incomeMaterialService.addIncomeMaterial(incomeMaterialDTOList);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping(ALL_INCOME_MATERIALS)
    public HttpEntity<?> getAllIncomeMaterials(
            @RequestParam(value = "page", defaultValue = DEFAULT_PAGE) Integer page,
            @RequestParam(value = "size", defaultValue = DEFAULT_SIZE) Integer size
    ) {
        ApiResponse apiResponse = incomeMaterialService.getAllIncomeMaterials(page, size);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 400).body(apiResponse);
    }

    @GetMapping(INCOME_MATERIALS_BETWEEN_TIMES)
    public HttpEntity<?> getIncomeMaterialsBetweenTimes(
            @RequestParam(value = "page", defaultValue = DEFAULT_PAGE) Integer page,
            @RequestParam(value = "size", defaultValue = DEFAULT_SIZE) Integer size,
            @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        LocalDate localDate = LocalDate.parse(startDate.format(DateTimeFormatter.ISO_DATE));
        endDate = endDate.plus(1, ChronoUnit.DAYS);
        LocalDate localDate1 = LocalDate.parse(endDate.format(DateTimeFormatter.ISO_DATE));
        Timestamp startDateTimestamp = Timestamp.valueOf(localDate.atTime(LocalTime.MIDNIGHT));
        Timestamp endDateTimestamp = Timestamp.valueOf(localDate1.atTime(LocalTime.MIDNIGHT));
        ApiResponse apiResponse = incomeMaterialService.getIncomeMaterialsBetweenTimes(page, size, startDateTimestamp, endDateTimestamp);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 400).body(apiResponse);
    }


}
