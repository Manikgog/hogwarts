package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.entity.Avatar;

import java.util.List;
import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    Optional<Avatar> findById(long studentId);
    @Query(value = "SELECT data FROM public.avatars ORDER BY id ASC ", nativeQuery = true)
    List<byte[]> getAvaratrsData();
}
