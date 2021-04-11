package launcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.net.URL;


public class Main extends Application {
    private Stage primaryStage;
    private Pane view;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        try{
            URL fileUrl = getClass().getResource("/view/home.fxml");
            if(fileUrl ==null){
                throw new FileNotFoundException("FXML file not found.");
            }
            FXMLLoader loader = new FXMLLoader(fileUrl);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setTitle("ToughBank ATM");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }



}
