package menu;


import java.util.LinkedList;
import java.util.List;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


/*
Extends SummaryStage.  Almost identical functionality.  Removes button and scroll
pane nodes for better formatting
*/
class SummaryStagePrint extends SummaryStage
{
    protected SummaryStagePrint(List<OrderPane.OrderGroup> orderGroups, double subTotal, LinkedList<Pair<String, String>> pairs)
    {
        super(orderGroups, subTotal, pairs);
    }
    
    protected void removeButtons()
    {
        HBox hbButtons = getHBoxButtons();
        hbButtons.getChildren().clear();
    }
    
    protected void removeScrollPane()
    {
        VBox vbOrder = getVboxOrder();
        vbOrder.getChildren().remove(0);
        vbOrder.getChildren().add(0, getGridPaneOrders());
    }
}
