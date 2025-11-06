package com.tallerwebi.dominio.entidad;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_sala", nullable = false)
    private Sala sala;

    @Column(nullable = false)
    private LocalDateTime fechaCompra;

    @Column(nullable = false)
    private Boolean pagada;

    @Column(name = "external_reference")
    private String externalReference;

    @Column(name = "payment_id")
    private String paymentId;

    // Constructor vacío (requerido por Hibernate)
    public Compra() {}

    public Compra(Usuario usuario, Sala sala, LocalDateTime fechaCompra, Boolean pagada) {
        this.usuario = usuario;
        this.sala = sala;
        this.fechaCompra = fechaCompra;
        this.pagada = pagada;
    }

    // Getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Sala getSala() { return sala; }
    public void setSala(Sala sala) { this.sala = sala; }

    public LocalDateTime getFechaCompra() { return fechaCompra; }
    public void setFechaCompra(LocalDateTime fechaCompra) { this.fechaCompra = fechaCompra; }

    public Boolean getPagada() { return pagada; }
    public void setPagada(Boolean pagada) { this.pagada = pagada; }

    public String getExternalReference() { return externalReference; }
    public void setExternalReference(String externalReference) { this.externalReference = externalReference; }

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
}