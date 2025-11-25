package org.example.proyectoReservaa.model.horarios;

import lombok.Getter;
import lombok.Setter;
import org.example.proyectoReservaa.model.BaseEntity;
import org.openxava.annotations.Required;
import org.openxava.annotations.View;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@View(members=
        "horarioReserva;" +
                "fechaInicio, fechaFin;" +
                "motivo;" +
                "otroMotivo"
)
//Representa un periodo donde un HorarioReserva queda deshabilitado
public class RangoDeshabilitacionHorario extends BaseEntity {
    @Required
    private LocalDate fechaInicio;
    @Required
    private LocalDate fechaFin;

    @Enumerated(EnumType.STRING)
    @Required
    private MotivoDeshabilitacion motivo;

    //Solo se usa si el usuario selecciona "OTRO"
    private String otroMotivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_horario_reserva")
    private HorarioReserva horarioReserva;

    public boolean estaDentroDelRango(LocalDate fecha) {
        return !fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaFin);
    }

    public boolean isOtroMotivoVisible() {
        return motivo == MotivoDeshabilitacion.OTRO;
    }

    //Setter inteligente para limpiado automatico
    public void setMotivo(MotivoDeshabilitacion motivo) {
        this.motivo = motivo;
        if (motivo != MotivoDeshabilitacion.OTRO) {
            this.otroMotivo = null;
        }
    }

    //Validaciones
    @AssertTrue(message = "La fecha final debe ser igual o posterior a la fecha inicial")
    public boolean isRangoFechaValido() {
        if (fechaInicio == null || fechaFin == null) return true;
        return !fechaFin.isBefore(fechaInicio);
    }

    @AssertTrue(message = "El rango debe estar dentro del año permitido para reservas")
    public boolean isRangoDentroDelAnioDinamico() {
        if (fechaInicio == null || fechaFin == null) return true;

        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusYears(1);

        return !fechaInicio.isBefore(hoy) && !fechaFin.isAfter(limite);
    }

    @AssertTrue(message = "Debe especificar el motivo cuando selecciona 'OTRO'")
    public boolean isOtroMotivoValido() {
        if (motivo == MotivoDeshabilitacion.OTRO) {
            return otroMotivo != null && !otroMotivo.isBlank();
        }
        return true;
    }
}
