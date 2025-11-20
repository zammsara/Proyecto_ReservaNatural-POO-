package org.example.proyectoReservaa.model.reservas;

import lombok.Getter;
import lombok.Setter;
import org.example.proyectoReservaa.model.BaseEntity;

import javax.persistence.Entity;

@Entity
@Setter
@Getter
public class EstadoReserva extends BaseEntity {

    // PendientePago, Confirmada, Expirada, Cancelada, Asistida
    private String nombreEstado;
    private String descripcion;

}
