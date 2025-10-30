package com.tallerwebi.dominio.entidad;

import com.tallerwebi.dominio.enums.TipoAcertijo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Acertijo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAcertijo tipo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_etapa")
    private Etapa etapa;

    @OneToMany(mappedBy = "acertijo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ImagenAcertijo> imagenes = new HashSet<>();

    @OneToMany(mappedBy = "acertijo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DragDropItem> dragDropItems = new HashSet<>();

    public Acertijo( String descripcion) {
        this.descripcion = descripcion;
    }

    public Acertijo() {
    }

    public Set<ImagenAcertijo> getImagenes() {return imagenes;}

    public void setImagenes(Set<ImagenAcertijo> imagenes) {this.imagenes = imagenes;}

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

    public Etapa getEtapa() {
        return etapa;
    }

    public void setEtapa(Etapa etapa) {
        this.etapa = etapa;
    }

    public TipoAcertijo getTipo() {
        return tipo;
    }

    public void setTipo(TipoAcertijo tipo) {
        this.tipo = tipo;
    }
    public Set<DragDropItem> getDragDropItems() {
        return dragDropItems;
    }

    public void setDragDropItems(Set<DragDropItem> dragDropItems) {
        this.dragDropItems = dragDropItems;
    }
}
