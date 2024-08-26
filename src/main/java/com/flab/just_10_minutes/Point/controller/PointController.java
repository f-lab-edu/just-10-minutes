package com.flab.just_10_minutes.Point.controller;

import com.flab.just_10_minutes.Point.domain.PointHistory;
import com.flab.just_10_minutes.Point.dto.PointHistoryDto;
import com.flab.just_10_minutes.Point.dto.PointHistories;
import com.flab.just_10_minutes.Point.service.PointService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/points")
public class PointController {

    private final PointService pointService;

    @PostMapping("/offer")
    public ResponseEntity<PointHistory> offerPoint(@RequestBody @Valid PointHistoryDto pointHistoryDto) {
        //TODO : ADMIN 권한을 가진 유저만 실행할 수 있도록 수정. due date : 7/16

        PointHistory latestHistory = pointService.offerPoint(PointHistoryDto.toDomain(pointHistoryDto));

        return ResponseEntity.ok(latestHistory);
    }

    @GetMapping("/total/{loginId}")
    public ResponseEntity<Long> showTotal(@PathVariable final String loginId) {
        return ResponseEntity.ok(pointService.getTotalPoint(loginId));
    }

    @GetMapping("/histories/{loginId}")
    public ResponseEntity<PointHistories> showHistories(@PathVariable final String loginId) {
        return ResponseEntity.ok(pointService.getPointHistories(loginId));
    }
}
