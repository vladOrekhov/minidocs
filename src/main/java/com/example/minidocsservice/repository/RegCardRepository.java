package com.example.minidocsservice.repository;

import com.example.minidocsservice.entity.Document;
import com.example.minidocsservice.entity.RegCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@Transactional
@Repository
public interface RegCardRepository extends JpaRepository<RegCard, Integer> {

    Optional<RegCard> findByDocumentId(Integer document_id);

    @Modifying
    @Query(value = "UPDATE reg_card SET date_extern= ?1, document_extern_number = ?2 WHERE document_id = ?3", nativeQuery = true)
    void remove(Timestamp timestamp, Integer externNum, Integer document_id);


    @Query(value = "SELECT count(*) FROM reg_card WHERE document_id = ?1 AND date_extern IS NOT NULL", nativeQuery = true)
    Integer checkExterned(Integer document_id);
}
