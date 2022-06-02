package com.example.minidocsservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Document {
	
	@Id
	@SequenceGenerator(name="pk_sequence",sequenceName="\"document_id_seq\"",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
	private Integer document_id;
	
	@Column(name = "document_name")
	private String document_name;
	
	@Column
	private String author;

}
