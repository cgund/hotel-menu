
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/*
Displays items selected in MenuPane.  Allows user to confirm order.
*/
public class OrderPane extends VBox
{
    private int row = 1; //Keeps track of row number in GridPane
    private final List<OrderGroup> orders = new ArrayList<>(); //Holds all OrderGroup objects
    private double subTotal = 0;
    
    private TextField tfFirstName;
    private TextField tfLastName;
    private TextField tfPhone;
    private TextField tfEmail;
    private TextField tfRoomNum;
    private TextField tfInstr;
    private DatePicker datePicker;
    private ComboBox<String> cbTime;
    private final Label lblSubTotal = new Label("$0.00");
    private GridPane gpOrders;
    private GridPane gpFields;
    private static final Insets DEFAULT_INSETS = new Insets(10, 10, 10, 10);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
   
    public OrderPane()
    {
        BorderPane borderPane = new BorderPane();

        borderPane.setTop(createTitlePane());
        borderPane.setCenter(createSummaryPane());
        getChildren().addAll(borderPane, createFieldsPane());
    }
    
    private Node createTitlePane()
    {
        Label lblTitle = new Label("Order Summary");
        lblTitle.setAlignment(Pos.CENTER);
        
        HBox hbTitle = new HBox(10);
        hbTitle.setAlignment(Pos.CENTER);
        hbTitle.setPadding(DEFAULT_INSETS);
        hbTitle.getChildren().add(lblTitle);
        
        return hbTitle;
    }
    
    private Node createSummaryPane()
    {   
        gpOrders = new GridPane();
        gpOrders.setAlignment(Pos.CENTER);
        gpOrders.setHgap(30);
        gpOrders.setVgap(10);
        setTitleRow();
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPadding(new Insets(5, 5, 5, 5));
        scrollPane.setContent(gpOrders);
        scrollPane.setPrefHeight(300);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        HBox hbSubTotal = new HBox(10);
        hbSubTotal.setAlignment(Pos.CENTER_RIGHT);
        hbSubTotal.getChildren().addAll(new Label("Subtotal: "), lblSubTotal);
        
        VBox vbOrder = new VBox(10);
        vbOrder.setAlignment(Pos.CENTER);
        vbOrder.setPadding(new Insets(0, 0, 40, 0));
        vbOrder.setPrefHeight(400);
        vbOrder.getChildren().addAll(scrollPane, hbSubTotal);
        
        return vbOrder;
    }
    
    private Node createFieldsPane()
    {
        gpFields = new GridPane();
        gpFields.setHgap(10);
        gpFields.setVgap(10);
        
        tfFirstName = new TextField();
        tfFirstName.setPrefWidth(100);
        tfFirstName.setPromptText("First Name");
        
        tfLastName = new TextField();
        tfLastName.setPrefWidth(150);
        tfLastName.setPromptText("Last Name");
              
        tfPhone = new TextField();
        tfPhone.setPrefWidth(100);
        tfPhone.setPromptText("Phone #");
        
        tfEmail = new TextField();
        tfEmail.setPrefWidth(150);
        tfEmail.setPromptText("email");
        
        tfRoomNum = new TextField();
        tfRoomNum.setPrefWidth(100);
        tfRoomNum.setPromptText("Room #");

        tfInstr = new TextField();
        tfInstr.setPrefWidth(150);
        tfInstr.setPromptText("Special instructions");
        
        datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now()); //Sets default value to today's date
        datePicker.setPrefWidth(100);
        
        ObservableList<String> data = this.fillTimeBox();
        cbTime = new ComboBox(data);
        LocalTime time = LocalTime.now().plusMinutes(30); //Sets default value to 30 minutes in future
        cbTime.setValue(time.format(formatter));
                
        gpFields.addRow(0, tfFirstName, tfLastName);
        gpFields.addRow(1, tfPhone, tfEmail);
        gpFields.addRow(2, tfRoomNum, tfInstr);
        gpFields.addRow(3, new Label("Delivery Date: "), new Label("Delivery Time: "));
        gpFields.addRow(4, datePicker, cbTime);
        
