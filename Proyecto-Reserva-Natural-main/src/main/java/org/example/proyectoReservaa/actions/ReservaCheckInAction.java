package org.example.proyectoReservaa.actions;

import org.example.proyectoReservaa.model.reservas.EstadoReserva;
import org.example.proyectoReservaa.model.reservas.Reserva;

public class ReservaCheckInAction extends AbstractReservaAction {

    @Override
    public void execute() throws Exception {
        Reserva reserva = cargarReservaActual();
        if (reserva == null) {
            addError("Seleccione o guarde una reserva para hacer check-in.");
            return;
        }
        try {
            String codigoIngresado = (String) getView().getValue("codigoReserva");
            if (codigoIngresado == null || codigoIngresado.isBlank()) {
                codigoIngresado = reserva.getCodigoReserva();
            }

            // Usamos directamente el enum
            reserva.registrarCheckIn(codigoIngresado, EstadoReserva.ASISTIDA);

            addMessage("Check-in exitoso.");
            refrescarVista(reserva);
        } catch (Exception ex) {
            addError(ex.getMessage());
        }
    }
}
