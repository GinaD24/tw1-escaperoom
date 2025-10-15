package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
public class Respuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String respuesta;

    @OneToOne
    @JoinColumn(name = "id_acertijo")
    private Acertijo acertijo;

    @Column(nullable = false)
    private Boolean es_correcta;

    public Respuesta(){

    }

    public Respuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Acertijo getAcertijo() {
        return acertijo;
    }

    public void setAcertijo(Acertijo acertijo) {
        this.acertijo = acertijo;
    }

    public Boolean getEs_correcta() {
        return es_correcta;
    }

    public void setEs_correcta(Boolean es_correcta) {
        this.es_correcta = es_correcta;
    }
}
