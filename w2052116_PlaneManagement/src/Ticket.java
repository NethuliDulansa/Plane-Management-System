import java.io.FileWriter;
import java.io.IOException;

public class Ticket {
    // Instance variables
    private String row;
    private int seat;
    private double price;
    private Person person;

    // Constructor
    public Ticket(String row, int seat, double price, Person person) {
        this.row = row;
        this.seat = seat;
        this.price = price;
        this.person = person;
    }

    // Creating getters for instance variables
    public String getRow() {
        return row;
    }

    public int getSeat() {
        return seat;
    }

    public double getPrice() {
        return price;
    }

    public Person getPerson() {
        return person;
    }

    // Creating setters instance variables
    public void setRow(String row) {
        this.row = row;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    // Method to print ticket information
    public void printTicketsInfo() {
        System.out.println("Row: " + row);
        System.out.println("Seat: " + seat);
        System.out.println("Price: £" + price);
        person.printPersonalInfo();


    }
    // Method to save each sold ticket into a text file
    public  void saveToTextFile(Ticket ticket, double price) {
        String ticketName = ticket.getRow() + ticket.getSeat(); // Generating a unique ticket name based on row and seat
        try {
            FileWriter writer = new FileWriter(ticketName + ".txt");
            writer.write("-----Ticket Information-----\n");
            writer.write("Seat: " + ticketName + "\n");
            writer.write("Price: £" + price + "\n");
            writer.write("-----Personal Information-----\n");
            writer.write("Name: " + ticket.getPerson().getName() + "\n");
            writer.write("Surname: " + ticket.getPerson().getSurname() + "\n");
            writer.write("Email: " + ticket.getPerson().getEmail() + "\n");
            writer.close();
            System.out.println("Ticket information saved to " + ticketName + ".txt");
        } catch (IOException e) {
            System.out.println("Error occurred while saving ticket information to a file. ");
            e.printStackTrace();
        }
    }

}
