package org.example.proyectoReservaa.actions;

import org.example.proyectoReservaa.model.reservas.EstadoReserva;
import org.example.proyectoReservaa.model.reservas.Reserva;

public class ReservaExpirarAction extends AbstractReservaAction {

    @Override
    public void execute() throws Exception {
        Reserva reserva = cargarReservaActual();
        if (reserva == null) {
            addError("Seleccione o guarde una reserva para expirar.");
            return;
        }

        try {
            // Usamos directamente el enum
            reserva.expirar(EstadoReserva.EXPIRADA);

            addMessage("Reserva expirada manualmente. Cupos liberados si correspondia.");
            refrescarVista(reserva);
        } catch (Exception ex) {
            addError(ex.getMessage());
        }
    }
}
