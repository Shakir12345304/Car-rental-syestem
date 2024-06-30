import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class Car {
    private String carId;
    private String brand;
    private String model;
    private double basePricePerDay;
    private boolean isAvailable;

    public Car(String carId, String brand, String model, double basePricePerDay) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = true;
    }

    public String getCarId() {
        return carId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double calculatePrice(int rentalDays) {
        return basePricePerDay * rentalDays;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void rent() {
        isAvailable = false;
    }

    public void returnCar() {
        isAvailable = true;
    }
}

class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
}

class Rental {
    private Car car;
    private Customer customer;
    private int days;

    public Rental(Car car, Customer customer, int days) {
        this.car = car;
        this.customer = customer;
        this.days = days;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }
}

class CarRentalSystem {
    private List<Car> cars;
    private List<Customer> customers;
    private List<Rental> rentals;

    public CarRentalSystem() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void rentCar(Car car, Customer customer, int days) {
        if (car.isAvailable()) {
            car.rent();
            rentals.add(new Rental(car, customer, days));
        } else {
            JOptionPane.showMessageDialog(null, "Car is not available for rent.");
        }
    }

    public void returnCar(Car car) {
        car.returnCar();
        Rental rentalToRemove = null;
        for (Rental rental : rentals) {
            if (rental.getCar() == car) {
                rentalToRemove = rental;
//                break;
            }
        }
        if (rentalToRemove != null) {
            rentals.remove(rentalToRemove);
        } else {
            JOptionPane.showMessageDialog(null, "Car was not rented.");
        }
    }

    public void showMenu() {
        JFrame frame = new JFrame("Car Rental System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new FlowLayout());

        JButton rentButton = new JButton("Rent a Car");
        JButton returnButton = new JButton("Return a Car");
        JButton exitButton = new JButton("Exit");

        rentButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                rentCarDialog();
            }
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnCarDialog();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        frame.add(rentButton);
        frame.add(returnButton);
        frame.add(exitButton);

        frame.setVisible(true);
    }

    private void rentCarDialog() {
        JTextField nameField = new JTextField(20);
        JTextField carIdField = new JTextField(10);
        JTextField daysField = new JTextField(5);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Enter your name:"));
        panel.add(nameField);
        panel.add(new JLabel("Available Cars:"));
        for (Car car : cars) {
            if (car.isAvailable()) {
                panel.add(new JLabel(car.getCarId() + " - " + car.getBrand() + " " + car.getModel()));
            }
        }
        panel.add(new JLabel("Enter the car ID you want to rent:"));
        panel.add(carIdField);
        panel.add(new JLabel("Enter the number of days for rental:"));
        panel.add(daysField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Rent a Car", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String customerName = nameField.getText();
            String carId = carIdField.getText();
            int rentalDays = Integer.parseInt(daysField.getText());

            Customer newCustomer = new Customer("CUS" + (customers.size() + 1), customerName);
            addCustomer(newCustomer);

            Car selectedCar = null;
            for (Car car : cars) {
                if (car.getCarId().equals(carId) && car.isAvailable()) {
                    selectedCar = car;
                    break;
                }
            }

            if (selectedCar != null) {
                double totalPrice = selectedCar.calculatePrice(rentalDays);
                String message = String.format("Customer ID: %s\nCustomer Name: %s\nCar: %s %s\nRental Days: %d\nTotal Price: $%.2f\nConfirm rental?",
                        newCustomer.getCustomerId(), newCustomer.getName(), selectedCar.getBrand(), selectedCar.getModel(), rentalDays, totalPrice);
                int confirm = JOptionPane.showConfirmDialog(null, message, "Rental Information", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    rentCar(selectedCar, newCustomer, rentalDays);
                    JOptionPane.showMessageDialog(null, "Car rented successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "Rental canceled.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid car selection or car not available for rent.");
            }
        }
    }

    private void returnCarDialog() {
        JTextField carIdField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Enter the car ID you want to return:"));
        panel.add(carIdField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Return a Car", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String carId = carIdField.getText();

            Car carToReturn = null;
            for (Car car : cars) {
                if (car.getCarId()==(carId) && !car.isAvailable()) {
                    carToReturn = car;
                    break;
                }
            }

            if (carToReturn != null) {
                Customer customer = null;
                for (Rental rental : rentals) {
                    if (rental.getCar() == carToReturn) {
                        customer = rental.getCustomer();
                        break;
                    }
                }

                if (customer != null) {
                    returnCar(carToReturn);
                    JOptionPane.showMessageDialog(null, "Car returned successfully by " + customer.getName());
                } else {
                    JOptionPane.showMessageDialog(null, "Car was not rented or rental information is missing.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid car ID or car is not rented.");
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        CarRentalSystem rentalSystem = new CarRentalSystem();

        Car car1 = new Car("C001", "Toyota", "Camry", 60.0); // Different base price per day for each car
        Car car2 = new Car("C002", "Honda", "Accord", 70.0);
        Car car3 = new Car("C003", "Mahindra", "Thar", 150.0);
        rentalSystem.addCar(car1);
        rentalSystem.addCar(car2);
        rentalSystem.addCar(car3);
        rentalSystem.showMenu();

    }
}
