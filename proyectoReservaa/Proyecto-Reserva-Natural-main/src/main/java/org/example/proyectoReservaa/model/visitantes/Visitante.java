package org.example.proyectoReservaa.model.visitantes;

import lombok.Getter;
import lombok.Setter;
import org.example.proyectoReservaa.model.BaseEntity;
import org.openxava.annotations.DescriptionsList;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Getter
@Setter
public class Visitante extends BaseEntity {

    private String nombreCompleto;
    private TipoDocumento tipoDocumento;
    private String documentoIdentidad;
    private LocalDate fechaNacimiento;
    private String correo;
    private String telefono;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "id_tipoVisitante", referencedColumnName = "id")
    @DescriptionsList(descriptionProperties = "nombre")
    private TipoVisitante tipoVisitante;

    public int getEdadEnAnios() {
        if (fechaNacimiento == null) return 0;
        return LocalDate.now().getYear() - fechaNacimiento.getYear()
            - (LocalDate.now().getDayOfYear() < fechaNacimiento.getDayOfYear() ? 1 : 0);
    }

    public void validarMayorDeEdad() {
        if (getEdadEnAnios() < 18) {
            throw new IllegalArgumentException("El visitante responsable debe ser mayor de edad.");
        }
    }
}
