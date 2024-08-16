package vehicles;

public class ElectricBike implements ElectricVehicle{
    private String name;
    private double price;
    public ElectricBike(String name, double price) {
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
        System.out.println(name+" "+"charge");
    }

    @Override
    public void charge() {
    }
}
