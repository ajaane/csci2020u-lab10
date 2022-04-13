package com.example.server;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import static com.example.server.HelloController.addLabel;

public class HelloController{
    @FXML
    private Button exitButton;
    @FXML
    private ScrollPane sp;
    @FXML
    private VBox vb;


    private DataInputStream inStream = null;
    private Socket sock = null;
    private BufferedReader bufferedReader = null;

    @FXML
    public void initialize(){
        new Thread(()-> {
            try {
                ServerSocket serverSocket = new ServerSocket(6666);

                while(true) {
                    Socket socket = serverSocket.accept();
                    new Thread(new ClientThread(socket)).start();
                }
            } catch(Exception e) {
                System.out.println(e + "\n");
            }
        }).start();

        vb.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sp.setVvalue((Double) newValue);
            }
        });
    }


    public static void addLabel(String messageFromClient, VBox vb){
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(5,5,5,10));

        Text text = new Text(messageFromClient);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setPadding(new Insets(5,10,5,10));
        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vb.getChildren().add(hBox);
            }
        });
    }

    public void exit(){
        Platform.exit();
        System.exit(0);

    }

    public class ClientThread implements Runnable {
        private final Socket socket;
        public ClientThread(Socket socket) { this.socket = socket; }
        @Override
        public void run() {
            try {
                DataInputStream inMsg = new DataInputStream(socket.getInputStream());
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while(true) {
                    String message = bufferedReader.readLine();
                    Platform.runLater(() -> addLabel(message, vb));
                }
            } catch(Exception e) {
                addLabel(e + "\n", vb);
            }
        }
    }
}
