
import java.util.InputMismatchException;
import java.util.Scanner;

public class w2052116_PlaneManagement {

        //Define constants for seats per row and total rows
        private static final int TOTAL_ROWS = 4;
        private static final int[] SEATS_PER_ROW = {14, 12, 12, 14};
        //Define constants for availability of the seats
        private static final int AVAILABLE_SEATS = 0;
        private static final int SEATS_SOLD = 1;
        //Define a 2D array to represent seats
        private static int[][] seats = new int[TOTAL_ROWS][];
        //Define a 2D array to store tickets
        private static Ticket[] tickets = new Ticket[countTotalSeats()];

        //Method to initialize the ticket system
        public static void setupTicketSystem() {
            //Defines the ticket array
            tickets = new Ticket[countTotalSeats()]; // Initialize tickets array
            for (int i = 0; i < TOTAL_ROWS; i++) {
                seats[i] = new int[SEATS_PER_ROW[i]];
                //Defines the seats array
                for (int j = 0; j < SEATS_PER_ROW[i]; j++) {
                    seats[i][j] = AVAILABLE_SEATS;
                }
            }
        }

        //Main method of the program
        public static void main(String[] args) {
            // Get the user choice input
            System.out.println("Welcome to Plane Management Application");

            Scanner input = new Scanner(System.in);
            String choiceOfUser;

            while (true) {
                System.out.println("Would you like to start the program? (y/n) ");
                choiceOfUser = input.next();

                if (choiceOfUser.equalsIgnoreCase("y")) {
                    setupTicketSystem();

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
                            option = input.nextInt();

                            if (option >= 0 && option <= 6) {
                                switch (option) {
                                    case 1:
                                        buy_seat(input);
                                        break;
                                    case 2:
                                        cancel_seat(input);
                                        break;
                                    case 3:
                                        find_first_available();
                                        break;
                                    case 4:
                                        show_seating_plan();
                                        break;

                                    case 5:
                                        print_tickets_info();
                                        break;
                                    case 6:
                                        search_ticket(input);
                                        break;
                                    case 0:
                                        System.out.println("You will exit from the program . Have a good day !!");
                                        return;
                                }
                            } else {
                                System.out.println("Invalid option. Please enter a valid option (0-6).");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input type!! Please enter a valid option (0-6).");
                            input.next();
                        } finally {
                            input.nextLine();
                        }
                    }
                } else if (choiceOfUser.equalsIgnoreCase("n")) {
                    System.out.println("We appreciate you have checked at the plane management application. Thank you!");
                    break;
                } else {
                    System.out.println("Invalid choice !! Please enter 'y' to start the booking or 'n' to start booking later.");
                }
            }
        }


        //Add tickets to the array of tickets
        private static void addTicketToArray(Ticket ticket) {
            for (int i = 0; i < tickets.length; i++) {
                if (tickets[i] == null) {
                    tickets[i] = ticket;
                    return;
                }
            }
            System.out.println("Error: No space available to add the ticket.");
        }
    //Method for buying seats
    public static void buy_seat(Scanner scanner) {
        String rowLetter;
        int seatNumber;
        // Validate row letter input
        do {
            // Ask the user for row letter
            System.out.println("Enter row letter (A, B, C, D): ");
            rowLetter = scanner.next().toUpperCase();
            if (!(rowLetter.equals("A") || rowLetter.equals("B") || rowLetter.equals("C") || rowLetter.equals("D"))) {
                System.out.println("Invalid row letter. Please enter a row from A to D.");
            }
        } while (!(rowLetter.equals("A") || rowLetter.equals("B") || rowLetter.equals("C") || rowLetter.equals("D")));
        //Calculate row index from row letter
        int indexOfRow = rowLetter.charAt(0) - 'A';
        String errorMessageForRow = (indexOfRow == 0 || indexOfRow == 3) ? "Enter a number between 1 - 14" : "Enter a number between 1 - 12";

        do {
            try {
                // Ask the user for seat number
                // Validate seat number input
                System.out.println("Enter seat number: ");
                seatNumber = scanner.nextInt();
                if (seatNumber < 1 || seatNumber > SEATS_PER_ROW[indexOfRow]) {
                    System.out.println("Invalid seat number. " + errorMessageForRow);
                    continue;
                }
                //Check if the seat is already booked
                if (seats[indexOfRow][seatNumber - 1] == SEATS_SOLD) {
                    System.out.println("Seat " + rowLetter + seatNumber + " is already booked.");
                    System.out.println("You can book another seat !");
                    continue;
                }
                seats[indexOfRow][seatNumber - 1] = SEATS_SOLD;

                // Ask the user for personal information
                System.out.println("Enter your name: ");
                String name = scanner.next();
                System.out.println("Enter your surname: ");
                String surname = scanner.next();

                String email;
                // Validate the email input
                do {
                    System.out.println("Enter your email: ");
                    email = scanner.next();
                    if (!email.contains("@")) {
                        System.out.println("The email you entered is wrong. Please enter a valid email address.");
                    }
                } while (!email.contains("@"));

                // Create a new Person object
                Person person = new Person(name, surname, email);

                // Create a new Ticket object
                Ticket ticket = new Ticket(rowLetter, seatNumber, calculateTicketCost(rowLetter, seatNumber), person);
                addTicketToArray(ticket); // Add the ticket to the array of tickets

                System.out.println("Seat " + rowLetter + seatNumber + " purchased successfully.");

                // Save ticket information to a text file
                ticket.saveToTextFile(ticket, calculateTicketCost(rowLetter, seatNumber));

                return;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid seat number.");
                scanner.next(); // Consume invalid input
            }
        } while (true);
    }


    // Method for canceling seats
    public static void cancel_seat(Scanner scanner) {
        // Ask the user for row letter
        String rowLetter;
        int indexOfRow;
        // Validate row letter input
        do {
            System.out.println("Enter row letter (A, B, C, D): ");
            rowLetter = scanner.next().toUpperCase();
            indexOfRow = rowLetter.charAt(0) - 'A';
            if (indexOfRow < 0 || indexOfRow >= TOTAL_ROWS) {
                System.out.println("Invalid row letter. Please enter a row from A to D.");
            }
        } while (indexOfRow < 0 || indexOfRow >= TOTAL_ROWS);

        int seatNumber;

        String errorMessageForRow = (indexOfRow == 0 || indexOfRow == 3) ? "Enter a number between 1 - 14" : "Enter a number between 1 - 12";

        // Validate seat number input
        do {
            System.out.println("Enter seat number: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a valid seat number.");
                scanner.next(); // Consume invalid input
            }
            seatNumber = scanner.nextInt();

            if (seatNumber < 1 || seatNumber > SEATS_PER_ROW[indexOfRow]) {
                System.out.println("Invalid seat number. " + errorMessageForRow);
            }
        } while (seatNumber < 1 || seatNumber > SEATS_PER_ROW[indexOfRow]);

        // Check if the seat is already booked
        if (seats[indexOfRow][seatNumber - 1] == SEATS_SOLD) {
            seats[indexOfRow][seatNumber - 1] = AVAILABLE_SEATS;

            // Find the index of the ticket in the array of tickets
            int ticketIndex = 0;
            for (int i = 0; i < tickets.length; i++) {
                if (tickets[i] != null && tickets[i].getRow().equals(rowLetter) && tickets[i].getSeat() == seatNumber) {
                    ticketIndex = i;
                    break;
                }
            }

            // Remove the ticket from the array of tickets
            tickets[ticketIndex] = null;

            System.out.println("Seat " + rowLetter + seatNumber + " canceled successfully.");
        } else {
            System.out.println("Seat " + rowLetter + seatNumber + " is already available.");
        }
    }

    //Method for find the first available seat in the plane
        public static void find_first_available() {
            boolean found = false;
            //Iterate through each row and seat to find the first available seat
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
                System.out.println("No available seats !! All the seats are booked.");
            }
        }


        // Method to display the seating plan of the plane
        public static void show_seating_plan() {
            System.out.println("Seating Plan:");
            System.out.println("Row\tSeats");
            //Iterate through each row and seat to print the seating plan
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
                System.out.println();
            }
        }





            //Method to print the ticket information of all the sold tickets during a session
        public static void print_tickets_info() {
            double totalPrice = 0;

            System.out.println("All the sold Tickets' Information:");
            //Iterate through each ticket
            for (int i = 0; i < tickets.length; i++) {
                if (tickets[i] != null && seats[tickets[i].getRow().charAt(0) - 'A'][tickets[i].getSeat() - 1] == SEATS_SOLD) {
                    // Retrieve ticket information
                    System.out.println("-----Ticket and Person Information-----");
                    String row = tickets[i].getRow();
                    int seat = tickets[i].getSeat();
                    double price = tickets[i].getPrice();
                    Person person = tickets[i].getPerson();

                    //Instantiate a new Ticket object and link it with the corresponding Person instance.
                    Ticket ticket = new Ticket(row, seat, price, person);

                    //Invoke the printTicketInfo method belonging to the Ticket class.
                    ticket.printTicketsInfo();

                    totalPrice += price;
                }
            }
            System.out.println("-----Total Sales of the Session-----");
            System.out.println("£" + totalPrice);
        }

        //Method for calculate the ticket price based on the row letter and seat number
        private static double calculateTicketCost(String rowLetter, int seatNumber) {
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

        //Calculate the total number of seats in plane system
        private static int countTotalSeats() {
            int totalSeats = 0;
            for (int seatsInRow : SEATS_PER_ROW) {
                totalSeats += seatsInRow;
            }
            return totalSeats;
        }

        //Method for searching a ticket in the plane system
        public static void search_ticket(Scanner scanner) {
            String rowLetter;
            int indexOfRow;
            int seatNumber;

            // Validate row letter input
            do {
                System.out.print("Enter row letter (A, B, C, D): ");
                rowLetter = scanner.next().toUpperCase();
                indexOfRow= rowLetter.charAt(0) - 'A';

                if (indexOfRow < 0 || indexOfRow >= TOTAL_ROWS) {
                    System.out.println("Invalid row letter. Please enter a row from A to D.");
                }
            } while (indexOfRow < 0 || indexOfRow >= TOTAL_ROWS);

            // Validate seat number input
            do {
                System.out.print("Enter seat number: ");
                while (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a valid seat number.");
                    scanner.next(); // Consume invalid input
                }
                seatNumber = scanner.nextInt();

                if (seatNumber < 1 || seatNumber > SEATS_PER_ROW[indexOfRow]) {
                    System.out.println("Invalid seat number. Please enter a valid seat number.");
                }
            } while (seatNumber < 1 || seatNumber > SEATS_PER_ROW[indexOfRow]);

            boolean foundTicket = false;

            if (seats[indexOfRow][seatNumber - 1] == SEATS_SOLD) {
                System.out.println("This ticket is already booked !!");

                // Since the seat is sold, retrieve and display both the ticket and personal information

                for (Ticket ticket : tickets) {
                    if (ticket != null && ticket.getRow().equals(rowLetter) && ticket.getSeat() == seatNumber) {
                        System.out.println("-----Ticket and Personal Information-----");
                        ticket.printTicketsInfo();
                        foundTicket = true;

                        // Print personal information only if it has not been displayed previously

                        if (!foundTicket) {
                            System.out.println("-----Personal Information-----");
                            ticket.getPerson().printPersonalInfo();
                            foundTicket = true;
                        }
                    }
                }
            } else {
                System.out.println("This seat is available.");
            }
        }


    }





















