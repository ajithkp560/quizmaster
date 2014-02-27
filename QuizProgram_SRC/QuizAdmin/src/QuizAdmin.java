/*
	AJITH KP [ http://www.terminalcoders.blogspot.in ] [ http://vxcrack.blogspot.in ]
*/

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
public class QuizAdmin extends Application 
{    
    @Override
    public void start(Stage stage) throws Exception 
    {
        Parent root = FXMLLoader.load(getClass().getResource("design.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Quiz Program Admin");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("apple.png")));
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
