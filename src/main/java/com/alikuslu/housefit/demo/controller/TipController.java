package com.alikuslu.housefit.demo.controller;

import com.alikuslu.housefit.demo.dto.TipDto;
import com.alikuslu.housefit.demo.model.Tip;
import com.alikuslu.housefit.demo.model.TipCategory;
import com.alikuslu.housefit.demo.service.TipService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tips")
public class TipController {

    private final TipService tipService;

    public TipController(TipService tipService) {
        this.tipService = tipService;
    }

    @PostMapping
    public ResponseEntity<Tip> createTip(@RequestBody TipDto dto) {
        return ResponseEntity.ok(tipService.createTip(dto));
    }

    @GetMapping
    public ResponseEntity<List<Tip>> getAllTips() {
        return ResponseEntity.ok(tipService.getAllTips());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Tip>> getTipsByCategory(
            @PathVariable TipCategory category) {
        return ResponseEntity.ok(tipService.getTipsByCategory(category));
    }
}
