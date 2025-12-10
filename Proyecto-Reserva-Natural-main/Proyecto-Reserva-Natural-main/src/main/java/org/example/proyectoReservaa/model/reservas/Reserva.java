package org.example.proyectoReservaa.model.reservas;

import lombok.Getter;
import lombok.Setter;
import org.example.proyectoReservaa.model.BaseEntity;
import org.example.proyectoReservaa.model.horarios.HorarioDiario;
import org.example.proyectoReservaa.model.pagos.Reembolso;
import org.example.proyectoReservaa.model.pagos.Pago;
import org.example.proyectoReservaa.model.estacionamiento.EstacionamientoReserva;
import org.example.proyectoReservaa.model.visitantes.Visitante;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
public class Reserva extends BaseEntity {
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaExpiracion;
    private LocalDateTime fechaCheckIn;
    private int totalVisitantes;
    private boolean estacionamientoRequerido = false;
    private String codigoReserva;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "id_visitanteResponsable", referencedColumnName = "id")
    private Visitante visitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_horarioDiario", referencedColumnName = "id")
    private HorarioDiario horario;

    @Enumerated(EnumType.STRING)
    private EstadoReserva estadoReserva;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservaVisitante> visitantes = new ArrayList<>();

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialEstadoReserva> historialEstados = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_pago", referencedColumnName = "id")
    private Pago pago;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reembolso> reembolsos = new ArrayList<>();

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EstacionamientoReserva> estacionamientos = new ArrayList<>();

    public void crearPendiente(Visitante visitante, HorarioDiario horario,
                               int cantidadVisitantes, boolean requiereEstacionamiento,
                               EstadoReserva estadoPendiente) {
        if (cantidadVisitantes <= 0) {
            throw new IllegalArgumentException("La reserva debe tener al menos un visitante.");
        }
        if (visitante == null) {
            throw new IllegalArgumentException("Se requiere un responsable para la reserva.");
        }
        if (horario == null) {
            throw new IllegalArgumentException("Se requiere un horario valido.");
        }
        //validar mayor de edad
        visitante.validarMayorDeEdad();
        //validar disponibilidad de cupos
        horario.validarDisponibilidad(cantidadVisitantes);

        this.visitante = visitante;
        this.horario = horario;
        this.totalVisitantes = cantidadVisitantes;
        this.estacionamientoRequerido = requiereEstacionamiento;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaExpiracion = fechaCreacion.plusMinutes(15);
        this.codigoReserva = generarCodigoReserva();

        //descontar cupos
        horario.descontarCupos(cantidadVisitantes);
        //actualizacion del estado
        actualizarEstado(estadoPendiente, OrigenCambioEstado.VISITANTE, "Reserva creada y cupos retenidos", false);
    }

    public boolean estaConfirmada() {
        return estadoReserva == EstadoReserva.CONFIRMADA;
    }

    public boolean estaCancelada() {
        return estadoReserva == EstadoReserva.CANCELADA;
    }

    public boolean estaExpiradaPorTiempo() {
        return fechaExpiracion != null && LocalDateTime.now().isAfter(fechaExpiracion)
            && !estaConfirmada();
    }

    public void confirmarPago(Pago pago, EstadoReserva estadoConfirmada) {
        if (pago == null || !pago.estaPagado()) {
            throw new IllegalStateException("El pago debe estar aprobado para confirmar la reserva.");
        }
        this.pago = pago;
        pago.setReserva(this);
        actualizarEstado(estadoConfirmada, OrigenCambioEstado.VISITANTE, "Pago confirmado", false);
    }

    public void expirar(EstadoReserva estadoExpirada) {
        if (estaConfirmada() || estaCancelada()) return;
        if (horario != null) {
            horario.aumentarCupos(totalVisitantes);
        }
        actualizarEstado(estadoExpirada, OrigenCambioEstado.USUARIO_SISTEMA, "Pago no realizado en el tiempo limite", false);
    }

    public void cancelar(EstadoReserva estadoCancelada, OrigenCambioEstado origen, String motivo, LocalDate fechaCancelacion) {
        if (estaCancelada()) return;
        boolean esCancelacionSistema = origen == OrigenCambioEstado.USUARIO_SISTEMA;
        boolean reembolsable = esReembolsable(fechaCancelacion, esCancelacionSistema);

        if (horario != null) {
            horario.aumentarCupos(totalVisitantes);
        }
        actualizarEstado(estadoCancelada, origen, motivo, reembolsable);

        if (reembolsable && pago != null) {
            Reembolso reembolso = new Reembolso();
            reembolso.setReserva(this);
            reembolso.setOrigen(origen);
            reembolso.setMotivo(motivo);
            reembolso.setMonto(pago.getMontoTotal());
            this.reembolsos.add(reembolso);
        }
    }


    public void registrarCheckIn(String codigoIngresado, EstadoReserva estadoAsistida) {
        if (!estaConfirmada()) {
            throw new IllegalStateException("Solo las reservas pagadas pueden hacer check-in.");
        }
        if (estaCancelada() || estaExpiradaPorTiempo()) {
            throw new IllegalStateException("La reserva no es valida para check-in.");
        }
        if (horario != null && horario.getFecha() != null && !LocalDate.now().isEqual(horario.getFecha())) {
            throw new IllegalStateException("El check-in solo es valido en la fecha reservada.");
        }
        if (codigoReserva == null || !codigoReserva.equalsIgnoreCase(codigoIngresado)) {
            throw new IllegalArgumentException("Codigo de reserva invalido.");
        }
        this.fechaCheckIn = LocalDateTime.now();
        actualizarEstado(estadoAsistida, OrigenCambioEstado.USUARIO_SISTEMA, "Check-in realizado", false);
    }

    public void marcarNoAsistida(EstadoReserva estadoNoAsistida) {
        if (estaConfirmada() && fechaCheckIn == null) {
            actualizarEstado(estadoNoAsistida, OrigenCambioEstado.USUARIO_SISTEMA, "Visitante no llego a tiempo", false);
        }
    }

    public boolean esReembolsable(LocalDate fechaCancelacion, boolean canceladoPorSistema) {
        if (horario == null || horario.getFecha() == null) return false;
        if (canceladoPorSistema) return true;
        LocalDate fechaReferencia = fechaCancelacion != null ? fechaCancelacion : LocalDate.now();
        long diasAnticipacion = ChronoUnit.DAYS.between(fechaReferencia, horario.getFecha());
        return diasAnticipacion >= 3;
    }

    private void actualizarEstado(EstadoReserva nuevoEstado, OrigenCambioEstado origen, String motivo, boolean reembolsable) {
        EstadoReserva anterior = this.estadoReserva;
        this.estadoReserva = nuevoEstado;

        HistorialEstadoReserva registro = new HistorialEstadoReserva();
        registro.setReserva(this);
        registro.setEstadoAnterior(anterior);
        registro.setEstadoNuevo(nuevoEstado);
        registro.setFechaCambio(LocalDateTime.now());
        registro.setOrigenCambio(origen.name()); //porque es String en la entidad
        registro.setMotivo(motivo);

        this.historialEstados.add(registro);
    }

    private String generarCodigoReserva() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
