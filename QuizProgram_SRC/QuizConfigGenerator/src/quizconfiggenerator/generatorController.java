package quizconfiggenerator;

import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import jfx.messagebox.MessageBox;

/**
 *
 * @author AJITH KP [ http://www.terminalcoders.blogspot.in ] [ http://vxcrack.blogspot.in ]
 */
public class generatorController implements Initializable {
    String host, port, username, password, database;
    @FXML
    private TextField hx, px, ux, passx, dx;
    
    @FXML
    private void handleButtonAction(ActionEvent event) 
    {
        host = hx.getText();
        port = px.getText();
        username = ux.getText();
        password = passx.getText();
        database = dx.getText();
        if("".equals(host) || "".equals(port) || "".equals("username") || "".equals(database))
        {
            MessageBox.show(null,"Please fill all textfields marked as important form.\n", "Error", MessageBox.ICON_ERROR | MessageBox.OK);
        }
        else
        {
            try
            {
                PrintWriter w = new PrintWriter("quiz.conf", "UTF-8");
                w.println("<username>"+username+"</username>");
                w.println("<password>"+password+"</password>");
                w.println("<host>"+host+"</host>");
                w.println("<database>"+database+"</database>");
                w.println("<port>"+port+"</port>");
                w.close();
                MessageBox.show(null,"Successfully generated 'quiz.conf'.\nCopy this file to your quiz client folder.", "Success", MessageBox.ICON_INFORMATION | MessageBox.OK);
            }
            catch(Exception ex)
            {
                MessageBox.show(null,ex.getMessage(), "Error", MessageBox.ICON_ERROR | MessageBox.OK);
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
