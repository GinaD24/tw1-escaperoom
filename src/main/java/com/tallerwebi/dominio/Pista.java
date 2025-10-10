package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
public class Pista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column (nullable = false)
    private String descripcion;

    @Column
    private Integer numero;

    @ManyToOne
    @JoinColumn(name = "id_acertijo")
    private Acertijo acertijo;

    public Pista(String descripcion, Integer numero) {
        this.descripcion = descripcion;
        this.numero = numero;
    }

    public Pista() {}

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Acertijo getAcertijo() {
        return acertijo;
    }

    public void setAcertijo(Acertijo acertijo) {
        this.acertijo = acertijo;
    }
}
