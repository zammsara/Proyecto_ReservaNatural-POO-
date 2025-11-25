package org.example.proyectoReservaa.model.reservas;

import lombok.Getter;
import lombok.Setter;
import org.example.proyectoReservaa.model.BaseEntity;
import org.example.proyectoReservaa.model.horarios.HorarioDiario;
import org.example.proyectoReservaa.model.visitantes.VisitanteResponsable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Reserva extends BaseEntity {
    private LocalDateTime fechaCreacion;
    private int totalVisitantes;
    private boolean estacionamientoRequerido = false;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "id_visitanteResponsable", referencedColumnName = "id")
    private VisitanteResponsable responsable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_horarioDiario", referencedColumnName = "id")
    private HorarioDiario horario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "id_estadoReserva", referencedColumnName = "id")
    private EstadoReserva estadoReserva;
}
