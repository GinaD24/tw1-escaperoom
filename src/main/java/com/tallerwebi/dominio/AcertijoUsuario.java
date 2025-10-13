package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
public class AcertijoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_etapa")
    private Etapa etapa;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_acertijo")
    private Acertijo acertijo;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;



    @Column (nullable = false)
    private Integer pistasUsadas = 0;

    public AcertijoUsuario() {

    }

    public AcertijoUsuario(Acertijo acertijo, Usuario usuario) {
        this.acertijo = acertijo;
        this.usuario = usuario;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getPistasUsadas() {
        return pistasUsadas;
    }

    public void setPistasUsadas(Integer pistasUsadas) {
        this.pistasUsadas = pistasUsadas;
    }

    public Etapa getEtapa() {
        return etapa;
    }

    public void setEtapa(Etapa etapa) {
        this.etapa = etapa;
    }
}
