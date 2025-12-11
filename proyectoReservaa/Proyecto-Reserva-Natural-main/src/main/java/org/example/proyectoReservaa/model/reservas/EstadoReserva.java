package org.example.proyectoReservaa.model.reservas;

import lombok.Getter;
import lombok.Setter;
import org.example.proyectoReservaa.model.BaseEntity;
import org.openxava.annotations.TextArea;

import javax.persistence.Entity;


public enum EstadoReserva {

   PENDIENTE,
    EXPIRADA,
    CONFIRMADA,
    CANCELADA,
    ASISTIDA,
    NOASISTIDA
}
