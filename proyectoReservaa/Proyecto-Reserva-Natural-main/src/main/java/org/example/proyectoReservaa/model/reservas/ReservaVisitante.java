package org.example.proyectoReservaa.model.reservas;

import lombok.Getter;
import lombok.Setter;
import org.example.proyectoReservaa.model.BaseEntity;
import org.example.proyectoReservaa.model.visitantes.RolVisitante;
import org.example.proyectoReservaa.model.visitantes.Visitante;
import org.openxava.annotations.DescriptionsList;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class ReservaVisitante extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_reserva", referencedColumnName = "id")
    @DescriptionsList(descriptionProperties = "id")
    private Reserva reserva;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "id_visitante", referencedColumnName = "id")
    @DescriptionsList(descriptionProperties = "id")
    private Visitante visitante;

    @Enumerated(EnumType.STRING)
    private RolVisitante rol;

    @Enumerated(EnumType.STRING)
    private EstadoAsistencia asistencia;

    private LocalDateTime checkIn;

    public void registrarAsistencia() {
        this.asistencia = EstadoAsistencia.ASISTIDO;
        this.checkIn = LocalDateTime.now();
    }

    public void marcarNoAsistio() {
        this.asistencia = EstadoAsistencia.NO_ASISTIDO;
    }
}
