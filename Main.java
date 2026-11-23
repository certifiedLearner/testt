
public static void main(String[] args) throws InvalidDataException {
    loadSampleData();
    loadSavedTickets();

    boolean running = true;

    while (running) {
        printMenu();
        String choice = ask("Choice: ");

        try {
            switch (choice) {
                case "1":
                    sellTickets();
                    break;

                case "2":
                    System.out.println(office.seatsRemaining(ask("Fixture id: ")) + " seats left");
                    break;

                case "3":
                    showProducts();
                    break;

                case "4":
                    searchProducts();
                    break;

                case "5":
                    shopSale();
                    break;

                case "6":
                    shop.restock(ask("Product id: "), askInt("Quantity: "));
                    System.out.println("Restocked successfully.");
                    break;

                case "7":
                    System.out.println(office.attendanceReport());
                    System.out.println(office.ticketListReport());
                    System.out.println(shop.inventoryReport());
                    System.out.println(shop.lowStockReport());
                    break;

                case "8":
                    save();
                    break;

                case "9":
                    load();
                    break;

                case "0":
                    running = false;
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } catch (LeagueException e) {
            System.out.println("Error: " + e.getMessage());

        } catch (java.io.IOException e) {
            System.out.println("File error: " + e.getMessage());

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        }
    }
}

private static void printMenu() {
    System.out.println("\n===== KPFL TICKETS & SHOP =====");
    System.out.println("1. Sell Tickets");
    System.out.println("2. Check Available Seats");
    System.out.println("3. Show Products");
    System.out.println("4. Search Products");
    System.out.println("5. Buy Shop Products");
    System.out.println("6. Restock Products");
    System.out.println("7. View Reports");
    System.out.println("8. Save Data");
    System.out.println("9. Load Data");
    System.out.println("0. Exit");
}
