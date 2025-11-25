package org.example.proyectoReservaa.model.horarios;

import lombok.Getter;
import lombok.Setter;
import org.example.proyectoReservaa.model.BaseEntity;
import org.example.proyectoReservaa.model.reservas.Reserva;
import org.openxava.annotations.View;
import org.openxava.annotations.Required;
import org.openxava.annotations.ListProperties;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@View(members =
        "horarioBase, fecha;" +
                "estado, motivoCambio;" +
                "cuposDisponibles;" +
                "reservas"
)
//Representa el horario en una fecha especifica
public class HorarioDiario extends BaseEntity {
    @Required
    private LocalDate fecha;
    private int cuposDisponibles;

    @Enumerated(EnumType.STRING)
    private EstadoHorario estado;  //Solo afecta este dia

    private String motivoCambio; //mantenimiento, emergencia, etc

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_horario_reserva")
    private HorarioReserva horarioBase;

    //Inicializa los cupos segun la capacidad global
    public void inicializarCupos() {
        this.cuposDisponibles = horarioBase.getCapacidadVisitantes();
    }

    //Logica
    public boolean tieneCupos(int cantidad) {
        return cuposDisponibles >= cantidad;
    }

    public void descontarCupos(int cantidad) {
        if (cantidad > 0) cuposDisponibles -= cantidad;
    }

    public void aumentarCupos(int cantidad) {
        if (cantidad > 0) {
            cuposDisponibles += cantidad;
            if (horarioBase != null && cuposDisponibles > horarioBase.getCapacidadVisitantes()) {
                cuposDisponibles = horarioBase.getCapacidadVisitantes();
            }
        }
    }

     //El horario diario está habilitado SI:
     //Su propio estado = HABILITADO
     //La fecha no está en rango deshabilitado global
     //La fecha está en el rango de 1 año
     public boolean estaHabilitado() {
         return estado == EstadoHorario.HABILITADO &&
                 horarioBase.estaHabilitadoParaFecha(this.fecha);
     }

    //Validaciones
    @AssertTrue(message = "No se pueden gestionar horarios de fechas pasadas")
    public boolean isFechaFutura() {
        if (fecha == null) return true;
        return !fecha.isBefore(LocalDate.now());
    }

    @AssertTrue(message = "Los cupos no pueden ser negativos")
    public boolean isCuposNoNegativos() {
        return cuposDisponibles >= 0;
    }

    @AssertTrue(message = "Los cupos no pueden exceder la capacidad del horario")
    public boolean isCuposNoExcedenCapacidad() {
        if (horarioBase == null) return true;
        return cuposDisponibles <= horarioBase.getCapacidadVisitantes();
    }

    @AssertTrue(message = "Debe especificar el motivo del cambio cuando el horario está inhabilitado")
    public boolean isMotivoRequeridoSiInhabilitado() {
        if (estado == EstadoHorario.INHABILITADO) {
            return motivoCambio != null && !motivoCambio.isBlank();
        }
        return true;
    }

    public boolean isMotivoCambioVisible() {
        return estado == EstadoHorario.INHABILITADO;
    }
}
