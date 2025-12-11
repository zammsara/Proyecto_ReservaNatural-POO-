package org.example.proyectoReservaa.actions;

import org.example.proyectoReservaa.model.pagos.EstadoPago;
import org.example.proyectoReservaa.model.pagos.Pago;
import org.example.proyectoReservaa.model.reservas.EstadoReserva;
import org.example.proyectoReservaa.model.reservas.Reserva;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReservaConfirmarPagoAction extends AbstractReservaAction {

    @Override
    public void execute() throws Exception {
        Reserva reserva = cargarReservaActual();
        if (reserva == null) {
            addError("Guarde primero la reserva para confirmarla.");
            return;
        }

        try {
            Pago pago = reserva.getPago();
            if (pago == null) {
                pago = new Pago();
                pago.setProveedor("MANUAL");
                pago.setMontoTotal(BigDecimal.ZERO);
                pago.setEstado(EstadoPago.APROBADO);
                pago.setFechaPago(LocalDateTime.now());
                pago.setReserva(reserva);
            }
            EstadoReserva estadoConfirmada = EstadoReserva.CONFIRMADA;
            reserva.confirmarPago(pago, estadoConfirmada);
            addMessage("Reserva confirmada con pago aprobado.");
            refrescarVista(reserva);
        } catch (Exception ex) {
            addError(ex.getMessage());
        }
    }
}