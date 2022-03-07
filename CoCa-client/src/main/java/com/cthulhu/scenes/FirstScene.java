package com.cthulhu.scenes;

import com.cthulhu.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class FirstScene implements CoCaScene {
    private Application application;
    private Scene scene;
    private int counter;
    private ObservableList<Button> observableButtons;

    public FirstScene(Application application) {
        this.application = application;
        counter = 0;

        VBox vBox = new VBox();
        Button b = new Button("Click me");
        b.setOnAction(this::buttonAction);
        vBox.getChildren().add(b);

        List<Button> buttons = new ArrayList<>();
        observableButtons = FXCollections.observableList(buttons);
        observableButtons.addListener((ListChangeListener<Button>) change -> {
                    vBox.getChildren().add(new Button("Button " + counter));
                    counter++;
                }
        );

        scene = new Scene(vBox, 800, 600);
        application.changeScene(scene);
    }

    private void buttonAction(ActionEvent actionEvent) {
        observableButtons.add(new Button());
    }

    @Override
    public Scene getScene() {
        return scene;
    }
}
