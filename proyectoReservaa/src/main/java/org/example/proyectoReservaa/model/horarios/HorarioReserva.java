package org.example.proyectoReservaa.model.horarios;

import lombok.Getter;
import lombok.Setter;
import org.example.proyectoReservaa.model.BaseEntity;
import org.openxava.annotations.ListProperties;
import org.openxava.annotations.Required;
import org.openxava.annotations.View;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@View(members =
        "nombre, capacidadVisitantes;" +
                "horaInicio, horaFin;" +
                "horariosPorDia;" +
                "rangosDeshabilitados"
)

// Representa un horario base repetible durante muchos días
public class HorarioReserva extends BaseEntity {
    @Required
    private String nombre;
    @Required
    private LocalTime horaInicio;
    @Required
    private LocalTime horaFin;
    @Required
    private int capacidadVisitantes;

    @OneToMany(mappedBy = "horarioBase", cascade = CascadeType.ALL)
    @ListProperties("fecha, cuposDisponibles, estado, motivoCambio")
    private List<HorarioDiario> horariosPorDia;

    @Transient // No relación directa
    private List<RangoDeshabilitacionGeneral> rangosGlobales;

    //Logica para el año dinamico
    public boolean estaHabilitadoParaFecha(LocalDate fecha) {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusYears(1);

        //No permitir fechas fuera del rango dinámico
        if (fecha.isBefore(hoy) || fecha.isAfter(limite)) {
            return false;
        }

        //Deshabilitaciones globales
        for (RangoDeshabilitacionGeneral r : rangosGlobales) {
            if (r.fechaDentro(fecha)) {
                return false;
            }
        }
        return true;
    }

    //Validaciones
    @AssertTrue(message = "La hora de inicio debe ser anterior a la hora de fin")
    public boolean isRangoHorasValido() {
        if (horaInicio == null || horaFin == null) return true;
        return horaInicio.isBefore(horaFin);
    }

    @AssertTrue(message = "La capacidad debe ser mayor a cero")
    public boolean isCapacidadValida() {
        return capacidadVisitantes > 0;
    }


}
