package org.gvozdetscky.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by Egor on 17.07.2017.
 *
 * Main window application
 */
public class MainWindow extends Application {

    private static final String NAME_APPLICATION = "MP3Player";
    private static final int WIDTH = 344;
    private static final int HEIGHT = 594;

    /**
     * Метод запуска окна.
     * @param args - параметры при запуске приложения
     */
    public static void launchMyApp(String[] args) {
        launch(args);
    }

    public void start(Stage myStage) throws Exception {

        myStage.setScene(new Scene(initAndGetComp(), WIDTH, HEIGHT));

        myStage.setTitle(NAME_APPLICATION);
        myStage.setResizable(false);
        myStage.show();
    }

    /**
     * Метод создает компоненты и возврашает контейнер с создаными элементами.
     * @return Parent
     */
    private Parent initAndGetComp() {

        BorderPane rootNode = new BorderPane();

        VBox container = new VBox(30);

        Parent firstLevel = getFirstLevel();
        Parent secondLevel = getSecondLevel();

        container.getChildren().addAll(firstLevel, secondLevel);
        container.setAlignment(Pos.CENTER);

        rootNode.setCenter(container);

        rootNode.setTop(initialAndGetMenu());

        return rootNode;
    }

    /**
     * Метод создает и возваращает меню.
     * @return Node
     */
    private Node initialAndGetMenu() {
        MenuBar menuBar = new MenuBar();

        Menu file = new Menu("Файл");

        MenuItem exit = new MenuItem("Выход");
        exit.setOnAction(event -> Platform.exit());

        MenuItem open = new MenuItem("Открыть плейлист");

        MenuItem save = new MenuItem("Сохранить плейлист");

        file.getItems().addAll(open, save, new SeparatorMenuItem(), exit);

        menuBar.getMenus().add(file);

        return menuBar;
    }

    /**
     * Метод создает первый уровень контейнера и возвращает его.
     * @return Parent
     */
    private Parent getFirstLevel() {
        FlowPane firstLevel = new FlowPane(10, 10);

        TextField txtSearch = new TextField();
        txtSearch.setPrefColumnCount(20);
        txtSearch.setPromptText("Введите имя для поиска");
        txtSearch.setTooltip(new Tooltip("Введите имя файла для поиска"));
        Button btnSearch = new Button("ПОИСК",
                new ImageView(new Image(getClass().getResourceAsStream("/images/search_16.png"))));

        firstLevel.getChildren().addAll(txtSearch, btnSearch);

        firstLevel.setAlignment(Pos.CENTER);

        return firstLevel;
    }

    /**
     * Метод создает второй уровень контейнера и возвращает его.
     * @return Parent
     */
    private Parent getSecondLevel() {

        FlowPane secondLevel = new FlowPane();

        Button btnPlay = new Button("",
                new ImageView(new Image(getClass().getResourceAsStream("/images/Play.png"))));
        btnPlay.setTooltip(new Tooltip("Произвести"));
        Button btnPause = new Button("",
                new ImageView(new Image(getClass().getResourceAsStream("/images/Pause-icon.png"))));
        btnPause.setTooltip(new Tooltip("Поставить на паузу"));
        Button btnStop = new Button("",
                new ImageView(new Image(getClass().getResourceAsStream("/images/stop-red-icon.png"))));
        btnStop.setTooltip(new Tooltip("Останвить"));
        Button btnPrev = new Button("",
                new ImageView(new Image(getClass().getResourceAsStream("/images/prev-icon.png"))));
        btnPrev.setTooltip(new Tooltip("Предыдущая"));
        Button btnNext = new Button("",
                new ImageView(new Image(getClass().getResourceAsStream("/images/next-icon.png"))));
        btnNext.setTooltip(new Tooltip("Следующая"));
        Button btnAddSong = new Button("",
                new ImageView(new Image(getClass().getResourceAsStream("/images/plus_16.png"))));
        btnAddSong.setTooltip(new Tooltip("Добавить песню"));
        Button btnDeleteSong = new Button("",
                new ImageView(new Image(getClass().getResourceAsStream("/images/remove_icon.png"))));
        btnDeleteSong.setTooltip(new Tooltip("Удалить песню"));
        Button btnSelectPrev = new Button("",
                new ImageView(new Image(getClass().getResourceAsStream("/images/arrow-up-icon.png"))));
        btnSelectPrev.setTooltip(new Tooltip("Выделить предыдущую песню"));
        Button btnSelectNext = new Button("",
                new ImageView(new Image(getClass().getResourceAsStream("/images/arrow-down-icon.png"))));
        btnSelectNext.setTooltip(new Tooltip("Выделить следующию песню"));

        ToggleButton tglBtnMute = new ToggleButton();
        final Image unselected = new Image(getClass().getResourceAsStream(
                "/images/speaker.png"
        ));
        final Image selected = new Image(getClass().getResourceAsStream(
                "/images/mute.png"
        ));
        final ImageView toggleImage = new ImageView();
        tglBtnMute.setGraphic(toggleImage);
        toggleImage.imageProperty().bind(Bindings
                .when(tglBtnMute.selectedProperty())
                .then(selected)
                .otherwise(unselected)
        );
        tglBtnMute.setTooltip(new Tooltip("Звук квл/выкл"));

        ListView<String> lstPlaylist = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList(
                "Song1", "Song2", "Song3", "Song4");
        lstPlaylist.setItems(items);

        ScrollPane scrollPane = new ScrollPane(lstPlaylist);
        lstPlaylist.setPrefSize(WIDTH - 20, HEIGHT - 200);

        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(100);
        slider.setValue(100);
        slider.setPrefWidth(WIDTH - 70);

        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);

        HBox firstContainerLevel = new HBox(30);
        firstContainerLevel.setAlignment(Pos.CENTER);
        firstContainerLevel.getChildren().addAll(
                btnAddSong,
                btnDeleteSong,
                separator,
                btnSelectPrev,
                btnSelectNext
        );

        HBox thirdContainerLevel = new HBox(10);
        thirdContainerLevel.setAlignment(Pos.CENTER);
        thirdContainerLevel.getChildren().addAll(
                tglBtnMute,
                slider
        );

        HBox fourthContainerLevel = new HBox(10);
        fourthContainerLevel.setAlignment(Pos.CENTER);
        fourthContainerLevel.getChildren().addAll(
                btnPrev,
                btnPlay,
                btnPause,
                btnStop,
                btnNext
        );

        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        container.getChildren().addAll(
                firstContainerLevel,
                scrollPane,
                thirdContainerLevel,
                fourthContainerLevel
        );

        secondLevel.getChildren().addAll(container);
        secondLevel.setAlignment(Pos.CENTER);

        return secondLevel;
    }
}
