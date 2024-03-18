package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Min;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.dto.AvatarDto;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.service.AvatarService;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(AvatarController.BASE_URI)
@Tag(name = "Аватарки", description = "Эндпоинты для работы со аватарами студентов")
@Validated
public class AvatarController {
    public static final String BASE_URI = "/avatars";
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
    @Operation(summary = "получение превью аватарки по id студента")
    public ResponseEntity<byte[]> getPreview(@PathVariable long id){
        return avatarService.getPreview(id);
    }
    @GetMapping(value = "/{id}/avatar")
    @Operation(summary = "получение оригинального изображения аватарки по её id")
    public void getAvatar(long id, HttpServletResponse response) throws IOException {
        avatarService.getAvatar(id, response);
    }
    @GetMapping
    @Operation(summary = "Получение списка ссылок на аватарки постранично")
    public List<AvatarDto> getAvatars(@RequestParam @Min(value = 1, message = "Минимальный номер страницы = 1") int page,
                                      @RequestParam @Min(value = 1, message = "Минимальное количество записей на странице = 1") int size){
        return avatarService.getAvatars(page, size);
    }

}
