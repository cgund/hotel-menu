
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/*
Class to display MenuPane and OrderPane objects
*/
public class MenuApp extends Application 
{
    @Override
    public void start(Stage primaryStage) 
    {
        Label lblTitle = new Label("Hotel Breakfast Menu");
        lblTitle.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 16));
        
        HBox hbTitle = new HBox(10);
        hbTitle.setAlignment(Pos.CENTER);
        hbTitle.setPadding(new Insets(10, 10, 10, 10));
        hbTitle.getChildren().add(lblTitle);
        
        OrderPane orderPane = new OrderPane(); //OrderPane
        orderPane.setPadding(new Insets(10, 10, 10, 10));
        
        MenuPane menuPane = new MenuPane(orderPane); //MenuPane
        menuPane.setPadding(new Insets(10, 10, 10, 10));
        
        HBox hbMenu = new HBox(10);
        hbMenu.setAlignment(Pos.CENTER_LEFT);
        hbMenu.setPadding(new Insets(10, 10, 10, 40));
        hbMenu.getChildren().add(menuPane);
        
        BorderPane root = new BorderPane();        
        root.setTop(hbTitle);
        root.setCenter(hbMenu);
        root.setRight(orderPane);
        
        Scene scene = new Scene(root, 600, 600);
        
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hotel Breakfast Menu");
        primaryStage.show();
    }

    public static void main(String[] args) 
    {
        launch(args);
    }
}
