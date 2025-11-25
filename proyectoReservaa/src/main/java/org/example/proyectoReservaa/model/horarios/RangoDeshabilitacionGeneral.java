package org.example.proyectoReservaa.model.horarios;

import lombok.Getter;
import lombok.Setter;
import org.example.proyectoReservaa.model.BaseEntity;
import org.openxava.annotations.Required;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class RangoDeshabilitacionGeneral extends BaseEntity {
    @Required
    private LocalDate fechaInicio;
    @Required
    private LocalDate fechaFin;
    @Enumerated(EnumType.STRING)
    private MotivoDeshabilitacion motivo;
    private String otroMotivo;

    public boolean fechaDentro(LocalDate fecha) {
        return !fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaFin);
    }

    //Validaciones
    @AssertTrue(message = "El rango debe ser válido para reservas")
    public boolean isValido() {
        if (fechaInicio == null || fechaFin == null) return true;
        return !fechaFin.isBefore(fechaInicio);
    }
}

