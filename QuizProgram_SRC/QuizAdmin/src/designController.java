import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import jfx.messagebox.MessageBox;

/**
 *
 * @author AJITH KP [ http://www.terminalcoders.blogspot.in ]
 */
public class designController implements Initializable 
{
    String host, user, pass, database;
    Integer port = 3306;
    static Connection conn = null;
    static Statement s = null;
    static ResultSet r = null;
    static PreparedStatement p = null;
    @FXML
    TextField hostx, portx, userx, passx, tabx, questionx, answerx, option_1x, option_2x, option_3x, option_4x, imagex, qid, qEd, aEd, o1Ed, o2Ed, o3Ed, o4Ed, iEd, dqid;
    @FXML
    AnchorPane loginPane, addNewQuiz, EditQuiz, deleteQuiz;
    
    @FXML
    public void loginClicked(ActionEvent evt)
    {
        host = hostx.getText();
        user = userx.getText();
        pass = passx.getText();
        port = Integer.parseInt(portx.getText());
        database = tabx.getText();
        if("".equals(host) || "".equals(user) || "".equals(portx.getText()) || "".equals(database))
        {
            MessageBox.show(null,"Please fill Host, Username and Database\n in the form.\n", "Error", MessageBox.ICON_ERROR | MessageBox.OK);
        }       
        else
        {
            try
            {
                Connection con = null;
                PreparedStatement pr = null;
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database, user, pass);
                pr = con.prepareStatement("create table if not exists "+database+".QuizData("
                    + "id int not null auto_increment primary key,"
                    + "question varchar(2048) not null,"
                    + "answer varchar(512) not null,"
                    + "option_1 varchar(512) not null,"
                    + "option_2 varchar(512) not null,"
                    + "option_3 varchar(512) not null,"
                    + "option_4 varchar(512) not null,"
                    + "image varchar(512) not null);");
                pr.executeUpdate();
                addNewQuiz.setVisible(true);
            }
            catch(ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex)
            {
                MessageBox.show(null,ex.getMessage(), "Error", MessageBox.ICON_ERROR | MessageBox.OK);
                addNewQuiz.setVisible(false);
                loginPane.setVisible(true);
                EditQuiz.setVisible(false);
                deleteQuiz.setVisible(false);
            }
        }
    }
    
    @FXML
    public void AddNewEntry(ActionEvent evt)
    {
        showAddNewQuiz();
    }
    
    @FXML
    public void EditEntry(ActionEvent evt)
    {
        editEntryx();
    }
    
    @FXML
    private void deleteEntry(ActionEvent evt)
    {
        deteleEntryx();
    }
    
    @FXML
    public void imagedlg(MouseEvent evt)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        File f = fileChooser.showOpenDialog(null);
        if (f != null) 
        {
            imagex.setText(f.toString());
        }
    }
    
    @FXML
    public void imagedlg2(MouseEvent evt)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        File f = fileChooser.showOpenDialog(null);
        if (f != null) 
        {
            iEd.setText(f.toString());
        }
    }
    
    @FXML
    public void addQuizButton(ActionEvent evt) throws SQLException
    {
        if(!questionx.getText().equals("") || !answerx.getText().equals("") || !option_1x.getText().equals("") || !option_2x.getText().equals("") || !option_3x.getText().equals("") || !option_4x.getText().equals(""))
        {
            addEntry();
            questionx.setText("");
            answerx.setText("");
            option_1x.setText("");
            option_2x.setText("");
            option_3x.setText("");
            option_4x.setText("");
            imagex.setText("");
        }
        else
        {
            MessageBox.show(null,"Please fill all textfields marked as important form.\n", "Error", MessageBox.ICON_ERROR | MessageBox.OK);
        }
    }
    
    @FXML
    public void qidClicked(ActionEvent evt)
    {
        String q, a, o1, o2, o3, o4, i;
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection cn = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database, user, pass);
            Statement stm = cn.createStatement();
            ResultSet rslt = stm.executeQuery("select * from "+database+".QuizData where id = "+qid.getText());
            while(rslt.next())
            {
                q = rslt.getString("question");
                a = rslt.getString("answer");
                o1 = rslt.getString("option_1");
                o2 = rslt.getString("option_2");
                o3 = rslt.getString("option_3");
                o4 = rslt.getString("option_4");
                i = rslt.getString("image");
                qEd.setText(q);
                aEd.setText(a);
                o1Ed.setText(o1);
                o2Ed.setText(o2);
                o3Ed.setText(o3);
                o4Ed.setText(o4);
                if("".equals(i) || i==null)
                {
                    iEd.setText("");
                }
                else
                {
                    iEd.setText(i);
                }
            }
        }
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex)
        {
            MessageBox.show(null, ex.toString(), "Error", MessageBox.ICON_ERROR | MessageBox.OK);
        }
    }
    
    @FXML
    public void updateQ(ActionEvent evt)
    {
        try
        {
            String q = qEd.getText();
            String a = aEd.getText();
            String o1 = o1Ed.getText();
            String o2 = o2Ed.getText();
            String o3 = o3Ed.getText();
            String o4 = o4Ed.getText();
            String i = iEd.getText();
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection cn = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database, user, pass);
            if(qEd.getText().equals("") || aEd.getText().equals("") || o1Ed.getText().equals("") || o2Ed.getText().equals("") || o3Ed.getText().equals("") || o4Ed.getText().equals(""))
            {
                MessageBox.show(null,"Please fill all textfields marked as important form.\n", "Error", MessageBox.ICON_ERROR | MessageBox.OK);
            }
            else
            {
                PreparedStatement pre = cn.prepareStatement("update " + database + ".quizdata set question = ?, answer = ?, option_1 = ?, option_2 = ?, option_3 = ?, option_4 = ?, image = ? where id = " + qid.getText() + ";");    
                pre.setString(1, q);
                pre.setString(2, a);
                pre.setString(3, o1);
                pre.setString(4, o2);
                pre.setString(5, o3);
                pre.setString(6, o4);
                pre.setString(7, i);
                pre.executeUpdate();
                MessageBox.show(null, "Successfully upadated the entries...!!!", "Success", MessageBox.ICON_INFORMATION | MessageBox.OK);
                qEd.setText("");
                aEd.setText("");
                o1Ed.setText("");
                o2Ed.setText("");
                o3Ed.setText("");
                o4Ed.setText("");
                iEd.setText("");
            }
        }
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex)
        {
            MessageBox.show(null, ex.toString(), "Error", MessageBox.ICON_ERROR | MessageBox.OK);
        }
    }
    
    @FXML
    public void deleteAQuiz(ActionEvent evt)
    {
        String delqid = dqid.getText();
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection cn = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database, user, pass);
            PreparedStatement pre = cn.prepareStatement("delete from " + database + ".quizdata where id = " + dqid.getText() + ";");
            pre.executeUpdate();
            MessageBox.show(null, "Successfully deleted the entry...!!!", "Success", MessageBox.ICON_INFORMATION | MessageBox.OK);
        }
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex)
        {
            MessageBox.show(null, ex.toString(), "Error", MessageBox.ICON_ERROR | MessageBox.OK);
        }
    }
    
    @FXML
    public void deleteFullQuiz(ActionEvent evt)
    {
        int sure = MessageBox.show(null, "Are you sure to delete all datas?", "Are You Sure", MessageBox.ICON_QUESTION | MessageBox.OK | MessageBox.CANCEL);
        if(sure==MessageBox.OK)
        {
            try
            {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                Connection cn = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database, user, pass);
                PreparedStatement pre = cn.prepareStatement("drop table "+ database +".quizdata;");
                pre.executeUpdate();
                MessageBox.show(null, "Successfully deleted full entries...!!!", "Success", MessageBox.ICON_INFORMATION | MessageBox.OK);
                
                try
                {
                    Connection con = null;
                    PreparedStatement pr = null;
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    con = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database, user, pass);
                    pr = con.prepareStatement("create table if not exists "+database+".QuizData("
                        + "id int not null auto_increment primary key,"
                        + "question varchar(2048) not null,"
                        + "answer varchar(512) not null,"
                        + "option_1 varchar(512) not null,"
                        + "option_2 varchar(512) not null,"
                        + "option_3 varchar(512) not null,"
                        + "option_4 varchar(512) not null,"
                        + "image varchar(512) not null);");
                        pr.executeUpdate();
                }
                catch(ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex)
                {
                    MessageBox.show(null,ex.getMessage(), "Error", MessageBox.ICON_ERROR | MessageBox.OK);
                }
            }
            catch(ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex)
            {
                MessageBox.show(null, ex.toString(), "Error", MessageBox.ICON_ERROR | MessageBox.OK);
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        addNewQuiz.setVisible(false);
        loginPane.setVisible(true);
        EditQuiz.setVisible(false);
        deleteQuiz.setVisible(false);
    }    

    private void showAddNewQuiz() 
    {
        EditQuiz.setVisible(false);
        loginPane.setVisible(false);
        addNewQuiz.setVisible(true);
        deleteQuiz.setVisible(false);
    }

    private void addEntry() throws SQLException
    {
        String que = questionx.getText();
        String ans = answerx.getText();
        String opt1 = option_1x.getText();
        String opt2 = option_2x.getText();
        String opt3 = option_3x.getText();
        String opt4 = option_4x.getText();
        String img = imagex.getText();
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database, user, pass);
            p = conn.prepareStatement("insert into "+database+".quizdata values(default, ?, ?, ?, ?, ?, ?, ?)");
            p.setString(1, que);
            p.setString(2, ans);
            p.setString(3, opt1);
            p.setString(4, opt2);
            p.setString(5, opt3);
            p.setString(6, opt4);
            p.setString(7, img);
            p.executeUpdate();
        }
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex)
        {
            MessageBox.show(null,ex.getMessage(), "Error", MessageBox.ICON_ERROR | MessageBox.OK);
        } 
    }
    
    private void setResult(ResultSet r) throws SQLException 
    {
        while(r.next()) 
        {
            Integer id = r.getInt("id");
            String q = r.getString("question");
            String a = r.getString("answer");
            String o1 = r.getString("option_1");
            String o2 = r.getString("option_2");
            String o3 = r.getString("option_3");
            String o4 = r.getString("option_4");
        }
    }

    private void editEntryx() 
    {
        loginPane.setVisible(false);
        addNewQuiz.setVisible(false);
        EditQuiz.setVisible(true);
        deleteQuiz.setVisible(false);
    }
    
    private void deteleEntryx()
    {
        loginPane.setVisible(false);
        addNewQuiz.setVisible(false);
        EditQuiz.setVisible(false);
        deleteQuiz.setVisible(true);
    }
}
