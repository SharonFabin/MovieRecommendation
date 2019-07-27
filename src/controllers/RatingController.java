package controllers;/*
 * Created by Sharon on 27/04/2018
 */

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.CsvParser;
import model.Model;
import model.User;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class RatingController implements Initializable {

    //@FXML HBox picsBox;
    //@FXML HBox inputsBox;
    @FXML Spinner<Integer> movieSelector;
    @FXML ScrollPane scroll;
    @FXML HBox box;
    @FXML Label status;
    @FXML Button calibrateButton;
    @FXML ProgressBar bar;

    private Stage stage;
    private List<TextField> inputs;
    private List<Integer> movies;
    private Model model;
    private CsvParser parser;

    public void initialize(URL location, ResourceBundle resources) {
        parser = new CsvParser("resources/movies.csv");
        model = new Model();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 50, 10);
        movieSelector.setValueFactory(valueFactory);
    }

    public void setStage(Stage stage){
        this.stage=stage;
    }

    private void fillMovies() throws Exception{
        inputs = new ArrayList<>();
        parser.setFilePath("resources/movies.csv");
        Map<Integer,String> movieNames = parser.mapMovies(0,1);
        IntStream.range(0,movieSelector.getValue()).forEach(i->createArticle(movieNames, i));
    }

    private void createArticle(Map<Integer, String> movieNames, int i) {
        VBox vBox = new VBox();
        TextField input = new TextField("0");
        input.setAlignment(Pos.CENTER);
        inputs.add(input);
        Label title = new Label(movieNames.get(movies.get(i)));
        vBox.getChildren().addAll(new ImageView(
                new Image("jack.jpg",150,300,false,true))
                ,title
                ,input);
        box.getChildren().add(vBox);
    }

    public void startRating(){
        this.stage.getScene().setCursor(Cursor.WAIT);
        calibrateButton.setDisable(true);

        Task task = new Task<Void>() {
            @Override public Void call() throws Exception {
                parser.setFilePath("resources/ratings.csv");
                movies=parser.getFrequentMovies(movieSelector.getValue());
                updateProgress(2,2);
                return null;
            }
        };
        task.setOnSucceeded(s -> {
            try {
                fillMovies();
            } catch (Exception e) {
                e.printStackTrace();
            }
            scroll.setVisible(true);
            stage.getScene().setCursor(Cursor.DEFAULT);
        });
        bar.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }

    public void findMatch() throws Exception{
        User user = new User(0);
        inputs.stream()
                .map(input -> Double.parseDouble(input.getText()))
                .forEach(rating -> user.putMovieRating(movies.remove(0),rating));
        model.updateRatings(user.getMovieRatings());
        User match = model.readUserToUser("resources/ratings.csv");
        showMovieScene(match);

    }

    private void showMovieScene(User user) throws Exception{
        stage.getScene().setCursor(Cursor.WAIT);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/movieScene.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        Controller controller = loader.getController();
        controller.setStage(stage);
        controller.setUser(user);
        controller.buildScene();
    }


}
