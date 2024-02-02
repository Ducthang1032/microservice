package tdt.intern.image.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tdt.intern.image.service.ImageInfoService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class HomeController {
    private final ImageInfoService imageInfoService;

    @GetMapping("/info")
    public ResponseEntity<Object> getImageInfo() {
        return ResponseEntity.ok(imageInfoService.getAll());
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<Object> getImageInfoById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(imageInfoService.getById(id));
    }
}