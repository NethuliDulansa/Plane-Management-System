import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class w2052116_PlaneManagement {
    private static final int TOTAL_ROWS = 4;
    private static final int[] SEATS_PER_ROW = {14, 12, 12, 14};
    private static final int AVAILABLE_SEATS = 0;
    private static final int SEATS_SOLD = 1;
    private static int[][] seats = new int[TOTAL_ROWS][];
    private static Ticket[] tickets = new Ticket[calculateTotalSeats()];


    public static void initializeSeats() {
        tickets = new Ticket[calculateTotalSeats()]; // Initialize tickets array
        for (int i = 0; i < TOTAL_ROWS; i++) {
            seats[i] = new int[SEATS_PER_ROW[i]];
            for (int j = 0; j < SEATS_PER_ROW[i]; j++) {
                seats[i][j] = AVAILABLE_SEATS;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Plane Management Application");
        initializeSeats(); // Ensure seats are initialized before displaying the menu
        userMenu();
    }


    public static void userMenu() {
        Scanner scanner = new Scanner(System.in);
        int option;

        while (true) {
            System.out.println("**************************************************************************");
            System.out.println("*                            Menu options                                *");
            System.out.println("**************************************************************************");
            System.out.println("1) Buy a seat");
            System.out.println("2) Cancel a seat");
            System.out.println("3) Find first available seat");
            System.out.println("4) Show seating plan");
            System.out.println("5) Print ticket information and total sales");
            System.out.println("6) Search ticket");
            System.out.println("0) Quit");
            System.out.println("**************************************************************************");

            System.out.println("Please select an option: ");

            try {
                option = scanner.nextInt();

                if (option >= 0 && option <= 6) {
                    switch (option) {
                        case 1:
                            buySeat(scanner);
                            break;
                        case 2:
                            cancelSeats(scanner);
                            break;
                        case 3:
                            findFirstAvailable();
                            break;
                        case 4:
                            showSeatingPlan();
                            break;
                        case 5:
                            print_tickets_info();
                            break;
                        case 6:
                            search_ticket(scanner);
                            break;
                        case 0:
                            System.out.println("You will exit from the program . Have good day !!");
                            return; // Exit the method and the loop
                    }
                } else {
                    System.out.println("Invalid option. Please enter a valid option (0-6).");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid option (0-6) .");
            } finally {
                scanner.nextLine(); // Consume the newline character
            }
        }
    }
    public static void buySeat(Scanner scanner) {
        String rowLetter;
        int seatNumber;

        do {
            // Prompt user for row letter
            System.out.println("Enter row letter (A, B, C, D): ");
            rowLetter = scanner.next().toUpperCase();
            if (!(rowLetter.equals("A") || rowLetter.equals("B") || rowLetter.equals("C") || rowLetter.equals("D"))) {
                System.out.println("Invalid row letter. Please enter a row from A to D.");
            }
        } while (!(rowLetter.equals("A") || rowLetter.equals("B") || rowLetter.equals("C") || rowLetter.equals("D")));

        do {
            try {
                // Prompt user for seat number
                System.out.println("Enter seat number: ");
                seatNumber = scanner.nextInt();
                int rowIndex = rowLetter.charAt(0) - 'A';
                if (seatNumber < 1 || seatNumber > SEATS_PER_ROW[rowIndex]) {
                    System.out.println("Invalid seat number. Please enter a valid seat number.");
                    continue;
                }
                if (seats[rowIndex][seatNumber - 1] == SEATS_SOLD) {
                    System.out.println("Seat " + rowLetter + seatNumber + " is already booked.");
                    System.out.println("You can book another seat !");
                    continue;
                }

                // Ask for person's information
                System.out.println("Enter name: ");
                String name = scanner.next();
                System.out.println("Enter surname: ");
                String surname = scanner.next();
                System.out.println("Enter email: ");
                String email = scanner.next();

                // Create a new Person object
                Person person = new Person(name, surname, email);

                // Create a new Ticket object and add it to the array of tickets
                Ticket ticket = new Ticket(rowLetter, seatNumber, calculateTicketPrice(rowLetter, seatNumber), person);
                addTicket(ticket); // Add the ticket to the array of tickets

                // Update the seats array to mark the seat as sold
                seats[rowIndex][seatNumber - 1] = SEATS_SOLD;

                System.out.println("Seat " + rowLetter + seatNumber + " purchased successfully.");

                // Save ticket information to file
                saveTicketToFile(ticket, calculateTicketPrice(rowLetter, seatNumber));

                return;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid seat number.");
                scanner.next(); // Consume invalid input
            }
        } while (true);
    }
    private static void addTicket(Ticket ticket) {
        for (int i = 0; i < tickets.length; i++) {
            if (tickets[i] == null) {
                tickets[i] = ticket;
                return;
            }
        }
        System.out.println("Error: No space available to add the ticket.");
    }
    public static void cancelSeats(Scanner scanner) {
        String rowLetter;
        int rowIndex;
        do {
            System.out.println("Enter row letter (A, B, C, D): ");
            rowLetter = scanner.next().toUpperCase();
            if (!rowLetter.matches("[A-D]")) {
                System.out.println("Invalid row letter. Please enter a row from A to D.");
            }
        } while (!rowLetter.matches("[A-D]"));

        int seatNumber;

        do {
            System.out.println("Enter seat number: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid seat number.");
                scanner.next();
            }
            seatNumber = scanner.nextInt();
            rowIndex = rowLetter.charAt(0) - 'A';
            if (seatNumber < 1 || seatNumber > SEATS_PER_ROW[rowIndex]) {
                System.out.println("Invalid seat number. Please enter a valid seat number.");
            }
        } while (seatNumber < 1 || seatNumber > SEATS_PER_ROW[rowIndex]);

        if (seats[rowIndex][seatNumber - 1] == SEATS_SOLD) {
            seats[rowIndex][seatNumber - 1] = AVAILABLE_SEATS;

            // Find the index of the ticket in the tickets array
            int ticketIndex = (rowIndex * SEATS_PER_ROW[rowIndex]) + (seatNumber - 1);

            // Remove the ticket from the array of tickets
            tickets[ticketIndex] = null;

            System.out.println("Seat " + rowLetter + seatNumber + " cancelled successfully.");
        } else {
            System.out.println("Seat " + rowLetter + seatNumber + " is already available.");
        }
    }
    public static void findFirstAvailable() {
        boolean found = false;
        for (int i = 0; i < TOTAL_ROWS; i++) {
            for (int j = 0; j < SEATS_PER_ROW[i]; j++) {
                if (seats[i][j] == AVAILABLE_SEATS) {
                    char rowLetter = (char) ('A' + i);
                    System.out.println("First available seat is : " + rowLetter + (j + 1));
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }
        if (!found) {
            System.out.println("No available seats all the seats are booked.");
        }
    }

    public static void showSeatingPlan() {
        System.out.println("Seating Plan:");
        System.out.println("Row\tSeats");
        for (int i = 0; i < TOTAL_ROWS; i++) {
            char rowLetter = (char) ('A' + i);
            System.out.print(rowLetter + "\t");
            for (int j = 0; j < SEATS_PER_ROW[i]; j++) {
                if (seats[i][j] == AVAILABLE_SEATS) {
                    System.out.print("O ");
                } else {
                    System.out.print("X ");
                }
            }
            System.out.println(); // Move to the next row
        }
    }
    public static void print_tickets_info() {
        double totalPrice = 0;

        System.out.println("Ticket Information:");
        for (int i = 0; i < tickets.length; i++) {
            if (tickets[i] != null && seats[tickets[i].getRow().charAt(0) - 'A'][tickets[i].getSeat() - 1] == SEATS_SOLD) {
                // Retrieve ticket information
                String row = tickets[i].getRow();
                int seat = tickets[i].getSeat();
                double price = tickets[i].getPrice();
                Person person = tickets[i].getPerson();

                // Create a new Ticket object with the associated Person object
                Ticket ticket = new Ticket(row, seat, price, person);

                // Call printTicketInfo method of Ticket class
                ticket.printTicketInfo();

                totalPrice += price;
            }
        }

        System.out.println("Total Sales: £" + totalPrice);
    }

    private static double calculateTicketPrice(String rowLetter, int seatNumber) {
        if (seatNumber >= 1 && seatNumber <= 5) {
            return 200;
        } else if (seatNumber >= 6 && seatNumber <= 9) {
            return 150;
        } else if (seatNumber >= 10 && seatNumber <= 14) {
            return 180;
        } else {
            return 0; // Invalid seat number
        }
    }

    private static int calculateTotalSeats() {
        int totalSeats = 0;
        for (int seatsInRow : SEATS_PER_ROW) {
            totalSeats += seatsInRow;
        }
        return totalSeats;
    }

    public static void search_ticket(Scanner scanner) {
        System.out.print("Enter row letter (A, B, C, D): ");
        String rowLetter = scanner.next().toUpperCase();

        int rowIndex = rowLetter.charAt(0) - 'A';

        System.out.print("Enter seat number: ");
        int seatNumber = scanner.nextInt();

        // Check if the seat is within the valid range for the specified row
        if (seatNumber < 1 || seatNumber > SEATS_PER_ROW[rowIndex]) {
            System.out.println("Invalid seat number. Please enter a valid seat number.");
            return;
        }

        if (seats[rowIndex][seatNumber - 1] == SEATS_SOLD) {
            // The seat is sold, so retrieve and print the ticket and person information
            for (Ticket ticket : tickets) {
                if (ticket != null && ticket.getRow().equals(rowLetter) && ticket.getSeat() == seatNumber) {
                    System.out.println("Ticket Information:");
                    ticket.printTicketInfo();
                    System.out.println("Personal Information:");
                    ticket.getPerson().printPersonInfo();
                    return;
                }
            }
        } else {
            System.out.println("This seat is available.");
        }
    }
    public static void saveTicketToFile(Ticket ticket, double price) {
        String fileName = "tickets_" + price + ".txt"; // File name based on ticket price
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(ticket.toString() + " - Ticket Price: £" + price + "\n"); // Include ticket price in the saved file
        } catch (IOException e) {
            System.out.println("An error occurred while saving the ticket to file.");
            e.printStackTrace();
        }
    }
}
