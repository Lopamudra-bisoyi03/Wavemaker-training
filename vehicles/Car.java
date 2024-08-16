package vehicles;

public class Car implements Vehicle {
    private String name;
    private double price;

    public Car(String name, double price) {
        this.name = name;
        this.price = price;
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
        System.out.println(name + " "+ "drives");
    }

}
