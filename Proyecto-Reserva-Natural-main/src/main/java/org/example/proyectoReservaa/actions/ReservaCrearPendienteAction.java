package org.example.proyectoReservaa.actions;

import org.example.proyectoReservaa.model.reservas.EstadoReserva;
import org.example.proyectoReservaa.model.reservas.Reserva;

public class ReservaCrearPendienteAction extends AbstractReservaAction {

    @Override
    public void execute() throws Exception {
        Reserva reserva = cargarReservaActual();
        if (reserva == null) {
            addError("Guarde primero la reserva para generar el pendiente.");
            return;
        }

        try {
            // Directamente asignamos el enum
            EstadoReserva estadoPendiente = EstadoReserva.PENDIENTE;

            reserva.crearPendiente(
                    reserva.getVisitante(),
                    reserva.getHorario(),
                    reserva.getTotalVisitantes(),
                    reserva.isEstacionamientoRequerido(),
                    estadoPendiente
            );

            addMessage("Reserva marcada como Pendiente. Cupos retenidos 15 minutos.");
            refrescarVista(reserva);

        } catch (Exception ex) {
            addError(ex.getMessage());
        }
    }
}
