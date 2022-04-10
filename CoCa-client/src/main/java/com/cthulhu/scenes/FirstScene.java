package com.cthulhu.scenes;

import com.cthulhu.Application;
import com.cthulhu.events.EventType;
import com.cthulhu.events.client.EventInvestigators;
import com.cthulhu.models.Investigator;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
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
        vBox.setAlignment(Pos.TOP_CENTER);
        Button b = new Button("Refresh data");
        b.setOnAction(this::buttonAction);
        vBox.getChildren().add(b);

        scene = new Scene(vBox, 900, 600);
        addGridPane(Investigator.builder().name("Alice Carter").strength(50).constitution(60).size(50).dexterity(40)
                .appearance(70).intelligence(80).power(60).education(50).sanity(70).luck(36).hitPoints(9).magicPoints(10).build());
        application.changeScene(scene);
    }

    public void addGridPane(Investigator investigator) {
        GridPane gridPane = new GridPane();
        //gridPane.setGridLinesVisible(true);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.add(new Label("Name"), 0, 0);
        gridPane.add(new Label(investigator.getName()), 1, 0);

        gridPane.add(new Label("Strength"), 2, 0);
        gridPane.add(new Label(String.valueOf(investigator.getStrength())), 3, 0);
        gridPane.add(new Label("Constitution"), 2, 1);
        gridPane.add(new Label(String.valueOf(investigator.getConstitution())), 3, 1);

        gridPane.add(new Label("Size"), 4, 0);
        gridPane.add(new Label(String.valueOf(investigator.getSize())), 5, 0);
        gridPane.add(new Label("Dexterity"), 4, 1);
        gridPane.add(new Label(String.valueOf(investigator.getDexterity())), 5, 1);

        gridPane.add(new Label("Appearance"), 6, 0);
        gridPane.add(new Label(String.valueOf(investigator.getAppearance())), 7, 0);
        gridPane.add(new Label("Intelligence"), 6, 1);
        gridPane.add(new Label(String.valueOf(investigator.getIntelligence())), 7, 1);

        gridPane.add(new Label("Power"), 8, 0);
        gridPane.add(new Label(String.valueOf(investigator.getPower())), 9, 0);
        gridPane.add(new Label("Education"), 8, 1);
        gridPane.add(new Label(String.valueOf(investigator.getEducation())), 9, 1);

        Separator separator = new Separator(Orientation.VERTICAL);
        gridPane.add(separator, 10, 0, 1, 2);

        gridPane.add(new Label("Sanity"), 11, 0);
        gridPane.add(new Label(String.valueOf(investigator.getSanity())), 12, 0);
        gridPane.add(new Label("Luck"), 11, 1);
        gridPane.add(new Label(String.valueOf(investigator.getLuck())), 12, 1);

        gridPane.add(new Label("Hit Points"), 13, 0);
        gridPane.add(new Label(String.valueOf(investigator.getHitPoints())), 14, 0);
        gridPane.add(new Label("Magic Points"), 13, 1);
        gridPane.add(new Label(String.valueOf(investigator.getMagicPoints())), 14, 1);

        Button readyButton = new Button("Not ready");
        readyButton.setOnAction(this::changeReadyStatus);

        gridPane.add(readyButton, 15, 0);

        ColumnConstraints standardConstraint = new ColumnConstraints();
        ColumnConstraints nameColumnConstraint = new ColumnConstraints(140, 140, 140, Priority.ALWAYS, HPos.CENTER, true);
        ColumnConstraints centralConstraint = new ColumnConstraints(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE,
                Control.USE_PREF_SIZE, Priority.ALWAYS, HPos.CENTER, true);
        ColumnConstraints buttonConstraint = new ColumnConstraints(100, 100, 100, Priority.ALWAYS, HPos.CENTER, true);

        for(int i = 0; i <= 15; i++) {
            if(i == 1) {
                gridPane.getColumnConstraints().add(nameColumnConstraint);
            }
            else if(i == 10) {
                gridPane.getColumnConstraints().add(centralConstraint);
            }
            else if(i == 15) {
                gridPane.getColumnConstraints().add(buttonConstraint);
            }
            else {
                gridPane.getColumnConstraints().add(standardConstraint);
            }
        }

        Platform.runLater(() -> vBox.getChildren().add(gridPane));
    }

    private void changeReadyStatus(ActionEvent actionEvent) {
        Button button = (Button)actionEvent.getSource();
        if(button.getText().equals("Not ready")) {
            button.setText("Ready");
        }
        else {
            button.setText("Not ready");
        }
        vBox.requestFocus();
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
