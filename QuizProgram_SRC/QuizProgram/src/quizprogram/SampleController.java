package quizprogram;

/*
 * Coded By AJITH KP [ ajithkp560@gmail.com ]
 * AMSTECK ARTS & SCIENCE COLLEGE, KALLIASSERY
 * BCA 2012 - 2015 Batch
 */



import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import static java.lang.Math.random;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jfx.messagebox.MessageBox;
public class SampleController implements Initializable, Runnable 
{     
    static final Integer START = (30*60)+2;
    Integer timesec = START;
    Integer[] val = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    static Integer qno = 0;
    static Integer qnum = 0;
    static Integer sqno = 0;
    static float score = 0;
    static Connection c = null, cc = null;
    static Statement s = null, ss = null;
    static ResultSet r = null, rr = null;
    public String ans;
    String username, password, host, port, database;
    static String[] questions;
    
    static String[] answers;
    
    static String[][] options;
    
    static Integer[] imgx;
    static String[] images;
    final ToggleGroup group = new ToggleGroup();
    Timeline timeline = null;
    Stage stage;
    KeyCodeCombination kb, finished, scoreS;
    @FXML
    AnchorPane optionPane, finishx;
    @FXML
    Rectangle unlocktop, unlockbottom;
    @FXML
    private Node root;
    @FXML
    Label timex, question, bcarock, qsno, scoreB;
    @FXML
    Button button, button2, lock, subm;
    @FXML
    RadioButton a1, a2, a3, a4;
    
    @FXML
    private void nextQ(ActionEvent event) 
    {   
        next();
    }
    
    @FXML
    private void prevQ(ActionEvent event)
    {
        prev();
    }
    
    @FXML
    private void submitted(ActionEvent event)
    {
        if(group.getSelectedToggle()!=null)
        {
            val[qno] = 1;  
            if(ans.equals(answers[qno]))
            {
                score+=1;
            }
            else
            {
                score-=0.25;
            }
            check();
            //setScore();
            next();
        }
    }
    
