import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/** Console menu for the Ticketing and Shop module (plug into member A's main menu when integrating). */
public class Main {
    private static final String PRODUCT_FILE = "products.txt";
    private static final String TICKET_FILE = "tickets.txt";

    private static final Scanner in = new Scanner(System.in);
    private static final StadiumShop shop = new StadiumShop();
    private static final TicketOffice office = new TicketOffice();

    public static void main(String[] args) throws InvalidDataException {
        loadSampleData();
        loadSavedTickets();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = ask("Choice: ");
            try {
                if (choice.equals("1")) {
                    sellTickets();
                } else if (choice.equals("2")) {
                    System.out.println(office.seatsRemaining(ask("Fixture id: ")) + " seat(s) left");
                } else if (choice.equals("3")) {
                    showProducts();
                } else if (choice.equals("4")) {
                    searchProducts();
                } else if (choice.equals("5")) {
                    shopSale();
                } else if (choice.equals("6")) {
                    shop.restock(ask("Product id: "), askInt("Quantity: "));
                    System.out.println("Restocked.");
                } else if (choice.equals("7")) {
                    System.out.println(office.attendanceReport());
                    System.out.println(office.ticketListReport());
                    System.out.println(shop.inventoryReport());
                    System.out.println(shop.lowStockReport());
                } else if (choice.equals("8")) {
                    save();
                } else if (choice.equals("9")) {
                    load();
                } else if (choice.equals("0")) {
                    running = false;
                } else {
                    System.out.println("Invalid choice.");
                }
            } catch (LeagueException e) {
                System.out.println("Not allowed: " + e.getMessage());
            } catch (java.io.IOException e) {
                System.out.println("File problem: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=== KPFL TICKETS & SHOP ===");
        System.out.println("1 Sell tickets   2 Seats remaining   3 Show products   4 Search product");
        System.out.println("5 Shop sale      6 Restock           7 Reports         8 Save   9 Load   0 Exit");
    }

    private static void sellTickets() throws LeagueException, java.io.IOException {
        String fixtureId = ask("Fixture id: ");
        String spectatorId = ask("Spectator id: ");
        TicketCategory category;
        try {
            category = TicketCategory.valueOf(ask("Category (GENERAL/VIP/STUDENT): ").toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidDataException("Unknown ticket category");
        }
        int qty = askInt("Quantity: ");
        double basePrice = askDouble("Base ticket price: ");
        double payment = askDouble("Payment: ");
        Receipt receipt = office.sellTickets(fixtureId, spectatorId, category, qty, basePrice, payment);
        System.out.println(receipt);
        System.out.println("Ticket codes:");
        for (Ticket t : office.getTicketsForReceipt(receipt.getReceiptNo())) {
            System.out.println("  " + t.getTicketId());
        }
        FileManager.saveTickets(office.getAllTickets(), TICKET_FILE);   // keep the ticket list in the file
        System.out.println("Ticket records saved to " + TICKET_FILE);
    }

    private static void showProducts() {
        for (Product p : shop.getAllProducts()) {
            System.out.println(p);
        }
    }

    private static void searchProducts() {
        for (Product p : shop.searchByName(ask("Keyword: "))) {
            System.out.println(p);
        }
    }

    private static void shopSale() throws LeagueException {
        Map<String, Integer> cart = new HashMap<>();
        String id = ask("Product id (blank to finish): ");
        while (!id.isEmpty()) {
            int qty = askInt("Quantity: ");
            if (cart.containsKey(id)) {
                cart.put(id, cart.get(id) + qty);
            } else {
                cart.put(id, qty);
            }
            id = ask("Product id (blank to finish): ");
        }
        double payment = askDouble("Payment: ");
        System.out.println(shop.processSale(cart, payment));
    }

    private static void save() throws java.io.IOException {
        FileManager.saveProducts(shop.getAllProducts(), PRODUCT_FILE);
        FileManager.saveTickets(office.getAllTickets(), TICKET_FILE);
        System.out.println("Saved.");
    }

    private static void load() throws java.io.IOException, InvalidDataException {
        for (Product p : FileManager.loadProducts(PRODUCT_FILE)) {
            if (shop.findById(p.getId()) == null) {
                shop.addProduct(p);
            }
        }
        loadTickets();
        System.out.println("Loaded.");
    }

    private static void loadTickets() throws java.io.IOException, InvalidDataException {
        for (Ticket t : FileManager.loadTickets(TICKET_FILE)) {
            office.restoreTicket(t);
        }
    }

    /** Reloads the ticket list at start-up so earlier sales are never lost. */
    private static void loadSavedTickets() {
        if (!new File(TICKET_FILE).exists()) {
            return;
        }
        try {
            loadTickets();
            System.out.println("Loaded earlier ticket sales from " + TICKET_FILE);
        } catch (java.io.IOException e) {
            System.out.println("File problem: " + e.getMessage());
        } catch (InvalidDataException e) {
            System.out.println("Ticket file problem: " + e.getMessage());
        }
    }

    private static void loadSampleData() throws InvalidDataException {
        office.registerFixture(new MatchInfo("F1", "Tivoli vs Harbour View",
            "2026-12-05 15:00", "National Stadium", 100));
        shop.addProduct(new Merchandise("M001", "KPFL Home Jersey", 6500, 20, "L", "Green"));
        shop.addProduct(new Merchandise("M002", "Team Flag", 1500, 30, "One size", "Black/Gold"));
        shop.addProduct(new FoodItem("F001", "Jerk Chicken Box", 1200, 40, false));
        shop.addProduct(new FoodItem("F002", "Bottled Water", 250, 100, true));
    }

    private static String ask(String prompt) {
        System.out.print(prompt);
        return in.nextLine().trim();
    }

    private static int askInt(String prompt) {
        return Integer.parseInt(ask(prompt));
    }

    private static double askDouble(String prompt) {
        return Double.parseDouble(ask(prompt));
    }
}
