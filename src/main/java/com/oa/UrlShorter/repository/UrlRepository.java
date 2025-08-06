package com.oa.UrlShorter.repository;

import com.oa.UrlShorter.models.ShortUrl;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UrlRepository extends JpaRepository<ShortUrl, Long> {
    //    @Query("SELECT url FROM ShortUrl url LEFT JOIN FETCH url.createdBy WHERE url.isPrivate = false ORDER BY url.createdAt DESC")
    //    List<ShortUrl> findByPublicShortUrls();

    // Yukardaki ile aynı, createdBy fetch olacak n+1 olmayacak
    @Query("SELECT url FROM ShortUrl url WHERE url.isPrivate = false ORDER BY url.createdAt DESC")
    @EntityGraph(attributePaths = {"createdBy"})
    List<ShortUrl> findByPublicShortUrls();


    // List<ShortUrl> findByIsPrivateIsFalseOrderByCreatedAtDesc();
}
