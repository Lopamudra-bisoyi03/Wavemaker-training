package vehicles;

public class Main {
    public static void main(String[] args) {
        try {
            Car toyota = new Car("Toyota", 250000);
            toyota.drive();
        } catch (Exception e) {
            System.out.println("An error occurred"+e.getMessage());
        }
        try {
            Bike hero = new Bike("Hero", 150000);
            hero.drive();
        } catch (Exception e) {
            System.out.println("An error occurred"+e.getMessage());
        }
        try {
            ElectricCar tesla = new ElectricCar("Tesla", 600000);
            tesla.drive();
            tesla.charge();
        } catch (Exception e) {
            System.out.println("An error occurred"+e.getMessage());
        }
        try {
            ElectricBike odyssey = new ElectricBike("Odyssey", 200000);
            odyssey.drive();
            odyssey.charge();
            throw new Exception();
        } catch (Exception e) {
            System.out.println("An error occurred"+e.getMessage());
        }

        }
    }

