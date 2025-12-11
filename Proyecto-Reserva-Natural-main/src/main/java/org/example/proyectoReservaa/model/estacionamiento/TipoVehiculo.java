package org.example.proyectoReservaa.model.estacionamiento;

import lombok.Getter;
import lombok.Setter;
import org.example.proyectoReservaa.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class TipoVehiculo extends BaseEntity {

    @Column
    private String nombreTipoVehiculo;

    @Column(precision = 5, scale = 2)
    private BigDecimal tarifaUnitaria;
}
