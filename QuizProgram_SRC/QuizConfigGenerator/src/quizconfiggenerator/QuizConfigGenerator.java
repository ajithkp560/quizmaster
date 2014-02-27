package quizconfiggenerator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author AJITH KP [ http://www.terminalcoders.blogspot.in ] [ http://vxcrack.blogspot.in ]
 */
public class QuizConfigGenerator extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("generator.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Quiz Config Generator");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("apple.png")));
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
