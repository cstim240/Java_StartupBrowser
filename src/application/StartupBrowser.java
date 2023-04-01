package application;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
//--------------------------------------	
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

/** <h1> Assigment 5 - JavaFX Browser - GUI and Event Handling </h1>
 * <p>
 * Description: A functional browser with a navigation bar and a live status bar
 * </p>
 * <h2> CPSC 1181 - 002 </h2>
 * @author Tim Supan
 * @see <a href="https://docs.oracle.com/en/java/"> Java Documentation </a> 
 * @version 18.0.1.1
 * @since 2023-03-31
 */
//--------------------------------------

public class StartupBrowser extends Application {
	private BorderPane root;
	private WebView webView = new WebView(); //webView is a node that manages WebEngine and displays its content
	private WebEngine webEngine;
	private HBox addressBar;
	private HBox statusBar;
	private Text domain;
	private final String homePage = "https://langara.ca";
	
	// this method sets up the Navigation Bar by containing all its children nodes into an HBox obj
    private void setupAddressBar() {
    	 addressBar = new HBox();
    	 addressBar.setStyle("-fx-background-color: #000000;");
    	 addressBar.setAlignment(Pos.BOTTOM_RIGHT);
    	 addressBar.setSpacing(10); //gives space between children nodes
    	 
         Button home = new Button("",new ImageView("home.png"));   
         
         Button back = new Button("", new ImageView("back.png"));
         
         Button forward = new Button("", new ImageView("forward.png"));
                  
         Button refresh = new Button("", new ImageView("refresh.png"));
         
         TextField url = new TextField();
         url.setPrefWidth(700);
         
         home.setOnMouseClicked(event -> { //event handler + lambda for home button
        	 webEngine.load(homePage); //.load is used to load the webpage into webview
        	 url.setText(homePage); //updates what is inside the url textfield
         });
         
         back.setOnMouseClicked(event -> goBack());
         
         forward.setOnMouseClicked(event -> goForward());
         
         refresh.setOnMouseClicked(event -> webEngine.reload());
         
         url.setOnAction(event -> loadURL(url.getText()));
         
         addressBar.getChildren().addAll(home,back,forward,refresh,url);
         addressBar.setPadding(new Insets(15, 15, 15, 10)); //adds padding at bottom of children, layout: top, right, left, bottom
 
    }
    //-------------------------------------- 
    // This method is called by the back button's event handler under the setupAddressBar method
    private void goBack() {
    	if (webEngine.getHistory().getCurrentIndex() > 0) {
    		webEngine.getHistory().go(-1);
    	}
    }
    //--------------------------------------
    // this method is called by the forward button's event handler under the setupAddressBar method
    private void goForward() {
    	//the getEntries() returns an observable list with every single entry of getHistory, an entry instance is associated with the visited page.
    	int size = webEngine.getHistory().getEntries().size();
    	//we subtract one from size as it starts from 0
    	if (webEngine.getHistory().getCurrentIndex() < size - 1) {
    		webEngine.getHistory().go(1);
    	}
    }
    //--------------------------------------
    // this method is called by the url textfield's event handler
    private void loadURL(String url) {
    	if (url.startsWith("http://")) {
    		webEngine.load(url);
    	} else {
    		webEngine.load("http://" + url);
    	} 
    }
    //--------------------------------------
    
    
    // this sets up the live browser status bar at the bottom of the browser by containing all its child nodes in an HBox
    private void setupStatusBar() {
    	statusBar = new HBox();
    	statusBar.setAlignment(Pos.CENTER);
    	
        Text domain = new Text(getDomain(webEngine.getLocation())); //original: domain = new Text("langara.ca");
        domain.setFill(Color.YELLOW);
        
        Timeline domainline = new Timeline(new KeyFrame(
        		Duration.seconds(1), event -> {
        			domain.setText(getDomain(webEngine.getLocation()));
        		})
        		);
        domainline.setCycleCount(Animation.INDEFINITE);
        domainline.play();
        
        Text separator1 = new Text(" | ");
        separator1.setFill(Color.YELLOW);
        
        Text copyrightSymbol = new Text("© ");
        copyrightSymbol.setFill(Color.YELLOW);
        Text yearAndName = new Text("2023 -- Tim Supan");
        yearAndName.setFill(Color.YELLOW);
        //Text copyright = new Text("JavaFX -- All Rights Reserved.");
        
        Text separator2 = new Text(" | ");
        separator2.setFill(Color.YELLOW);
        
        Text date = new Text("date");
        date.setFill(Color.YELLOW);
        
        Timeline dateline = new Timeline(
        		new KeyFrame(Duration.seconds(1), 
        				event -> date.setText(getDate())
        				)
        		);
        dateline.setCycleCount(Animation.INDEFINITE);
        dateline.play();
        
        Text separator3 = new Text(" | ");
        separator3.setFill(Color.YELLOW);
        
        Text time = new Text(getTime());
        time.setFill(Color.YELLOW);
        
        Timeline timeline = new Timeline( //a timeline obj is used to schedule a series of KeyFrames
        		new KeyFrame(Duration.seconds(1), //a keyFrame represents a point in time at which an action is performed, in this case a keyframe is schedule to act every 1 sec
        				event -> time.setText(getTime()) //the action performed every sec is specified by Duration.seconds(1)
        				)
        		);
        timeline.setCycleCount(Animation.INDEFINITE); //makes it so that the number of cycles of a timeline repeats indefinitely
        timeline.play(); //method used to begin playing the animation
        
        
        statusBar.getChildren().addAll(domain,separator1,copyrightSymbol,yearAndName,separator2,date,separator3,time);
        statusBar.setStyle("-fx-background-color: #000000;");
    }
    //--------------------------------------
    /** this method is called by domain's constructor, under the setupStatusBar method
     * @param url in String type
     * @return String to represent the domain
     */
    private String getDomain(String url) {
    	try {
			URI uri = new URI(url); //turns the url string into a uri object, example: "reddit.com" becomes a uri object
			String domain = uri.getHost();
	    	return domain.startsWith("www.") ? domain.substring(4) : domain;
		} catch (URISyntaxException e) {
			return "";
		}
    	
    }
    //--------------------------------------
    /** this method is called by the time's constructor, under the setupStatusBar method
     * @return String which represents the Time is a specific hour:minute:second format
     */
    private String getTime() {
    	Date today = new Date();
    	SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    	String processedTime = dateFormat.format(today);
    	return processedTime;
    }
    //--------------------------------------
    /** this method is called by the date's constructor, under the the setupStatusBar method
     * @return String which represents the date in year-month-day format
     */
    private String getDate() {
    	Date today = new Date();
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	String processedDate = dateFormat.format(today);
    	return processedDate;
    }
    //--------------------------------------
    
    private void setupWebView() {
        webView= new WebView();
        webEngine = webView.getEngine();
        webEngine.load(homePage);
    }
    
    //entry point for our application
    public void start(Stage stage) {
        root = new BorderPane();
        //root.setPadding(new Insets(10)); //added for padding purposes around browser
    //--------------------------------------
        this.setupAddressBar();
        this.setupWebView();
        this.setupStatusBar();
    //--------------------------------------
        root.setTop(addressBar);
        root.setBottom(statusBar);
        root.setCenter(webView);
    //--------------------------------------

        Scene scene = new Scene(root);
        stage.setScene(scene);
        //stage.setFullScreen(true);
        stage.setWidth(1200);
        stage.setHeight(1000);
        stage.setResizable(false);
        stage.setTitle("JavaFX Web Browser");
        stage.show();
    }
	//--------------------------------------
	 public static void main(String[] args) {
        Application.launch(args);
    }
    //--------------------------------------
}
