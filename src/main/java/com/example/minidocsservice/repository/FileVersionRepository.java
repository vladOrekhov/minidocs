package com.example.minidocsservice.repository;

import com.example.minidocsservice.entity.Document;
import com.example.minidocsservice.entity.DocumentVersion;
import com.example.minidocsservice.entity.RegCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileVersionRepository extends JpaRepository<DocumentVersion, Integer> {

    @Query(value = "SELECT * FROM document_version WHERE document_id = ?1 AND document_version_id = ?2", nativeQuery = true)
    DocumentVersion findDocumentVersionsByDocument_version_idAndDocument_id(Integer document_id,Integer document_version_id);

    @Query(value = "SELECT document_version_id FROM document_version WHERE document_id = ?1 ORDER BY document_version_id ASC", nativeQuery = true)
    Integer findLatestVersion(Integer document_id);

    @Query(value = "SELECT document_version_id FROM document_version WHERE document_id = ?1 ORDER BY document_version_id ASC", nativeQuery = true)
    List<Integer> findAllVersions(Integer document_id);
}
