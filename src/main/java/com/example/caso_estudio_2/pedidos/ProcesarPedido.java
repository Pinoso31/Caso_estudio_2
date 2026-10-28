package com.example.caso_estudio_2.pedidos;
import javafx.concurrent.Task;

/**
 * Task que simula el procesamiento de 30 pedidos.
 * Cada pedido tarda 200 ms. Usa cancelación cooperativa.
 */
public class ProcesarPedido extends Task<Void> {

    private static final int TOTAL_PEDIDOS = 30;
    private static final long tiempo_por_pedido = 200; // ms

    @Override
    protected Void call() throws Exception {
        for (int i = 0; i < TOTAL_PEDIDOS; i++) {

            // --- Cancelación cooperativa ---
            if (isCancelled()) {
                break;
            }

            // Simula el trabajo del pedido
            Thread.sleep(tiempo_por_pedido);

            // Solo contamos el pedido si no se canceló durante el sleep
            if (isCancelled()) {
                break;
            }

            // Actualizaciones thread-safe hacia la UI
            updateProgress(i + 1, TOTAL_PEDIDOS);
            updateMessage(String.valueOf(i + 1)); // usaremos este valor como contador
        }
        return null;
    }
}