package com.tallerwebi.dominio.entidad;

import javax.persistence.*;

@Entity

public class ImagenAcertijo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_acertijo",  nullable = false)
    private Acertijo acertijo;

    @Column(nullable = false)
    private String nombreArchivo;

    @Column
    private Integer ordenCorrecto;

    public ImagenAcertijo() {
    }

    public ImagenAcertijo(Acertijo acertijo, String nombreArchivo) {
        this.acertijo = acertijo;
        this.nombreArchivo = nombreArchivo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Acertijo getAcertijo() {
        return acertijo;
    }

    public void setAcertijo(Acertijo acertijo) {
        this.acertijo = acertijo;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public Integer getOrdenCorrecto() {
        return ordenCorrecto;
    }

    public void setOrdenCorrecto(Integer ordenCorrecto) {
        this.ordenCorrecto = ordenCorrecto;
    }
}

