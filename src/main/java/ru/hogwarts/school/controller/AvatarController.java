package ru.hogwarts.school.controller;

import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.service.AvatarService;

@RestController
@RequestMapping("/avatars")
public class AvatarController {
    private final AvatarService avatarService;
    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadAvatar(@RequestParam long studentId, @RequestPart MultipartFile avatar){
        avatarService.upload(studentId, avatar);
    }
    @GetMapping("/from-db")
    public ResponseEntity<byte[]> getAvatarFromDB(@RequestParam long studentId){
        return transform(avatarService.downLoadFromDB(studentId));
    }
    @GetMapping("/from-fs")
    public ResponseEntity<byte[]> getAvatarFromFS(@RequestParam long studentId){
        return transform(avatarService.downLoadFromFS(studentId));
    }
    private ResponseEntity<byte[]> transform(Pair<byte[], String> pair){
        byte[] data = pair.getFirst();
        return ResponseEntity.status(HttpStatus.OK)
                .contentLength(data.length)
                .contentType(MediaType.parseMediaType(pair.getSecond()))
                .body(data);
    }
}
