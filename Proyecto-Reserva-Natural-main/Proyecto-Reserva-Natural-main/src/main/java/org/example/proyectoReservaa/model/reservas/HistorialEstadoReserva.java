package org.example.proyectoReservaa.model.reservas;

import lombok.Getter;
import lombok.Setter;
import org.example.proyectoReservaa.model.BaseEntity;
import org.openxava.annotations.DescriptionsList;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class HistorialEstadoReserva extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @DescriptionsList(descriptionProperties = "id")
    private Reserva reserva;

    @Enumerated(EnumType.STRING)
    private EstadoReserva estadoAnterior;

    @Enumerated(EnumType.STRING)
    private EstadoReserva estadoNuevo;

    private String origenCambio;
    private LocalDateTime fechaCambio;
    private String motivo;
}
