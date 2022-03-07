package com.cthulhu;

import com.cthulhu.listeners.GeneralListener;
import com.cthulhu.scenes.CoCaScene;
import com.cthulhu.scenes.FirstScene;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;

public class Application extends javafx.application.Application {
    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //setUpConnection();

        stage = new Stage();
        CoCaScene firstScene = new FirstScene(this);
        changeScene(firstScene.getScene());
    }

    public static void main(String[] args) {
        launch();
    }

    public void changeScene(Scene scene) {
        stage.setScene(scene);
        stage.show();
    }

    private void setUpConnection() throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("testQueue");
        MessageProducer producer = session.createProducer(queue);
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(new GeneralListener());
    }
}
