package com.client;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class HelloController {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField usernameText;
    @FXML
    private  TextField messageText;
    @FXML
    private Button sendButton;
    @FXML
    private Button exitButton;

    private DataOutputStream dout = null;
    private DataInputStream din = null;
    private BufferedWriter bufferedWriter = null;
    private Socket sock;

    public void initialize(){
        try {
            sock = new Socket ("localhost", 6666);
            System.out.println("Connected to server...");
            System.out.println("Input \"done\" to kill connection...");
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
        } catch(IOException h){
            System.out.println(h);
            System.out.println("Unable to connect to server");
        }

        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String message = messageText.getText();
                if (!message.isEmpty()){
                    try{
                        bufferedWriter.write(usernameText.getText() + ": " + message);
                        //System.out.println(usernameText.getText() + ": " + message);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    } catch (IOException e){
                        e.printStackTrace();
                        System.out.println("Error sending message to server");
                    }
                    messageText.clear();

                }
            }
        });

        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    sock.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Platform.exit();
                System.exit(0);
            }
        });
    }
}