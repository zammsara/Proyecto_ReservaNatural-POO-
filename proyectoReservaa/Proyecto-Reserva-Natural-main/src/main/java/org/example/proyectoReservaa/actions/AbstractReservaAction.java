package org.example.proyectoReservaa.actions;

import org.example.proyectoReservaa.model.reservas.EstadoReserva;
import org.example.proyectoReservaa.model.reservas.Reserva;
import org.openxava.actions.ViewBaseAction;
import org.openxava.jpa.XPersistence;

import javax.persistence.NoResultException;

public abstract class AbstractReservaAction extends ViewBaseAction {

    protected Reserva cargarReservaActual() {
        Object id = getView().getValue("id");
        if (id == null) return null;
        return XPersistence.getManager().find(Reserva.class, id);
    }

    protected void refrescarVista(Reserva reserva) {
        if (reserva != null) {
            XPersistence.getManager().flush();
            getView().refresh();
        }
    }
}
