package vehicles;

public class Bike implements Vehicle {
    private String name;
    private double price;
    public Bike(String name, double price) {
        this.price = price;
        this.name = name;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public double getPrice() {
        return price;
    }
    @Override
    public void drive() {
        System.out.println(name+" "+"drives");
    }
}
