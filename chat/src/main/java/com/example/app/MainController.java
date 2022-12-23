package com.example.app;

import com.example.dao.*;
import com.example.dao.impl.*;
import com.example.models.*;
import com.example.protocols.Message;
import com.example.protocols.Type;
import com.example.services.UserService;
import com.example.sockets.SocketClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainController implements Initializable {
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

    private SocketClient socketClient;
    private ScheduledExecutorService service;
    public LineChart<String, Number> chart;
    public TableView<PriceToVolume> stockGlass;
    public TableColumn<PriceToVolume, String> priceColumn;
    public TableColumn<PriceToVolume, String> volumeColumn;
    public TextField volumeTextField;
    public Label enterVolumeLabel;
    public Button buyButton;
    public Button sellButton;
    public CheckBox limitCheckBox;
    public TextField enterPriceTextField;
    public Label enterLimitPriceLabel;
    public Label balanceLabel;
    final int WINDOW_SIZE = 20;
    public TableView<GeneralPosition> positionTable;
    public TableColumn<GeneralPosition, String>  sideColumn;
    public TableColumn<GeneralPosition, Integer>  positionVolumeColumn;
    public TableColumn<GeneralPosition, Integer>  positionOpenAtColumn;
    public TableColumn<GeneralPosition, Integer>  pnlColumn;

    private ScheduledExecutorService scheduledExecutorService;

    XYChart.Series<String, Number> series = new XYChart.Series<>();


    StockGlassDao stockGlassDao = new StockGlassDaoImpl();

    int userID = 2;

    QuotationDao quotationDao = new QuotationDaoImpl();
    PositionDao positionDao = new PositionDaoImpl();

    LimitOrderDao limitOrderDao = new LimitOrderDaoImpl();

    UserDao userDao = new UserDaoImpl();

    private ObservableList<PriceToVolume> data = FXCollections.observableArrayList();

    UserService userService = new UserService();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setup();

        socketClient = new SocketClient("localhost", 7777);
        socketClient.setController(this);
        socketClient.start();
        service = Executors.newScheduledThreadPool(2);
    }

    public void setup() {
        try {
            setupStockGlass();
            setupChart();
            setupUserPosition();
            setupBalanceLabel();
            setupButtons();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() {
        try {
            setupUserPosition();
            Integer price = Integer.valueOf("" + (quotationDao.get(1).getPrice()));
            Date now = new Date();
            setupStockGlass();
            setupBalanceLabel();
            series.setName((String.valueOf(price)).concat("$"));
            series.getData().remove(series.getData().size()-1);
            series.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), price));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupButtons() {
        EventHandler<ActionEvent> buyEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                try {
                    clickOnBuyButton();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };

        buyButton.setOnAction(buyEvent);

        EventHandler<ActionEvent> sellEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                try {
                    clickOnSellButton();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };

        sellButton.setOnAction(sellEvent);
    }

    private void setupChart() throws SQLException {

        Integer price = Integer.valueOf("" + (quotationDao.get(1).getPrice()));

        chart.setTitle("S&P 500");
        chart.setAnimated(true); // disable animations

        //defining a series to display data

        series.setName((String.valueOf(price)).concat("$"));

        // add series to chart
        chart.getData().add(series);

        // this is used to display time in HH:mm:ss format

        setupEmitation();

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        // put dummy data onto graph per second
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            Integer random = null;
            try {
                random = (quotationDao.get(1).getPrice());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Integer finalRandom = random;
            Platform.runLater(() -> {
                // get current time
                Date now = new Date();
                // put random number with current time
                series.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), finalRandom));

                if (series.getData().size() > WINDOW_SIZE)
                    series.getData().remove(0);
            });
        }, 0, 900, TimeUnit.SECONDS);

        Date now = new Date();
    }

    private void setupStockGlass() throws SQLException {
        StockGlass glass = stockGlassDao.get();

        List<PriceToVolume> priceToVolumes = new ArrayList<>();
        glass.getPriceToVolume().forEach((key, value) -> priceToVolumes.add(
                new PriceToVolume(key, value)
        ));


        ObservableList<PriceToVolume> data =
                FXCollections.observableArrayList(
                        priceToVolumes
                );

        // data.forEach(System.out::println);

        priceColumn.setCellValueFactory(new PropertyValueFactory<PriceToVolume, String>("price"));
        volumeColumn.setCellValueFactory(new PropertyValueFactory<PriceToVolume, String>("volume"));

        stockGlass.setItems(data);

        priceColumn.setCellFactory(column -> {
            return new TableCell<PriceToVolume, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty); //This is mandatory

                    if (item == null || empty) { //If the cell is empty
                        setText(null);
                        setStyle("");
                    } else { //If the cell is not empty

                        setText(item); //Put the String data in the cell

                        //We get here all the info of the Person of this row
                        PriceToVolume priceToVolume = getTableView().getItems().get(getIndex());

                        // Style all persons wich name is "Edgard"
                        if (priceToVolume.getVolume().contains("%"))
                            setStyle("");
                        else {
                            try {
                                if (Integer.parseInt(priceToVolume.getPrice()) > quotationDao.get(1).getPrice())
                                    setStyle("-fx-background-color: #ffd7d1;");
                                else if (Integer.parseInt(priceToVolume.getPrice()) < quotationDao.get(1).getPrice())
                                    setStyle("-fx-background-color: #baffba;");
                                else
                                    setStyle("");
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            };
        });

        volumeColumn.setCellFactory(column -> {
            return new TableCell<PriceToVolume, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty); //This is mandatory

                    if (item == null || empty) { //If the cell is empty
                        setText(null);
                        setStyle("");
                    } else { //If the cell is not empty

                        setText(item); //Put the String data in the cell

                        //We get here all the info of the Person of this row
                        PriceToVolume priceToVolume = getTableView().getItems().get(getIndex());

                        // Style all persons wich name is "Edgard"
                        if (priceToVolume.getVolume().contains("%"))
                            setStyle("");
                        else {
                            try {
                                if (Integer.parseInt(priceToVolume.getPrice()) > quotationDao.get(1).getPrice())
                                    setStyle("-fx-background-color: #ffd7d2;");
                                else if (Integer.parseInt(priceToVolume.getPrice()) < quotationDao.get(1).getPrice())
                                    setStyle("-fx-background-color: #baffba;");
                                else
                                    setStyle("");
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            };
        });
    }

    private void setupUserPosition() throws SQLException {
        GeneralPosition generalPosition = positionDao.getCurrentPosition(userID);
        System.out.println(positionDao.getCurrentPosition(2));

        List<GeneralPosition> position = new ArrayList<>();
        position.add(generalPosition);

        System.out.println(generalPosition);


        ObservableList<GeneralPosition> data =
                FXCollections.observableArrayList(
                        position
                );

        data.forEach(System.out::println);

        sideColumn.setCellValueFactory(new PropertyValueFactory<GeneralPosition, String>("side"));
        positionVolumeColumn.setCellValueFactory(new PropertyValueFactory<GeneralPosition, Integer>("avgVolume"));
        positionOpenAtColumn.setCellValueFactory(new PropertyValueFactory<GeneralPosition, Integer>("avgPrice"));
        pnlColumn.setCellValueFactory(new PropertyValueFactory<GeneralPosition, Integer>("pnl"));

        positionTable.setItems(data);
    }

    private void setupEmitation() {
        Date now = new Date();
        now.setTime(now.getTime() - 2500000);
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        for (int i = 0; i <= 9; i++) {
            now.setTime(now.getTime() + 2500 * i);
            if (i == 0) {
                series.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), 3000));
            }
            if (i == 1) {
                series.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), 3400));
            }
            if (i == 2) {
                series.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), 3120));
            }
            if (i == 3) {
                series.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), 3201));
            }
            if (i == 4) {
                series.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), 3450));
            }
            if (i == 5) {
                series.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), 2900));
            }
            if (i == 6) {
                series.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), 4000));
            }
            if (i == 7) {
                series.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), 3450));
            }
            if (i == 8) {
                series.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), 3780));
            }
            if (i == 9) {
                series.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), 3600));
            }
        }
    }

    private void setupBalanceLabel() throws SQLException {
        User user = userDao.get(userID);
        balanceLabel.setText(("Your balance: " + user.getBalance()).concat("$"));
    }

    private void clickOnBuyButton() throws SQLException {
        User user = userDao.get(userID);
        Message message = new Message();
        message.setType(Type.UPDATE);
        socketClient.sendMessage(message);
        if (limitCheckBox.isSelected()) {
            if (volumeTextField.getText() != null && enterPriceTextField.getText() != null) {
                int volume = Integer.parseInt(volumeTextField.getText());
                int enterPrice = Integer.parseInt(enterPriceTextField.getText());
                if (volume * enterPrice < user.getBalance()) {
                    LimitOrder limitOrder = new LimitOrder(1, "Buy", volume, enterPrice, userID, 1);
                    limitOrderDao.add(limitOrder);
                }
            }
        } else {
            if (volumeTextField.getText() != null) {
                int volume = Integer.parseInt(volumeTextField.getText());
                userService.openPosition(user, "Buy", volume);
            }
        }
    }

    private void clickOnSellButton() throws SQLException {
        User user = userDao.get(userID);
        Message message = new Message();
        message.setType(Type.UPDATE);
        socketClient.sendMessage(message);
        if (limitCheckBox.isSelected()) {
            if (volumeTextField.getText() != null && enterPriceTextField.getText() != null) {
                int volume = Integer.parseInt(volumeTextField.getText());
                int enterPrice = Integer.parseInt(enterPriceTextField.getText());
                if (volume * enterPrice < user.getBalance()) {
                    LimitOrder limitOrder = new LimitOrder(1, "Sell", volume, enterPrice, userID, 1);
                    limitOrderDao.add(limitOrder);
                }
            }
        } else {
            if (volumeTextField.getText() != null) {
                int volume = Integer.parseInt(volumeTextField.getText());
                userService.openPosition(user, "Sell", volume);
            }
        }
    }
}
