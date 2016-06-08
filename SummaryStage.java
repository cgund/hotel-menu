package menu;


import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/*
Displays to user summary of order and allows user the option to print an order 
summary
*/
class SummaryStage
{
    private final Stage stage = new Stage();
    private final BorderPane root = new BorderPane();
    private final GridPane gpOrders = new GridPane();
    private final Scene scene;
    private final VBox vbOrder = new VBox();
    private final HBox hbButtons = new HBox(10);;
    
    /*
    Constructs a SummaryStage
    @param orderGroup A collection of OrderGroup objects
    @param subTotal The order subtotal
    @param map Customer identifiers mapped to customer information
    */
    protected SummaryStage(List<OrderPane.OrderGroup> orderGroups, 
            double subTotal, LinkedList<Pair<String, String>> pairs)
    {
        gpOrders.setHgap(15);
        gpOrders.setVgap(10);
        fillGridPane(orderGroups);
        scene = new Scene(root, 500, gpOrders.getHeight() * 1.5);
        
        root.setTop(createTitlePane());
        root.setCenter(createCombinedPane(subTotal, pairs));
        root.setBottom(createButtonPane(orderGroups, subTotal, pairs));
        
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Order"); 
    }
    
    private Node createTitlePane()
    {
        HBox hbTitle = new HBox(10);
        hbTitle.setAlignment(Pos.CENTER);
        hbTitle.setPadding(new Insets(10, 0, 10, 0));
        hbTitle.getChildren().add(new Label("Order Confirmation"));
        return hbTitle;
    }
    
    private Node createButtonPane(List<OrderPane.OrderGroup> orderGroups, 
                double subTotal, LinkedList<Pair<String, String>> pairs)
    {
        Button btnOk = new Button("OK");
        btnOk.setOnMouseClicked(e ->
        {
            stage.close();
        });
        
        Button btnPrint = new Button("Print");
        /*
        Processes print button events
        */
        btnPrint.setOnMouseClicked(e ->
        {
            //Creates a new class for printing order summary. Almost identical 
            //to order summary class
            SummaryStagePrint printStage = new SummaryStagePrint(orderGroups, 
                    subTotal, pairs);
            printStage.removeButtons(); //Removes buttons for printing
            printStage.removeScrollPane(); //Removes scroll pane for printing
            print(printStage); //passes SummaryStagePrint object to print method              
        });
        
        hbButtons.setAlignment(Pos.CENTER);
        hbButtons.getChildren().addAll(btnOk, btnPrint);
        hbButtons.setPadding(new Insets(0, 0, 20, 0));
        return hbButtons;
    }
    
    private Node createCombinedPane(double subTotal, LinkedList<Pair<String, String>> pairs)
    {   
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPadding(new Insets(5, 5, 5, 5));
        scrollPane.setContent(gpOrders);
        scrollPane.setPrefWidth(100);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        NumberFormat nFormatter = NumberFormat.getCurrencyInstance();       
        Label lblSubtotal = new Label();
        lblSubtotal.setText(nFormatter.format(subTotal));
        Label lblTax = new Label();
        lblTax.setText(nFormatter.format((subTotal * 1.06) - subTotal));
        Label lblTotal = new Label();
        lblTotal.setText(nFormatter.format(subTotal * 1.06));
        
        GridPane gpTotal = new GridPane();
        gpTotal.setHgap(10);
        gpTotal.setVgap(2);
        gpTotal.setAlignment(Pos.CENTER_RIGHT);
        gpTotal.addRow(0, new Label("Subtotal: "), lblSubtotal);
        gpTotal.addRow(1, new Label("Tax: "), lblTax);
        gpTotal.addRow(2, new Label("Total: "), lblTotal);
     
        vbOrder.setAlignment(Pos.TOP_CENTER);
        vbOrder.setPrefWidth(250);
        vbOrder.setPadding(new Insets(10, 20, 10, 10));
        vbOrder.getChildren().addAll(scrollPane, gpTotal);

        HBox hbCustomer = new HBox(10);
        hbCustomer.setAlignment(Pos.CENTER);
        hbCustomer.setPadding(new Insets(10, 10, 10, 20));
        hbCustomer.getChildren().addAll(createTitleLabels(pairs), createCustomerLabels(pairs));
        
        HBox hbCombined = new HBox(10);
        hbCombined.getChildren().addAll(vbOrder, hbCustomer);
        return hbCombined;
    }
    
    private VBox createTitleLabels(LinkedList<Pair<String, String>> pairs)
    {
        VBox vbTitles = new VBox(10);
        for (Pair pair: pairs)
        {
            vbTitles.getChildren().add(new Label((String)pair.getLeft()));
        }
        return vbTitles;
    }
    
    private VBox createCustomerLabels(LinkedList<Pair<String, String>> pairs)
    {
        VBox vbCustomer = new VBox(10);
        for (Pair pair: pairs)
        {
            vbCustomer.getChildren().add(new Label((String)pair.getRight()));
        }
        return vbCustomer;
    }
    
    private void print(SummaryStagePrint print)
    {    
        Printer printer = Printer.getDefaultPrinter(); //Retrieves machine dependent default printer
        PrinterJob job = PrinterJob.createPrinterJob(printer); //Creates a PrinterJob
        if (job != null)
        {
            //Pass Stage to method that shows a print dialog
            if (job.showPrintDialog(print.getStage()))
            {
                boolean success = job.printPage(print.getRoot()); //Prints all nodes that are in the BorderPane graph
                if (success)
                {
                    job.endJob();
                }                    
            }
        }       
    }
    
    private void fillGridPane(List<OrderPane.OrderGroup> orderGroups)
    {
        //Header row
        gpOrders.addRow(0, new Label("Item"), new Label("Qty"), new Label("Price"));
        
        //Iterates through List of OrderGroups.  Adds each to seperate row of GridPane
        int row = 1;
        for (OrderPane.OrderGroup group: orderGroups)
        {
            gpOrders.addRow(row, new Label(group.getItem()), new Label(String.valueOf(group.getQuantity())), 
                        new Label(String.valueOf(group.getTotalPrice())));
            row++;
        }
    }
    
    protected HBox getHBoxButtons()
    {
        return hbButtons; 
    }
    
    protected GridPane getGridPaneOrders()
    {
        return gpOrders;
    }
    
    protected VBox getVboxOrder()
    {
        return vbOrder;
    }
    
    public void show()
    {
        getStage().showAndWait();
    }

    public Stage getStage() {
        return stage;
    }

    public BorderPane getRoot() {
        return root;
    }
}
