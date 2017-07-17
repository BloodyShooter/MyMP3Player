package org.gvozdetscky.view;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by Egor on 17.07.2017.
 */
public class MainWindow extends Application {

    private static final String NAME_APPLICATION = "MP3Player";
    private static final int WIDTH = 300;
    private static final int HEIGHT = 600;

    public static void launchMyApp(String[] args) {
        launch(args);
    }

    public void start(Stage myStage) throws Exception {

        myStage.setScene(new Scene(initialAndGetComp(), WIDTH, HEIGHT));

        myStage.setTitle(NAME_APPLICATION);
        myStage.setResizable(false);
        myStage.show();
    }

    private Parent initialAndGetComp() {

        VBox root = new VBox(10);

        FlowPane firstLevel = new FlowPane(10, 10);
        FlowPane secondLevel = new FlowPane();

        TextField txtSearch = new TextField();
        txtSearch.setPromptText("Введите имя для поиска");
        txtSearch.setTooltip(new Tooltip("Введите имя файла для поиска"));
        Button btnSearch = new Button("ПОИСК");

        firstLevel.getChildren().addAll(txtSearch, btnSearch);

        Button btnPlay = new Button("Play");

        secondLevel.getChildren().addAll(btnPlay);

        root.getChildren().addAll(firstLevel, secondLevel);

        return root;
    }
}
