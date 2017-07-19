package org.gvozdetscky.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import org.gvozdetscky.model.MP3;
import org.gvozdetscky.model.MP3Player;
import org.gvozdetscky.utils.MyUtils;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Egor on 17.07.2017.
 *
 * Main window application
 */
public class MainWindow extends Application implements BasicPlayerListener {

    private long duration;
    private int bytesLen;
    private String songName;

    private static final String NAME_APPLICATION = "MP3Player";
    private static final int WIDTH = 344;
    private static final int HEIGHT = 594;

    private final FileChooser fileChooser = new FileChooser();
    private final MP3Player player = new MP3Player();
    private ObservableList<MP3> items;
    private ListView<MP3> lstPlaylist;
    private TextField txtSearch;
    private Slider sliderVolume;
    private Slider slider;
    private Label lblCurrentSong;
    private ToggleButton tglBtnMute;

    private Stage myStage;

    /**
     * Метод запуска окна.
     * @param args - параметры при запуске приложения
     */
    public static void launchMyApp(String[] args) {
        launch(args);
    }

    public void start(Stage myStage) throws Exception {
        this.myStage = myStage;

        myStage.setScene(new Scene(initAndGetComp(), WIDTH, HEIGHT));

        myStage.setTitle(NAME_APPLICATION);
        myStage.setResizable(false);
        myStage.show();
    }

    @Override
    public void stop() throws Exception {
        player.stop();
        super.stop();
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

        MenuItem exit = new MenuItem("Выход",
                new ImageView(new Image("/images/exit.png")));
        exit.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
        exit.setOnAction(event -> Platform.exit());

        MenuItem open = new MenuItem("Открыть плейлист",
                new ImageView(new Image("/images/open-icon.png")));
        open.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        open.setOnAction(event -> menuOpenAction());

        MenuItem save = new MenuItem("Сохранить плейлист",
                new ImageView(new Image("/images/save_16.png")));
        save.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        save.setOnAction(event -> menuSaveAction());

        file.getItems().addAll(open, save, new SeparatorMenuItem(), exit);

        menuBar.getMenus().add(file);

        return menuBar;
    }

    private void menuSaveAction() {
        fileChooser.setTitle("Сохранение плейлиста");
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Файлы плейлиста", "*.pls")
        );
        File file = fileChooser.showSaveDialog(myStage);

