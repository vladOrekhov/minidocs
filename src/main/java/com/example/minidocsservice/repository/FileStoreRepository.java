package com.example.minidocsservice.repository;

import com.example.minidocsservice.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileStoreRepository extends JpaRepository<Document, Integer> {

}
