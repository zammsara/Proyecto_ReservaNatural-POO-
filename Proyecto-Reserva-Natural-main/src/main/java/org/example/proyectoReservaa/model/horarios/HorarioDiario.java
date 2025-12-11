package org.example.proyectoReservaa.model.horarios;

import lombok.Getter;
import lombok.Setter;
import org.example.proyectoReservaa.model.BaseEntity;
import org.openxava.annotations.DescriptionsList;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class HorarioDiario extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_horarioGlobal", referencedColumnName = "id")
    @DescriptionsList(descriptionProperties = "nombre")
    private HorarioReserva horarioReserva;

    private LocalDate fecha;

    //capacidad total de este dia (no puede ser mayor que la del horario global)
    private int cupoHabilitado;

    //cupos restantes que pueden reservarse, baja al crear reservas y sube cuando se cancelan
    private int cupoDisponible;

    private EstadoHorario estado;
    private String motivoCambio;

    //inicializa los cupos del dia segun el horario global
    public void inicializarCupos() {
        int capacidad = horarioReserva.getCapacidadVisitantes();
        this.cupoHabilitado = capacidad;
        this.cupoDisponible = capacidad;
    }

    public boolean estaDentroDeRangoReserva() {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusYears(1);
        return !fecha.isBefore(hoy) && !fecha.isAfter(limite);
    }

    public void validarDisponibilidad(int cantidad) {
        if (estado == EstadoHorario.INHABILITADO) {
            throw new IllegalStateException("El horario no está habilitado.");
        }
        if (!estaDentroDeRangoReserva()) {
            throw new IllegalStateException("La fecha del horario no está dentro del periodo permitido.");
        }
        if (!tieneCupos(cantidad)) {
            throw new IllegalStateException("No hay cupos suficientes para la reserva solicitada.");
        }
    }

    public boolean tieneCupos(int cantidad) {
        return cupoDisponible >= cantidad;
    }

    //reduce cupos disponibles al crear una reserva
    public void descontarCupos(int cantidad) {
        if (cantidad <= 0) return;

        if (!tieneCupos(cantidad)) {
            throw new IllegalStateException("No hay cupos suficientes.");
        }

        this.cupoDisponible -= cantidad;
    }

    //nncrementa cupos disponibles al cancelar una reserva
    public void aumentarCupos(int cantidad) {
        if (cantidad <= 0) return;

        this.cupoDisponible += cantidad;

        //no puede superar la capacidad habilitada para ese día
        if (this.cupoDisponible > cupoHabilitado) {
            this.cupoDisponible = cupoHabilitado;
        }
    }

    //valida que la fecha no sea pasada
    public boolean esFuturo() {
        return !fecha.isBefore(LocalDate.now());
    }
}
