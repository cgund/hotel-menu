package menu;


import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/*
Class to represent a restaurant menu.
*/
public class MenuPane extends Pane
{   
    private Label lblItem;
    private Label lblPrice;
    private GridPane gPane;
    
    private final OrderPane order;
    /*
    Constructs MenuPane.  GridPane provides structure for column headers,
    menu items, and prices
    */
    public MenuPane(OrderPane order)
    {
        this.order = order;
        initControls();  
        MenuItem[] menuItems = prepareMenu();
        buildMenu(menuItems);
    }
    
    private void initControls()
    {
        lblItem = new Label("Item");
        lblItem.setFont(Font.font(null, FontWeight.BOLD, 12));
        lblPrice = new Label("Price");
        lblPrice.setFont(Font.font(null, FontWeight.BOLD, 12));   
        
        gPane = new GridPane();
        gPane.setAlignment(Pos.CENTER_LEFT);
        gPane.setVgap(10);
        gPane.setHgap(20);     
        gPane.add(lblItem, 0, 0);
        gPane.add(lblPrice, 1, 0);        
    }
    
    /*
    Creates and returns an array of MenuItems
    */
    private MenuItem[] prepareMenu()
    {
        MenuItem[] items = {new MenuItem("Eggs and Bacon", 6.99), 
                            new MenuItem("Pancakes", 8.99),
                            new MenuItem("Waffles", 9.99)};
        return items;
    }
           
    /*
    Adds menu lblItem and its lblPrice to grid pane.  Registers event handler with
    onMouseClicked property of HyperLink.  The event handler calls the 
    addMenuItem of the MenuOrder object
    */    
    private void buildMenu(MenuItem[] menuItems)
    {
        for (int i = 0; i < menuItems.length; i++)
        {
            MenuItem item = menuItems[i];
            Hyperlink hypName = new Hyperlink(item.getName());
            Double dblPrice = item.getPrice();
            hypName.setOnMouseClicked(e ->
            {
                order.addMenuItem(hypName.getText(), dblPrice); //adds to OrderPane
            });

            gPane.add(hypName, 0, i + 1);
            gPane.add(new Label("$" + dblPrice.toString()), 1, i + 1);
        }

        this.getChildren().add(gPane);        
    }
    
    
}
