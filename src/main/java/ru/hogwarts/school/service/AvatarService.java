package ru.hogwarts.school.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.dto.AvatarDto;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.exception.TooBigFileException;
import ru.hogwarts.school.mapper.AvatarMapper;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {
    @Value("${path.to.avatars.folder}")
    private String avatarsDir;
    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;
    private final AvatarMapper avatarMapper;
    Logger logger = LoggerFactory.getLogger(AvatarService.class);
    public AvatarService(AvatarRepository avatarRepository,
                         StudentRepository studentRepository,
                         AvatarMapper avatarMapper){
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
        this.avatarMapper = avatarMapper;
    }
    public ResponseEntity uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        logger.info("The uploadAvatar(Long studentId, MultipartFile file) method was called");
        if(file.getSize() > 1024 * 300){
            logger.error("The file size of the photo is larger 1024*300 bytes.");
            throw new TooBigFileException(file.getName());
        }
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new NotFoundException(studentId));

        Path filePath = Path.of(avatarsDir, studentId + "." + getExtention(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try(InputStream is = file.getInputStream();
            OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
            BufferedInputStream bis = new BufferedInputStream(is, 1024);
            BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ){
            bis.transferTo(bos);
        }

        Avatar studentAvatar = avatarRepository.findById(studentId).orElse(new Avatar());
        studentAvatar.setFilePath(filePath.toString());
        studentAvatar.setFileSize(file.getSize());
        studentAvatar.setMediaType(file.getContentType());
        studentAvatar.setData(generateImagePreview(filePath));
        studentAvatar.setStudent(student);
        avatarRepository.save(studentAvatar);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<byte[]> getPreview(long id){
        logger.info("The getPreview(long id) method was called");
        Student student = studentRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        Avatar preview = avatarRepository.findByStudent(student).orElseThrow(() -> new NotFoundException(student.getId()));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(preview.getMediaType()));
        headers.setContentLength(preview.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(preview.getData());
    }

    public void getAvatar(long id, HttpServletResponse response) throws IOException{
        logger.info("The getAvatar(long id, HttpServletResponse response) method was called");
        Avatar preview = avatarRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        Path path = Path.of(preview.getFilePath());
        try(InputStream is = Files.newInputStream(path);
            OutputStream os = response.getOutputStream()){
            response.setContentType(preview.getMediaType());
            response.setContentLength((int)preview.getFileSize());
            is.transferTo(os);
        }
    }
    public String getExtention(String fileName){
        logger.info("The getExtention(String fileName) method was called");
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    private byte[] generateImagePreview(Path file) throws IOException{
        logger.info("The generateImagePreview(Path file) method was called");
        try(InputStream is = Files.newInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is, 1024);
            ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage bufferedImage = ImageIO.read(bis);

            int height = bufferedImage.getHeight() / (bufferedImage.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, height, bufferedImage.getType());
            Graphics2D graphics2D = preview.createGraphics();
            graphics2D.drawImage(bufferedImage, 0, 0, 100, height, null);
            graphics2D.dispose();
            ImageIO.write(preview, getExtention(file.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }

    public List<AvatarDto> getAvatars(int page, int size) {
        logger.info("The getAvatars(int page, int size) method was called");
        return avatarRepository.findAll(PageRequest.of(page-1, size)).get()
                .map(avatarMapper::toDto)
                .collect(Collectors.toList());
    }
}
