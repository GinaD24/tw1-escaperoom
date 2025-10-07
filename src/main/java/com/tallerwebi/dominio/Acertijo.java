package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
public class Acertijo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "id_etapa")
    private Etapa etapa;

    public Acertijo( String descripcion) {
        this.descripcion = descripcion;
    }

    public Acertijo() {

    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
