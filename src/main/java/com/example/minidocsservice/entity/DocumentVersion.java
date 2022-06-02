package com.example.minidocsservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@IdClass(VersionPK.class)
public class DocumentVersion {

    @Id
    private Integer document_version_id;

    @Id
    private Integer document_id;

    @Column
    private byte[] content;

    @Column
    private String version_author;

}

@AllArgsConstructor
@NoArgsConstructor
@Data
class VersionPK implements Serializable {
    protected Integer document_version_id;
    protected Integer document_id;

}