package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByUserIdAndArticleId(Long userId, Long articleId);

    long countByArticleId(Long articleId);
    Optional<Like> findByUserIdAndArticleId(Long userId, Long articleId);

}
