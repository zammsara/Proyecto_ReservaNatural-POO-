package org.example.proyectoReservaa.model.estacionamiento;

import lombok.Getter;
import lombok.Setter;
import org.example.proyectoReservaa.model.BaseEntity;
import org.example.proyectoReservaa.model.reservas.Reserva;
import org.openxava.annotations.DescriptionsList;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
public class EstacionamientoReserva extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserva_id", referencedColumnName = "id")
    @DescriptionsList(descriptionProperties = "id")
    private Reserva reserva;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "id_tipoVehiculo", referencedColumnName = "id")
    @DescriptionsList(descriptionProperties = "id")
    private TipoVehiculo tipoVehiculo;

    private int cantidad;

}
