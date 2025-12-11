package org.example.proyectoReservaa.model.pagos;

import lombok.Getter;
import lombok.Setter;
import org.example.proyectoReservaa.model.BaseEntity;
import org.example.proyectoReservaa.model.reservas.Reserva;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Pago extends BaseEntity {

    private String proveedor;
    private String codigoTransaccion;

    @Column(precision = 10, scale = 2)
    private BigDecimal montoTotal;
    private LocalDateTime fechaPago;

    @Enumerated(EnumType.STRING)
    private EstadoPago estado = EstadoPago.PENDIENTE;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva", referencedColumnName = "id")
    private Reserva reserva;

    public void aprobar(String codigoTransaccion) {
        this.codigoTransaccion = codigoTransaccion;
        this.estado = EstadoPago.APROBADO;
        this.fechaPago = LocalDateTime.now();
    }

    public void rechazar(String motivoCodigo) {
        this.codigoTransaccion = motivoCodigo;
        this.estado = EstadoPago.RECHAZADO;
        this.fechaPago = LocalDateTime.now();
    }

    public boolean estaPagado() {
        return estado == EstadoPago.APROBADO || estado == EstadoPago.REEMBOLSADO;
    }
}