        VBox vbCheckOut = new VBox(10);
        vbCheckOut.setAlignment(Pos.CENTER_RIGHT);
        vbCheckOut.setPrefWidth(320);
        vbCheckOut.getChildren().addAll(gpFields, createOrderButton());
        return vbCheckOut;
    }
    
    private Node createOrderButton()
    {
        Button btnOrder = new Button("Order");  
        btnOrder.setOnMouseClicked(e ->
        {
            if (!orders.isEmpty()) //MenuItem added to order
            {
                if (Validator.fieldsComplete(gpFields, tfInstr)) //Required fields complete
                {
                    try
                    {
                        /*
                        Uses static methods in Validator class to check user input
                        */
                        Validator.phone(tfPhone.getText());
                        LocalDate date = datePicker.getValue();
                        String selectedTime = cbTime.getValue();
                        Validator.dateRange(date, 0, 7); //Checks for acceptable date range
                        Validator.time(date, selectedTime); //Checks for acceptable time range
                        String[] labels = {tfFirstName.getText() + " " + tfLastName.getText(), 
                                           tfPhone.getText(), tfEmail.getText(), tfRoomNum.getText(), 
                                           date.toString(), selectedTime};
                        List<String> list = Arrays.asList(labels);
                        LinkedList<Pair<String, String>> pairs = new LinkedList<>();
                        pairs.add(new Pair("Name: ", list.get(list.indexOf(tfFirstName.getText() + " " + tfLastName.getText()))));
                        pairs.add(new Pair("Phone: ", list.get(list.indexOf(tfPhone.getText()))));
                        pairs.add(new Pair("email: ", tfEmail.getText()));
                        pairs.add(new Pair("Room #: ", tfRoomNum.getText()));
                        pairs.add(new Pair("Delivery Date: ", date.toString()));
                        pairs.add(new Pair("Delivery Time: ", selectedTime));
                       
                        //Create a SummaryStage
                        SummaryStage confirm = new SummaryStage(orders, subTotal, pairs);
                        confirm.show();
                        resetMenuOrder();
                    }
                    catch (IllegalArgumentException ex)
                    {
                        displayAlert("Incorrect Format", ex.getMessage());
                    }
                }
                else
                {
                    displayAlert("Missing Fields", "All fields required (except"
                            + "'Special Instructions')");
                }                
            }
            else
            {
                displayAlert("Empty Order", "No items ordered");
            }
        });
        return btnOrder;
    }
    
    public void displayAlert(String title, String message)
    {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();        
    }
    
    /*
    Creates a new OrderGroup. Adds OrderGroup properties to GridPane, adds
    OrderGroup object to List of OrderGroups
    */
    public void addMenuItem(String menuItem, Double price)
    {
        OrderGroup group = new OrderGroup(menuItem, 1, price);
        gpOrders.addRow(row++, group.getLblItem(), group.getSpinner(), 
                  group.getLblPrice(), group.getBtnRemove());
        orders.add(group); //Collection to hold all OrderGroup objects
        updateSubTotal();
    }
    
    /*
    Updates subtotal displayed to user
    */
    public void updateSubTotal()
    {
        double runningTotal = 0;
        for (OrderGroup group: orders)
        {
            runningTotal += group.getPrice();
        }
        subTotal = runningTotal;
        NumberFormat nFormatter = NumberFormat.getCurrencyInstance();
        lblSubTotal.setText(nFormatter.format(subTotal));
    }
    
    /*
    Returns an ObservableList of Strings that represent 15 minute time intervals
    */
    public final ObservableList<String> fillTimeBox()
    {
        ObservableList<String> data = FXCollections.observableArrayList();
        
        LocalTime time = LocalTime.of(0, 0);
        for (int i = 0; i < 96; i++)
        {
            String t = time.format(formatter);
            data.add(t);
            time = time.plusMinutes(15);
        }
        return data;
    }
    
    /*
    Resets menu order
    */
    public final void resetMenuOrder()
    {
        row = 1;
        orders.clear();
        subTotal = 0;
        lblSubTotal.setText("$0.00");
        gpOrders.getChildren().clear();
        setTitleRow();
        ObservableList<Node> list = gpFields.getChildren();
        for (Node node: list)
        {
            if (!(node instanceof Label))
            {
                if (node instanceof TextField)
                {
                    TextField tf = (TextField)node;
                    tf.setText("");
                }
                else if (node instanceof DatePicker)
                {
                    DatePicker dp = (DatePicker)node;
                    dp.setValue(LocalDate.now());
                }
                else
                {
                    ComboBox cb = (ComboBox)node;
                    cb.setValue(formatter.format(LocalTime.now().plusMinutes(30)));
                }                
            }
        }
    }

    /*
    Resets GridPane title row after the elements in its ObservableList have been
    cleared in the resetMenuOrder method
    */
    private void setTitleRow() 
    {
        Label item = new Label("Item");
        item.setFont(Font.font(null, FontWeight.BOLD, 12));
        Label quantity = new Label("Quantity");
        quantity.setFont(Font.font(null, FontWeight.BOLD, 12));
        Label price = new Label("Price");
        price.setFont(Font.font(null, FontWeight.BOLD, 12));
        gpOrders.addRow(0, item, quantity, price);
    }

    /*
    Inner class to encapsulate order records
    */
    class OrderGroup extends HBox
    {
        private final String item;
        private int quantity;
        private Double price;
        private final Label lblItem;
        private Spinner<Integer> spinner;
        private Label lblPrice;
        private final Button btnRemove;

        public OrderGroup(String item, Integer quantity, Double itemPrice) 
        {
            super();
            this.item = item;
            this.quantity = quantity;
            this.price = itemPrice;
            spinner = new Spinner();
            spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 1));
            spinner.setPrefWidth(55);
            /*
            Recalculates subtotal whenever the user changes the quantity of an item
            ordered
            */
            spinner.setOnMouseClicked(e ->
            {
                this.quantity = spinner.getValue();
                this.price = itemPrice * (double) this.quantity;
                lblPrice.setText(this.price.toString());
                updateSubTotal();
            });

            lblItem = new Label(item);
            lblPrice = new Label(itemPrice.toString());
            btnRemove = new Button("Remove");
            btnRemove.setOnMouseClicked(e -> 
            {
                remove();
            });

            this.setSpacing(20);
            this.getChildren().addAll(lblItem, spinner, lblPrice, btnRemove);
        }
        
        public String getItem()
        {
            return item;
        }
        
        public Integer getQuantity()
        {
            return quantity;
        }
        public Double getPrice() {
            return price;
        }

        public Label getLblItem() {
            return lblItem;
        }

        public Spinner<Integer> getSpinner() {
            return spinner;
        }

        public Label getLblPrice() {
            return lblPrice;
        }

        public Button getBtnRemove() {
            return btnRemove;
        }
        
        /*
        Removes OrderGroup from List and ObservableList of GridPane.  Calls
        updateSubTotal method
        */
        public final void remove()
        {
            orders.remove(this);
            gpOrders.getChildren().removeAll(lblItem, spinner, 
                               lblPrice, btnRemove);
            updateSubTotal();        
        }
    }
}
