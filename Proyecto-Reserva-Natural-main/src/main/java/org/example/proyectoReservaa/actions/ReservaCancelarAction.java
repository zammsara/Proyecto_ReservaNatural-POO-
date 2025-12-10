package org.example.proyectoReservaa.actions;

import org.example.proyectoReservaa.model.reservas.EstadoReserva;
import org.example.proyectoReservaa.model.reservas.OrigenCambioEstado;
import org.example.proyectoReservaa.model.reservas.Reserva;

import java.time.LocalDate;

public class ReservaCancelarAction extends AbstractReservaAction {

    @Override
    public void execute() throws Exception {
        Reserva reserva = cargarReservaActual();
        if (reserva == null) {
            addError("Seleccione o guarde una reserva para cancelar.");
            return;
        }
        try {
            // Usamos directamente el enum
            reserva.cancelar(EstadoReserva.CANCELADA, OrigenCambioEstado.USUARIO_SISTEMA,
                    "Cancelada manualmente", LocalDate.now());

            addMessage("Reserva cancelada. Cupos liberados y reembolso evaluado.");
            refrescarVista(reserva);
        } catch (Exception ex) {
            addError(ex.getMessage());
        }
    }
}
