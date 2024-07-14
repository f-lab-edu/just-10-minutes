package com.flab.just_10_minutes.Point.controller;

import com.flab.just_10_minutes.Point.dto.PointHistoryDto;
import com.flab.just_10_minutes.Point.service.PointService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/points")
public class PointController {

    private PointService pointService;

    @PostMapping
    public ResponseEntity<HttpStatus> offerPoint(@RequestBody @Valid PointHistoryDto pointHistoryDto) {
        //TODO : ADMIN 권한을 가진 유저만 실행할 수 있도록 수정. due date : 7/16
        pointService.save(PointHistoryDto.toDomain(pointHistoryDto));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
