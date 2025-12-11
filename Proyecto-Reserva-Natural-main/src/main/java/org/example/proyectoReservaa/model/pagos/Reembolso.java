package org.example.proyectoReservaa.model.pagos;

import lombok.Getter;
import lombok.Setter;
import org.example.proyectoReservaa.model.BaseEntity;
import org.example.proyectoReservaa.model.reservas.Reserva;
import org.example.proyectoReservaa.model.reservas.OrigenCambioEstado;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Reembolso extends BaseEntity {

    @Column(precision = 10, scale = 2)
    private BigDecimal monto;

    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    private OrigenCambioEstado origen;

    private String motivo;
    private boolean aprobado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reserva", referencedColumnName = "id")
    private Reserva reserva;

    public void aprobar(BigDecimal montoAprobado) {
        this.monto = montoAprobado;
        this.aprobado = true;
        this.fecha = LocalDateTime.now();
    }
}
