package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Информация", description = "Эндпоинт для получения номера порта")
public class InfoController {
    @Value("${server.port}")
    private Integer port;
    @GetMapping("/port")
    public ResponseEntity<Integer> getPort(){
        return ResponseEntity.ok(port);
    }
}
