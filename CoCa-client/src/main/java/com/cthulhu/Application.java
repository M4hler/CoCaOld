package com.cthulhu;

import com.cthulhu.listeners.GeneralListener;
import com.cthulhu.scenes.FirstScene;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;

public class Application extends javafx.application.Application {
    private Stage stage;
    private MessageProducer producer;
    private MessageConsumer consumer;
    private GeneralListener generalListener;
    private Session session;

    @Override
    public void start(Stage primaryStage) throws Exception {
        setUpConnection();

        stage = new Stage();
        FirstScene firstScene = new FirstScene(this, producer, session);
        generalListener.setScene(firstScene);
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
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("testQueue");
        producer = session.createProducer(queue);
        consumer = session.createConsumer(queue);
        generalListener = new GeneralListener();
        consumer.setMessageListener(generalListener);
    }
}
