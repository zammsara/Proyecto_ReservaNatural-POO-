package org.example.proyectoReservaa.actions;

import org.example.proyectoReservaa.model.reservas.EstadoReserva;
import org.example.proyectoReservaa.model.reservas.Reserva;

public class ReservaNoAsistidaAction extends AbstractReservaAction {

    @Override
    public void execute() throws Exception {
        Reserva reserva = cargarReservaActual();
        if (reserva == null) {
            addError("Seleccione o guarde una reserva para marcarla como no asistida.");
            return;
        }
        try {
            // Usamos directamente el enum
            reserva.marcarNoAsistida(EstadoReserva.NOASISTIDA);

            addMessage("Reserva marcada como No Asistida.");
            refrescarVista(reserva);
        } catch (Exception ex) {
            addError(ex.getMessage());
        }
    }
}