    @FXML
    private void unlockPressed(ActionEvent event)
    {
        //Thanks to Oracle Tutorial for this animation;
        
        lock.setDisable(true);
        root.requestFocus();
        final FadeTransition fadeLockButton = fadeOut(Duration.valueOf("1s"), lock);
        final FadeTransition fadeBca = fadeOut(Duration.valueOf("1s"), bcarock);
        final heightTrans openLockTop = new heightTrans(Duration.valueOf("2s"), unlocktop);
        openLockTop.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent args) {
                unlocktop.setVisible(false);
                unlocktop.setHeight(openLockTop.height);
            }
        });
        final heightTrans openLockBottom = new heightTrans(Duration.valueOf("2s"), unlockbottom);
        openLockBottom.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent args) {
                unlockbottom.setVisible(false);
                unlockbottom.setHeight(openLockBottom.height);
            }
        });
        final ParallelTransition openLock = new ParallelTransition(openLockTop, openLockBottom);
        final SequentialTransition unlock = new SequentialTransition(fadeLockButton, openLock); 
        final SequentialTransition bca = new SequentialTransition(fadeBca, openLock); 
        unlock.play();
        bca.play();
               
        if(timeline != null)
        {
            timeline.stop();
        }
        timesec = START;
        timex.setText("Time Left: "+(timesec/60)+":"+(timesec%60));
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1),
                new EventHandler()
                {
                    @Override
                    public void handle(Event t) 
                    {
                        timesec--;
                        timex.setText("Time Left: "+(timesec/60)+":"+(timesec%60));
                        if(timesec<=0)
                        {
                            timeline.stop();
                            question.setText("Time Out!!!\nYour Score is "+score);
                            root.setDisable(true);
                            finishQ();
                        }
                    }                    
                }));
        timeline.playFromStart();                
        question.setText(questions[qno]);
        qsno.setText("Question Number: 1");
        //scoreB.setText("Score: "+score);
        
        if(imgx[qno]==1 && val[qno]==0)
        {
            showImage(qno);
        }
        
        setOptionsx();
    }
    
    @FXML
    private void mouseOver(MouseEvent event)
    {
        button.setStyle("-fx-background-color:#3c7fb1, linear-gradient(#fafdfe, #e8f5fc), linear-gradient(#eaf6fd 0%, #d9f0fc 49%, #bee6fd 50%, #a7d9f5 100%);\n" +
"	-fx-background-insets:0, 1, 2;\n" +
"	-fx-background-radius: 30;\n" +
"	-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.6), 3, 0.0, 0, 1);\n" +
"	-fx-padding: 3 30 3 30;\n" +
"	-fx-text-fill: #242d35;\n" +
"	-fx-font-size: 14px;");
    }
    
    @FXML
    private void mouseOver2(MouseEvent event)
    {
        button2.setStyle("-fx-background-color:#3c7fb1, linear-gradient(#fafdfe, #e8f5fc), linear-gradient(#eaf6fd 0%, #d9f0fc 49%, #bee6fd 50%, #a7d9f5 100%);\n" +
"	-fx-background-insets:0, 1, 2;\n" +
"	-fx-background-radius: 30;\n" +
"	-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.6), 3, 0.0, 0, 1);\n" +
"	-fx-padding: 3 30 3 30;\n" +
"	-fx-text-fill: #242d35;\n" +
"	-fx-font-size: 14px;");
    }
    
    @FXML
    private void mouseOver3(MouseEvent event)
    {
        subm.setStyle("-fx-background-color:rgba(0,0,0,0,0.08), linear-gradient(#5a61af, #51536d), linear-gradient(#e4fbff 0%, #cee6fb 10%, #a5d3fb 50%, #88c6fb 51%, #d5faff 100%);\n" +
"	-fx-background-insets:0, 1, 2;\n" +
"	-fx-background-radius: 70;\n" +
"	-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.6), 3, 0.0, 0, 1);\n" );
    }
    
    @FXML
    private void mouseExit1(MouseEvent event)
    {
        button.setStyle("-fx-background-color:rgba(0,0,0,0,0.08), linear-gradient(#5a61af, #51536d), linear-gradient(#e4fbff 0%, #cee6fb 10%, #a5d3fb 50%, #88c6fb 51%, #d5faff 100%);\n" +
"	-fx-background-insets: 0 0 -1 0, 0, 1;\n" +
"	-fx-background-radius: 30;\n" +
"	-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.6), 3, 0.0, 0, 1);\n" +
"	-fx-padding: 3 30 3 30;\n" +
"	-fx-text-fill: #242d35;\n" +
"	-fx-font-size: 14px;");
    }
    
    @FXML
    private void mouseExit2(MouseEvent event)
    {
        button2.setStyle("-fx-background-color:rgba(0,0,0,0,0.08), linear-gradient(#5a61af, #51536d), linear-gradient(#e4fbff 0%, #cee6fb 10%, #a5d3fb 50%, #88c6fb 51%, #d5faff 100%);\n" +
"	-fx-background-insets: 0 0 -1 0, 0, 1;\n" +
"	-fx-background-radius: 30;\n" +
"	-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.6), 3, 0.0, 0, 1);\n" +
"	-fx-padding: 3 30 3 30;\n" +
"	-fx-text-fill: #242d35;\n" +
"	-fx-font-size: 14px;");
    }
    
    @FXML
    private void mouseExit3(MouseEvent event)
    {
        subm.setStyle("-fx-background-color:rgba(0,0,0,0,0.08), linear-gradient(#5a61af, #51536d), linear-gradient(#e4fbff 0%, #cee6fb 10%, #a5d3fb 50%, #88c6fb 51%, #d5faff 100%);\n" +
"       -fx-background-radius: 70;\n"+
"	-fx-background-insets: 0 0 -1 0, 0, 1;\n" +
"	-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.6), 3, 0.0, 0, 1);\n");
    }
    
    @FXML
    private void KeyPressed(KeyEvent event)
    {
        if(kb.match(event))
        {
            if(imgx[qno] == 1 && val[qno] == 0)
            {
                showImage(qno);
            }
        }
        if(finished.match(event))
        {
            finishQ();
        }
        if(scoreS.match(event))
        {
            setScore();
        }
    }
    
    @FXML
    private void KeyRel(KeyEvent event)
    {
        if(scoreS.match(event))
        {
            scoreB.setText("");
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {    
        try
        {
            String read = "", line;
            Pattern uP, pP, hP, dP, pp; 
            Matcher uM, pM, hM, dM, pm;
            FileInputStream fstream = new FileInputStream("quiz.conf");
            DataInputStream d = new DataInputStream(fstream);
            BufferedReader buf = new BufferedReader(new InputStreamReader(d));
            while((line = buf.readLine()) != null)
            {
                read+=line;
                read+="\n";
            }
            d.close();
            buf.close();
            read = read.replaceAll("\\s+", " ");
            uP = Pattern.compile("<username>(.*?)</username>");
            pP = Pattern.compile("<password>(.*?)</password>");
            hP = Pattern.compile("<host>(.*?)</host>");
            dP = Pattern.compile("<database>(.*?)</database>");
            pp = Pattern.compile("<port>(.*?)</port>");
            uM = uP.matcher(read);
            pM = pP.matcher(read);
            hM = hP.matcher(read);
            dM = dP.matcher(read);
            pm = pp.matcher(read);
            //username, password, host, port, database
            while(uM.find()==true)
            {
                username = uM.group(1);
                break;
            }
            while(pM.find()==true)
            {
                password = pM.group(1);
                break;
            }
            while(hM.find()==true)
            {
                host = hM.group(1);
                break;
            }
            while(dM.find()==true)
            {
                database = dM.group(1);
                break;
            }
            while(pm.find()==true)
            {
                port = pm.group(1);
                break;
            }
        }
        catch(Exception ex)
        {
            MessageBox.show(null,ex.getMessage(), "Error", MessageBox.ICON_ERROR | MessageBox.OK);
        }
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            cc = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database, username, password);
            ss = cc.createStatement();
            rr = ss.executeQuery("select * from "+database+".QUIZDATA");
            int size = countRow(rr);
            answers = new String[size];
            questions = new String[size];
            options = new String[size][4];
            imgx = new Integer[size];
            images = new String[size];
            //imgx = new Integer[size];
        }
        catch(Exception ex)
        {
            MessageBox.show(null,ex.getMessage(), "Error", MessageBox.ICON_ERROR | MessageBox.OK);
        } 
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+database, "root", "");
            s = c.createStatement();
            r = s.executeQuery("select * from "+database+".QUIZDATA");
            setResult(r);
        }
        catch(Exception ex)
        {
            MessageBox.show(null,ex.getMessage(), "Error", MessageBox.ICON_ERROR | MessageBox.OK);
        }  
        question.setWrapText(true);
        finishx.setVisible(false);
        kb = new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN);
        finished = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);
        scoreS = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        Random rand = new Random();
        qno = rand.nextInt(questions.length);
        a1.setToggleGroup(group);
        a2.setToggleGroup(group);
        a3.setToggleGroup(group);
        a4.setToggleGroup(group);
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {
           @Override
           public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle)
           {
               if(group.getSelectedToggle()!=null)
               {
                   ans = ((RadioButton) new_toggle).getText();
               }
           }
        });
    }    
    
    private void check()
    {
        if(val[qno]==1)
        {
            optionPane.setDisable(true);
        }
        else
        {
            optionPane.setDisable(false);            
            a1.setSelected(false);
            a2.setSelected(false);
            a3.setSelected(false);
            a4.setSelected(false);
        }
    }
    
    private void setOptionsx()
    {       
        a1.setText(options[qno][0]);
        a2.setText(options[qno][1]);
        a3.setText(options[qno][2]);
        a4.setText(options[qno][3]);
    }
    
    private void showImage(Integer qn)
    {   
        try
        {
            Image img = new Image("file:"+images[qno]);
            ImageView imv = new ImageView(img);
            stage  = new Stage();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("apple.png")));
            stage.initModality(Modality.APPLICATION_MODAL);
            StackPane pane2 = new StackPane();
            pane2.getChildren().add(imv);
            double wid = imv.getImage().getWidth();
            double hei = imv.getImage().getHeight();
            Scene scene = new Scene(pane2, wid, hei);
            stage.setScene(scene);
            stage.setTitle("Identify...");
            stage.show();
        }
        catch(Exception ex)
        {
            MessageBox.show(null,ex.getMessage(), "Error", MessageBox.ICON_ERROR | MessageBox.OK);
        }
    }

    private void next() 
    {    
        try
        {
            stage.close();
        }
        catch(Exception e)
        {
            
        }
        
        qno++;
        qnum++;
        qno = (qno%questions.length);
        qnum = (qnum%questions.length);
        
        if(imgx[qno]==1 && val[qno]==0)
        {
            showImage(qno);
        }
        
        check();
        question.setText(questions[qno]);
        qsno.setText("Question Number: "+(qnum+1));
        setOptionsx();
    }

    private void prev() 
    {
        try
        {
            stage.close();
        }
        catch(Exception e)
        {
            
        }
        
        if(qno==0)
        {
            qno = questions.length;
        }
        if(qnum==0)
        {
            qnum = questions.length;
        }
        qno--;
        qnum--;
        qno = (qno%questions.length);
        qnum = (qnum%questions.length);
        setOptionsx();
        check();
        question.setText(questions[qno]);
        qsno.setText("Question Number: "+(qnum+1));
        
        
        if(imgx[qno]==1 && val[qno]==0)
        {
            showImage(qno);
        }
    }
    
    private void setScore()
    {
        scoreB.setText("Score: "+score);
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
            String img = r.getString("image");
            questions[sqno] = q;
            answers[sqno] = a;
            options[sqno][0] = o1;
            options[sqno][1] = o2;
            options[sqno][2] = o3;
            options[sqno][3] = o4;
            if(img == null || img.equals(""))
            {
                imgx[sqno] = 0;
            }
            else
            {
                imgx[sqno] = 1;
            }
            images[sqno] = img;
            sqno++;
        }
    }
    
    private Integer countRow(ResultSet r) throws SQLException
    {
        Integer cnt = 0;
        while(r.next())
        {
            cnt++;
        }
        return cnt;
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
        
    private final static class heightTrans extends Transition 
    {
        final Rectangle node;
        final double height;

        public heightTrans(Duration duration, Rectangle node) 
        {
            this(duration, node, node.getHeight());
        }

        public heightTrans(Duration duration, Rectangle node, double height) 
        {
            this.node = node;
            this.height = height;
            this.setCycleDuration(duration);
        }

        public Duration getDuration() 
        {
            return getCycleDuration();
        }

        @Override
        protected void interpolate(double frac) 
        {
            node.setHeight((1.0 - frac) * height);
        }
    }
    
    private void finishQ()
    {
        //Thanks to Oracle Tutorial for this animation;
        Text t = new Text("Your Score: "+score);
        t.setFont(Font.font("Algerian", 50));
        t.setFill(Color.YELLOWGREEN);
        t.setCache(true);
        t.setTranslateY(320);//296
        t.setTranslateX(210);//210
        finishx.setVisible(true);
        Group circ = new Group();
        
        for(int i=0;i<25;i++)
        {
            Circle c = new Circle(150, Color.web("white", 0.05));
            c.setStrokeType(StrokeType.OUTSIDE);
            c.setStroke(Color.web("white", 0.16));
            c.setStrokeWidth(4);
            c.setEffect(new BoxBlur(10,10,3));
            circ.getChildren().add(c);
        }
        
        Rectangle col = new Rectangle(finishx.getWidth(), finishx.getHeight(), new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new Stop[]{
                                        new Stop(0,    Color.web("#f8bd55")),
                                        new Stop(0.14, Color.web("#c0fe56")),
                                        new Stop(0.28, Color.web("#5dfbc1")),
                                        new Stop(0.43, Color.web("#64c2f8")),
                                        new Stop(0.57, Color.web("#be4af7")),
                                        new Stop(0.71, Color.web("#ed5fc2")),
                                        new Stop(0.85, Color.web("#ef504c")),
                                        new Stop(1,    Color.web("#f2660f")),}));
        col.widthProperty().bind(finishx.widthProperty());
        col.heightProperty().bind(finishx.heightProperty());
        Group grp = new Group(new Group(new Rectangle(finishx.getWidth(), finishx.getHeight(), Color.BLACK), circ), col);
        col.setBlendMode(BlendMode.OVERLAY);
        finishx.getChildren().add(grp);
        Timeline tm = new Timeline();
        for(Node cir: circ.getChildren())
        {
            tm.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(cir.translateXProperty(), random()*800), new KeyValue(cir.translateYProperty(), random()*600)), new KeyFrame(new Duration(20000), new KeyValue(cir.translateXProperty(), random()*800), new KeyValue(cir.translateYProperty(), random()*600)));
        }
        tm.setCycleCount(Timeline.INDEFINITE);
        tm.setAutoReverse(true);
        tm.play();        
        finishx.getChildren().add(t);
    }
    
    private FadeTransition fadeOut(final Duration duration, final Node node) 
    {
        final FadeTransition fadeOut = new FadeTransition(duration, node);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(ActionEvent args) 
            {
                node.setVisible(false);
            }
        });
        return fadeOut;
    }  
}
