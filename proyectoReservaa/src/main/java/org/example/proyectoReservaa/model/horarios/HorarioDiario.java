package org.example.proyectoReservaa.model.horarios;

import lombok.Getter;
import lombok.Setter;
import org.example.proyectoReservaa.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class HorarioDiario extends BaseEntity {
    private LocalDate fecha;
    private int cuposDisponibles;
    private EstadoHorario estado;
    private String motivoCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_horario_reserva", referencedColumnName = "id")
    private HorarioReserva horarioBase;

    //Inicializa los cupos segun la capacidad global
    public void inicializarCupos() {
        this.cuposDisponibles = horarioBase.getCapacidadVisitantes();
    }

    public boolean tieneCupos(int cantidad) {
        return cuposDisponibles >= cantidad;
    }

    //Reduce los cupos cuando se crea una reserva
    public void descontarCupos(int cantidad) {
        if (cantidad <= 0) return;
        if (!tieneCupos(cantidad))
            throw new IllegalStateException("No hay cupos suficientes.");

        this.cuposDisponibles -= cantidad;
    }

    //Liberara los cupos si la reserva se cancela
    public void aumentarCupos(int cantidad) {
        if (cantidad <= 0) return;
        this.cuposDisponibles += cantidad;

        //nunca se podra exceder la capacidad original
        if (this.cuposDisponibles > horarioBase.getCapacidadVisitantes()) {
            this.cuposDisponibles = horarioBase.getCapacidadVisitantes();
        }
    }

    //Validacion para no permitir reservas en dias/horas del pasado
    public boolean esFuturo() {
        return !fecha.isBefore(LocalDate.now());
    }
}
