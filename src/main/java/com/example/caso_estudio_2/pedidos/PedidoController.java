package com.example.caso_estudio_2.pedidos;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class PedidoController {

    @FXML private Button btnIniciar;
    @FXML private Button btnCancelar;
    @FXML private ProgressBar barraProgreso;
    @FXML private Label lblContador;
    @FXML private Label lblEstado;

    private ProcesarPedido tareaActual;

    @FXML
    private void initialize() {
        ponerEstado("Listo");
        lblContador.setText("0 / 30");
        barraProgreso.setProgress(0);
    }

    @FXML
    private void onIniciar() {
        // Impedir inicio duplicado
        if (tareaActual != null && tareaActual.isRunning()) {
            return;
        }

        // Reiniciar contador solo al comenzar una nueva ejecución
        lblContador.setText("0 / 30");
        barraProgreso.setProgress(0);

        tareaActual = new ProcesarPedido();

        // Bindings seguros
        barraProgreso.progressProperty().bind(tareaActual.progressProperty());

        // Actualizar contador cuando el Task reporta progreso
        tareaActual.messageProperty().addListener((obs, old, nuevo) -> {
            if (nuevo != null && !nuevo.isEmpty()) {
                lblContador.setText(nuevo + " / 30");
            }
        });

        // Estados según el ciclo de vida del Task
        tareaActual.setOnRunning(e -> {
            ponerEstado("En ejecución");
            btnIniciar.setDisable(true);
            btnCancelar.setDisable(false);
        });

        tareaActual.setOnSucceeded(e -> {
            ponerEstado("Completado");
            finalizarTarea();
        });

        tareaActual.setOnCancelled(e -> {
            ponerEstado("Cancelado");
            finalizarTarea();
        });

        tareaActual.setOnFailed(e -> {
            ponerEstado("Error");
            finalizarTarea();
        });

        // Arrancar en hilo de fondo
        Thread hilo = new Thread(tareaActual);
        hilo.setDaemon(true);
        hilo.start();
    }

    @FXML
    private void onCancelar() {
        if (tareaActual != null && tareaActual.isRunning()) {
            ponerEstado("Cancelando");
            tareaActual.cancel(true); // cancelación cooperativa
            btnCancelar.setDisable(true);
            // El botón Iniciar se habilita solo cuando el Task realmente termina
            // (en setOnCancelled / setOnSucceeded)
        }
    }

    /** Llamado cuando la tarea termina (éxito, cancelación o error) */
    private void finalizarTarea() {
        btnIniciar.setDisable(false);
        btnCancelar.setDisable(true);
        // Desvincular para evitar problemas en la siguiente ejecución
        barraProgreso.progressProperty().unbind();
    }

    private void ponerEstado(String estado) {
        lblEstado.setText(estado);
    }

    /** Se llama al cerrar la ventana → cancela sin bloquear el hilo JavaFX */
    public void cerrarAplicacion() {
        if (tareaActual != null && tareaActual.isRunning()) {
            tareaActual.cancel(true);
        }
    }
}
