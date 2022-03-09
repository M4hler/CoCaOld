package com.cthulhu.scenes;

import com.cthulhu.Application;
import com.cthulhu.events.EventType;
import com.cthulhu.events.client.EventInvestigators;
import com.cthulhu.models.Investigator;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

public class FirstScene implements CoCaScene {
    private Application application;
    private Scene scene;
    private final VBox vBox;
    private final MessageProducer producer;
    private final Session session;

    public FirstScene(Application application, MessageProducer producer, Session session) {
        this.application = application;
        this.producer = producer;
        this.session = session;

        vBox = new VBox();
        Button b = new Button("Refresh data");
        b.setOnAction(this::buttonAction);
        vBox.getChildren().add(b);

        scene = new Scene(vBox, 800, 600);
        addGridPane(Investigator.builder().name("Alice").strength(60).dexterity(50).build());
        application.changeScene(scene);
    }

    public void addGridPane(Investigator investigator) {
        GridPane gridPane = new GridPane();
        gridPane.add(new Label("Name"), 0, 0);
        gridPane.add(new Label(investigator.getName()), 1, 0);
        gridPane.add(new Label("Strength"), 0, 1);
        gridPane.add(new Label(String.valueOf(investigator.getStrength())), 1, 1);
        gridPane.add(new Label("Dexterity"), 2, 1);
        gridPane.add(new Label(String.valueOf(investigator.getDexterity())), 3, 1);

        Platform.runLater(() -> vBox.getChildren().add(gridPane));
    }

    private void buttonAction(ActionEvent actionEvent) {
        try {
            EventInvestigators event = new EventInvestigators();
            event.setEventType(EventType.INVESTIGATORS);
            ObjectMapper objectMapper = new ObjectMapper();
            Message message = session.createTextMessage(objectMapper.writeValueAsString(event));
            producer.send(message);
        }
        catch(Exception e) {
            System.out.println("Something went wrong");
        }
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
