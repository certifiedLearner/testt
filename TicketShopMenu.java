
import java.util.Scanner;

public class TicketShopMenu {

    private static final Scanner in = new Scanner(System.in);

    public static void showMenu() {

        boolean running = true;

        while (running) {

            System.out.println("\n===== KPFL TICKETS & SHOP =====");
            System.out.println("1. Sell Tickets");
            System.out.println("2. Check Available Seats");
            System.out.println("3. Show Products");
            System.out.println("4. Search Products");
            System.out.println("5. Buy Shop Products");
            System.out.println("6. Restock Products");
            System.out.println("7. Add New Product");
            System.out.println("8. View Reports");
            System.out.println("9. Save Data");
            System.out.println("10. Load Data");
            System.out.println("0. Return to Main Menu");

            System.out.print("\nEnter choice: ");
            String choice = in.nextLine().trim();

            switch (choice) {

                case "1":
                    System.out.println("Sell Tickets selected.");
                    break;

                case "2":
                    System.out.println("Check Available Seats selected.");
                    break;

                case "3":
                    System.out.println("Show Products selected.");
                    break;

                case "4":
                    System.out.println("Search Products selected.");
                    break;

                case "5":
                    System.out.println("Buy Shop Products selected.");
                    break;

                case "6":
                    System.out.println("Restock Products selected.");
                    break;

                case "7":
                    System.out.println("Add New Product selected.");
                    break;

                case "8":
                    System.out.println("View Reports selected.");
                    break;

                case "9":
                    System.out.println("Save Data selected.");
                    break;

                case "10":
                    System.out.println("Load Data selected.");
                    break;

                case "0":
                    running = false;
                    System.out.println("Returning to Main Menu...");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
