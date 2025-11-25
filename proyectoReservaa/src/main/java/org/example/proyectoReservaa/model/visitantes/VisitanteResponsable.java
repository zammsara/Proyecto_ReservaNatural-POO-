package org.example.proyectoReservaa.model.visitantes;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Setter
@Getter
public class VisitanteResponsable extends Visitante {
    private String correo;
    private String telefono;
}
