package org.example.proyectoReservaa.model.horarios;

import lombok.Getter;
import lombok.Setter;
import org.example.proyectoReservaa.model.BaseEntity;

import javax.persistence.Entity;
import java.time.LocalTime;

@Entity
@Getter
@Setter
public class HorarioReserva extends BaseEntity {

    private String nombre;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private int capacidadVisitantes;
    private EstadoHorario estado;

    // Si se desactiva el horario global
    public void desactivarGlobal() {
        this.estado = EstadoHorario.INHABILITADO;
    }

    public void activarGlobal() {
        this.estado = EstadoHorario.HABILITADO;
    }

}