        if (file != null) {
            MyUtils.serialize(items, file.getPath());
        }
    }

    private void menuOpenAction() {
        fileChooser.setTitle("Открытие плейлиста");
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Файлы плейлиста", "*.pls")
        );

        File file = fileChooser.showOpenDialog(myStage);

        if (file != null) {
            items.clear();
            List<MP3> items = MyUtils.deserialize(file.getPath());
            this.items.addAll(items);
        }
    }

    /**
     * Метод создает первый уровень контейнера и возвращает его.
     * @return Parent
     */
    private Parent getFirstLevel() {
        FlowPane firstLevel = new FlowPane(10, 10);

        txtSearch = new TextField();
        txtSearch.setPrefColumnCount(20);
        txtSearch.setPromptText("Введите имя для поиска");
        txtSearch.setTooltip(new Tooltip("Введите имя файла для поиска"));
        Button btnSearch = new Button("Поиск",
                new ImageView(new Image(getClass().getResourceAsStream("/images/search_16.png"))));
        btnSearch.setOnAction((ActionEvent event) -> btnSearchAction());

        firstLevel.getChildren().addAll(txtSearch, btnSearch);

        firstLevel.setAlignment(Pos.CENTER);

        return firstLevel;
    }

    private void btnSearchAction() {
        final String text = txtSearch.getText();

        if (text == null || text.trim().equals("")) return;

        List<Integer> itemIndexes = new ArrayList<>();

        for (MP3 item: this.items) {
            if (item.getName().toUpperCase().contains(text.toUpperCase())) {
                itemIndexes.add(this.items.indexOf(item));
            }
        }

        if (itemIndexes.size() == 0) {
            JOptionPane.showMessageDialog(null, "Поиск по строке \'" + text + "\' не дал результатов");
            txtSearch.requestFocus();
            txtSearch.selectAll();
            return;
        }

        lstPlaylist.getSelectionModel().clearSelection();
        for (Integer integer: itemIndexes) {
            lstPlaylist.getSelectionModel().select(integer);
        }
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
        btnPlay.setOnAction((ActionEvent event) -> btnPlayAction());

        Button btnPause = new Button("",
                new ImageView(new Image(getClass().getResourceAsStream("/images/Pause-icon.png"))));
        btnPause.setTooltip(new Tooltip("Поставить на паузу"));
        btnPause.setOnAction((ActionEvent event) -> btnPauseAction());

        Button btnStop = new Button("",
                new ImageView(new Image(getClass().getResourceAsStream("/images/stop-red-icon.png"))));
        btnStop.setTooltip(new Tooltip("Останвить"));
        btnStop.setOnAction((ActionEvent event) -> btnStopAction());

        Button btnPrev = new Button("",
                new ImageView(new Image(getClass().getResourceAsStream("/images/prev-icon.png"))));
        btnPrev.setTooltip(new Tooltip("Предыдущая"));
        btnPrev.setOnAction((ActionEvent event) -> btnPrevPlayAction());

        Button btnNext = new Button("",
                new ImageView(new Image(getClass().getResourceAsStream("/images/next-icon.png"))));
        btnNext.setTooltip(new Tooltip("Следующая"));
        btnNext.setOnAction((ActionEvent event) -> btnNextPlaygAction());

        Button btnAddSong = new Button("",
                new ImageView(new Image(getClass().getResourceAsStream("/images/plus_16.png"))));
        btnAddSong.setTooltip(new Tooltip("Добавить песню"));
        btnAddSong.setOnAction((ActionEvent event) -> btnAddSongAction());

        Button btnDeleteSong = new Button("",
                new ImageView(new Image(getClass().getResourceAsStream("/images/remove_icon.png"))));
        btnDeleteSong.setTooltip(new Tooltip("Удалить песню"));
        btnDeleteSong.setOnAction((ActionEvent event) -> btnDeleteSongAction());

        Button btnSelectPrev = new Button("",
                new ImageView(new Image(getClass().getResourceAsStream("/images/arrow-up-icon.png"))));
        btnSelectPrev.setTooltip(new Tooltip("Выделить предыдущую песню"));
        btnSelectPrev.setOnAction((ActionEvent event) -> btnSelectPrevAction());

        Button btnSelectNext = new Button("",
                new ImageView(new Image(getClass().getResourceAsStream("/images/arrow-down-icon.png"))));
        btnSelectNext.setTooltip(new Tooltip("Выделить следующию песню"));
        btnSelectNext.setOnAction((ActionEvent event) -> btnSelectNextAction());

        tglBtnMute = new ToggleButton();
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
        tglBtnMute.setOnAction(event -> tglBtnMuteAction());

        lstPlaylist = new ListView<>();
        lstPlaylist.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        final ContextMenu menuOnListPlayList = new ContextMenu();

        MenuItem addSong = new MenuItem("Добавить песню");
        addSong.setOnAction((ActionEvent event) -> btnAddSongAction());

        MenuItem deleteSong = new MenuItem("Удалить песню");
        deleteSong.setOnAction((ActionEvent event) -> btnDeleteSongAction());

        MenuItem openPls = new MenuItem("Открыть плейлист");
        openPls.setOnAction((ActionEvent event) -> menuOpenAction());

        MenuItem clearPls = new MenuItem("Очистить плейлист");
        clearPls.setOnAction((ActionEvent event) -> items.clear());

        menuOnListPlayList.getItems().addAll(
                addSong,
                deleteSong,
                openPls,
                clearPls
        );

        lstPlaylist.setContextMenu(menuOnListPlayList);
        items = FXCollections.observableArrayList();
        lstPlaylist.setItems(items);

        ScrollPane scrollPane = new ScrollPane(lstPlaylist);
        lstPlaylist.setPrefSize(WIDTH - 20, HEIGHT - 240);
        lstPlaylist.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    btnPlayAction();
                }
        });

        sliderVolume = new Slider();
        sliderVolume.setMin(0);
        sliderVolume.setMax(200);
        sliderVolume.setMinorTickCount(5);
        sliderVolume.setValue(100);
        sliderVolume.setPrefWidth(WIDTH - 70);
        sliderVolume.valueProperty().addListener(observable -> slideVolumeStateChanged());

        slider = new Slider();
        slider.setMin(0);
        slider.setMax(200);
        slider.setMinorTickCount(5);
        slider.setValue(0);
        slider.setPrefWidth(WIDTH - 70);
        //slider.valueProperty().addListener(observable -> slideVolumeStateChanged());

        lblCurrentSong = new Label();
        lblCurrentSong.setText("...");

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
                sliderVolume
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
                lblCurrentSong,
                slider,
                thirdContainerLevel,
                fourthContainerLevel
        );

        secondLevel.getChildren().addAll(container);
        secondLevel.setAlignment(Pos.CENTER);

        return secondLevel;
    }

    private void tglBtnMuteAction() {
        if (tglBtnMute.isSelected()) {
            player.setVolume(0, sliderVolume.getMax());
        } else {
            player.setVolume(sliderVolume.getValue(), sliderVolume.getMax());
        }
    }

    private void btnNextPlaygAction() {
        int selectedNextIndex = lstPlaylist.getSelectionModel().getSelectedIndex() + 1;
        if (selectedNextIndex <= items.size() - 1) {
            Platform.runLater(() -> playSongOnSelectIndex(selectedNextIndex));
        }
    }

    private void btnPrevPlayAction() {
        int selectedPrevIndex = lstPlaylist.getSelectionModel().getSelectedIndex() - 1;
        if (selectedPrevIndex >= 0)
            Platform.runLater(() -> playSongOnSelectIndex(selectedPrevIndex));
    }

    private void playSongOnSelectIndex(int selectedPrevIndex) {
        lstPlaylist.getSelectionModel().clearSelection();
        lstPlaylist.getSelectionModel().select(selectedPrevIndex);
        MP3 selectedItem = lstPlaylist.getSelectionModel().getSelectedItem();
        player.play(selectedItem.getPath());
        player.setVolume(sliderVolume.getValue(), sliderVolume.getMax());
    }

    private void btnPlayAction() {
        MP3 selectedItem = lstPlaylist.getSelectionModel().getSelectedItem();
        player.play(selectedItem.getPath());
        player.setVolume(sliderVolume.getValue(), sliderVolume.getMax());
    }

    private void btnStopAction() {
        player.stop();
    }

    private void btnPauseAction() {
        player.pause();
    }

    private void slideVolumeStateChanged() {
        player.setVolume(sliderVolume.getValue(), sliderVolume.getMax());

        if (sliderVolume.getValue() == 0) {
            tglBtnMute.setSelected(true);
        } else {
            tglBtnMute.setSelected(false);
        }
    }

    private void btnSelectNextAction() {
        int selectedNextIndex = lstPlaylist.getSelectionModel().getSelectedIndex() + 1;
        if (selectedNextIndex <= items.size() - 1)
            Platform.runLater(() -> {
                lstPlaylist.getSelectionModel().clearSelection();
                lstPlaylist.getSelectionModel().select(selectedNextIndex);
            });
    }

    private void btnSelectPrevAction() {
        int selectedPrevIndex = lstPlaylist.getSelectionModel().getSelectedIndex() - 1;
        if (selectedPrevIndex >= 0)
            Platform.runLater(() -> {
                lstPlaylist.getSelectionModel().clearSelection();
                lstPlaylist.getSelectionModel().select(selectedPrevIndex);
            });
    }

    private void btnDeleteSongAction() {
        ObservableList<MP3> selectedList = lstPlaylist.getSelectionModel().getSelectedItems();
        if (!selectedList.isEmpty()) {
            items.removeAll(selectedList);
        }
    }

    private void btnAddSongAction() {
        fileChooser.setTitle("Выбор файлов");
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP3", "*.mp3")
        );
        List<File> list =
                fileChooser.showOpenMultipleDialog(myStage);
        if (list != null) {
            for (File file : list) {
                MP3 mp3 = new MP3(file.getName(), file.getPath());
                if (!items.contains(mp3))
                    items.add(mp3);
            }
        }
    }

    @Override
    public void opened(Object o, Map map) {
        duration = (long) Math.round((Long) map.get("duration") / 1000000);
        bytesLen = Math.round((Integer) map.get("mp3.length.bytes"));

        songName = map.get("title") != null ? map.get("title").toString(): new File(o.toString()).getName();

        if (songName.length() > 30) {
            songName = songName.substring(0, 30) + "...";
        }

        Platform.runLater(() -> lblCurrentSong.setText(songName));
        System.out.println("dsgsdg");
    }

    @Override
    public void progress(int i, long l, byte[] bytes, Map map) {

    }

    @Override
    public void stateUpdated(BasicPlayerEvent basicPlayerEvent) {

    }

    @Override
    public void setController(BasicController basicController) {

    }
}
