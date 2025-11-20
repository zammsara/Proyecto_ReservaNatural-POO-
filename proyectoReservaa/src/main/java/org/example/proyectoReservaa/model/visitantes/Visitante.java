package org.example.proyectoReservaa.model.visitantes;

import lombok.Getter;
import lombok.Setter;
import org.example.proyectoReservaa.model.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name= "tipo_visitante")
@Getter
@Setter
public abstract class Visitante extends BaseEntity {

    private String nombreCompleto;
    private TipoDocumento tipoDocumento;
    private String documentoIdentidad;
    private LocalDate fechaNacimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "id_tipoVisitante", referencedColumnName = "id")
    private TipoVisitante tipoVisitante;
}
