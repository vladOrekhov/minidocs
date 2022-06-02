package com.example.minidocsservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class RegCard {
    @Id
    @SequenceGenerator(name="reg_pk_sequence",sequenceName="\"reg_card_id_seq\"",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reg_pk_sequence")
    private Integer regCardId;

    @Column
    private Integer documentId;

    @Column
    private String documentIntroNumber;
    @Column
    private String documentExternNumber;

    @Column
    private Timestamp dateIntro;

    @Column
    private Timestamp dateExtern;

}
