package hkeller.escolacaesguia.pessoa;

/*
*     "id"               BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    "nome"             text          NOT NULL,
    "data_nascimento"  date          NOT NULL,
    "cpf"              character(11) NOT NULL,
    "telefone_celular" text          NOT NULL,
    "telefone_fixo"    text,
    "created_at"       timestamp DEFAULT (now()),
    "updated_at"       timestamp DEFAULT (now())*/


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity(name = "pessoa")
public class Pessoa {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false,name = "nome")
  private String nome;
  @Column(nullable = false,name = "data_nascimento")
  private Date dataNascimento;
  @Column(nullable = false,name = "cpf")
  private String cpf;
  @Column(name = "telefone_celular")
  private String telefoneCelular;
  @Column(name = "telefone_fixo")
  private String telefoneFixo;
  @Column(name = "created_at")
  private Timestamp created_at;
  @Column(name = "updated_at")
  private Timestamp updated_at;
}
