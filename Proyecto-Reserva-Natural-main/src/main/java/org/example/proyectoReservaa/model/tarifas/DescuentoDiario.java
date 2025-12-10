package org.example.proyectoReservaa.model.tarifas;

import lombok.Getter;
import lombok.Setter;
import org.example.proyectoReservaa.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.DayOfWeek;

@Entity
@Getter
@Setter
public class DescuentoDiario extends BaseEntity {

    private DiaSemana diaSemana;

    @Column(precision = 5, scale = 2)
    private BigDecimal descuento; // Solo un descuento por dia

    public BigDecimal aplicar(BigDecimal montoBase) {
        if (montoBase == null || descuento == null) return montoBase;
        return montoBase.subtract(montoBase.multiply(descuento));
    }

    private EstadoDescuento estado;
}
