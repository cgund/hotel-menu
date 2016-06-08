package menu;


/*
Encapsulates an item on a restaurant menu
*/
public class MenuItem 
{
    private String name;
    private double price;
    
    public MenuItem(String item, double price)
    {
        this.name = item;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String item) {
        this.name = item;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
