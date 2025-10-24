package com.tallerwebi.dominio.entidad;

import javax.persistence.*;

@Entity
public class DragDropItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_acertijo")
    private Acertijo acertijo;

    @Column(nullable = false)
    private String contenido;

    @Column
    private String categoriaCorrecta;

    public DragDropItem() {}

    public DragDropItem(Acertijo acertijo, String contenido, String categoriaCorrecta) {
        this.acertijo = acertijo;
        this.contenido = contenido;
        this.categoriaCorrecta = categoriaCorrecta;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Acertijo getAcertijo() { return acertijo; }
    public void setAcertijo(Acertijo acertijo) { this.acertijo = acertijo; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }


    public String getCategoriaCorrecta() {
        return categoriaCorrecta;
    }

    public void setCategoriaCorrecta(String categoriaCorrecta) {
        this.categoriaCorrecta = categoriaCorrecta;
    }
}
