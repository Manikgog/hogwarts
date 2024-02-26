package ru.hogwarts.school.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.exception.AvatarProcessingException;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.entity.Student;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class AvatarService {
    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;
    private final Path pathToAvatarsDir;
    public AvatarService(AvatarRepository avatarRepository,
                         StudentRepository studentRepository,
                         @Value("${application.path-to-avatars-dir}") Path pathToAvatarsDir) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
        this.pathToAvatarsDir = pathToAvatarsDir;
    }
    @PostConstruct
    public void init(){
        try{
            if(!Files.exists(pathToAvatarsDir) || !Files.isDirectory(pathToAvatarsDir)){
                Files.createDirectories(pathToAvatarsDir);
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    public void upload(long studentId, MultipartFile image) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new NotFoundException(studentId));
        try {
            byte[] data = image.getBytes();
            String extension = StringUtils.getFilenameExtension(image.getOriginalFilename());
            String fileName = String.format("%s.%s", UUID.randomUUID(), extension);
            Path path = pathToAvatarsDir.resolve(fileName);
            Files.write(path, data);
            Avatar avatar = new Avatar();
            avatar.setStudent(student);
            avatar.setData(data);
            avatar.setFileSize(image.getSize());
            avatar.setMediaType(image.getContentType());
            avatar.setFilePath(path.toString());
            this.avatarRepository.save(avatar);
        }catch(IOException e){
            throw new AvatarProcessingException(e);
        }
    }
    public Pair<byte[], String> downLoadFromDB(long studentId) {
        Avatar avatar = avatarRepository.findByStudent_Id(studentId).orElseThrow(() -> new NotFoundException(studentId));
        return Pair.of(avatar.getData(), avatar.getMediaType());
    }

    public Pair<byte[], String> downLoadFromFS(long studentId) {
        try {
            Avatar avatar = avatarRepository.findByStudent_Id(studentId).orElseThrow(() -> new NotFoundException(studentId));
            byte[] data = Files.readAllBytes(Paths.get(avatar.getFilePath()));
            return Pair.of(data, avatar.getMediaType());
        }catch (IOException e){
            throw new AvatarProcessingException(e);
        }
    }
}
