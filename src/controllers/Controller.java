package controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.CsvParser;
import model.MovieScrapper;
import model.Scrapper.JSoupCrawler;
import model.User;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Controller implements Initializable {

    @FXML HBox hb;
    @FXML ProgressBar bar;

    private CsvParser csvParser;
    private Stage stage;
    private User user;
    private List<VBox> articles;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        articles = new ArrayList<>();
        hb.setSpacing(50);
    }

    public void setStage(Stage stage){this.stage=stage;}

    public void setUser(User user){this.user=user;}

    public void buildScene() throws Exception{
        List<Integer> actual = user.getMovies();
        csvParser = new CsvParser("resources/movies.csv");
        Map<Integer,String> map = csvParser.mapMovies(0,1);
        List<String> bestTitles = actual.stream().map(map::get).collect(Collectors.toList());
        String[] titles = bestTitles.stream().toArray(String[]::new);
        for(String title:titles){
            VBox box = new VBox();
            Label text = new Label(title);
            text.setMinWidth(100);
            box.getChildren().add(text);
            hb.getChildren().add(box);
            articles.add(box);
        }
        String picsUrlSource = "http://www.imdb.com/find?ref_=nv_sr_fn&q=";
        String defaultImgUrl = "jack.jpg";
        MovieScrapper scrapper = new MovieScrapper(picsUrlSource,titles,defaultImgUrl);
        JSoupCrawler crawler = new JSoupCrawler(scrapper,true);

        Task builder = new Task() {
            @Override
            protected Object call() throws Exception {
                stage.getScene().setCursor(Cursor.WAIT);
                crawler.crawl();
                for(int i=0;i<bestTitles.size();i++){
                    Platform.runLater(() -> addImage(scrapper,bestTitles));
                }
                return null;
            }
        };
        builder.setOnSucceeded(s->stage.getScene().setCursor(Cursor.DEFAULT));
        bar.progressProperty().bind(crawler.progressProperty);
        new Thread(builder).start();
    }

    private void addImage(MovieScrapper scrapper, List<String> bestTitles) {
        VBox box = articles.remove(0);
        String movieTitle = bestTitles.remove(0);
        box.getChildren().removeAll();
        box.getChildren().addAll(new ImageView(new Image(
                scrapper.getImageUrl(movieTitle)
                ,150
                ,300
                ,false
                ,true)));
    }

}
