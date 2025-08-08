package com.oa.UrlShorter.repository;

import com.oa.UrlShorter.models.ShortUrl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<ShortUrl, Long> {
    //    @Query("SELECT url FROM ShortUrl url LEFT JOIN FETCH url.createdBy WHERE url.isPrivate = false ORDER BY url.createdAt DESC")
    //    List<ShortUrl> findByPublicShortUrls();

    // Yukardaki ile aynı, createdBy fetch olacak n+1 olmayacak
    @Query("SELECT url FROM ShortUrl url WHERE url.isPrivate = false")
    @EntityGraph(attributePaths = {"createdBy"})
    Page<ShortUrl> findByPublicShortUrls(Pageable pageable);

    boolean existsByShortKey(String shortKey);

    Optional<ShortUrl> findByShortKey(String shortKey);

    // List<ShortUrl> findByIsPrivateIsFalseOrderByCreatedAtDesc();
}
