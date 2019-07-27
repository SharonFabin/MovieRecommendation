import controllers.RatingController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/ratingScene.fxml"));
        Parent root = loader.load();
        RatingController controller = loader.getController();
        controller.setStage(primaryStage);
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root,1200,800);
        scene.getStylesheets().add("view/main.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
