package com.example.caso_estudio_2.pedidos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PedidoApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/caso_estudio_2/vistaPedido.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Sistema de Pedidos - Hilos JavaFX");
        stage.setScene(scene);
        stage.setResizable(false);

        // Cierre limpio: cancelar tarea al cerrar la ventana
        PedidoController controller = loader.getController();
        stage.setOnCloseRequest(e -> controller.cerrarAplicacion());

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
