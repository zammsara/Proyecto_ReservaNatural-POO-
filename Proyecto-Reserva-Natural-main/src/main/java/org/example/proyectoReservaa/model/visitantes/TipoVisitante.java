package org.example.proyectoReservaa.model.visitantes;

import lombok.Getter;
import lombok.Setter;
import org.example.proyectoReservaa.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@Setter
@Getter
public class TipoVisitante extends BaseEntity {
    @Column(unique = true)
    private String nombre;
    private BigDecimal precioEntrada;
}
