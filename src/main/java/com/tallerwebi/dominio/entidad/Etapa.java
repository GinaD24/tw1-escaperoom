package com.tallerwebi.dominio.entidad;

import javax.persistence.*;

@Entity
public class Etapa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String nombre;

    @Column (nullable = false)
    private Integer numero;

    @Column (nullable = false)
    private String descripcion;

    @Column
    private String imagen;

    @Column
    private Boolean tieneBonus;

    @Column
    private Integer bonusTop;

    @Column
    private Integer bonusLeft;

    @ManyToOne
    @JoinColumn(name = "id_sala")
    private Sala sala;

    public Etapa(){}

    public Etapa(String nombre, Integer numero, String descripcion, String imagen) {
        this.nombre = nombre;
        this.numero = numero;
        this.descripcion = descripcion;
        this.imagen = imagen;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public Integer getBonusLeft() {
        return bonusLeft;
    }

    public void setBonusLeft(Integer bonusLeft) {
        this.bonusLeft = bonusLeft;
    }

    public Integer getBonusTop() {
        return bonusTop;
    }

    public void setBonusTop(Integer bonusTop) {
        this.bonusTop = bonusTop;
    }

    public Boolean getTieneBonus() {
        return tieneBonus;
    }

    public void setTieneBonus(Boolean tieneBonus) {
        this.tieneBonus = tieneBonus;
    }
}
