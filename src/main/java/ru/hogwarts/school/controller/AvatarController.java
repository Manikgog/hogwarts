package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.service.AvatarService;
import java.io.IOException;

@RestController
@RequestMapping("/avatars")
@Tag(name = "Аватарки", description = "Эндпоинты для работы со аватарами студентов")
public class AvatarController {
    private final AvatarService avatarService;
    public AvatarController(AvatarService avatarService){
        this.avatarService = avatarService;
    }
    @PostMapping(value = "/{id}/student", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "добавление аватарки для студента")
    public ResponseEntity<Avatar> uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar) throws IOException {
        return avatarService.uploadAvatar(id, avatar);
    }
    @GetMapping(value = "/{id}/avatar/preview")
    @Operation(summary = "получение превью аватарки по id аватарки")
    public ResponseEntity<byte[]> getPreview(@PathVariable long id){
        return avatarService.getPreview(id);
    }
    @GetMapping(value = "/{id}/avatar")
    @Operation(summary = "получение оригинального изображения аватарки по её id")
    public void getAvatar(long id, HttpServletResponse response) throws IOException {
        avatarService.getAvatar(id, response);
    }

}
