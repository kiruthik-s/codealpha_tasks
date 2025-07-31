import java.io.*;
import java.util.*;

// Room Class
class Room {
    int roomNumber;
    String category;
    double price;
    boolean isAvailable;

    Room(int roomNumber, String category, double price) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.price = price;
        this.isAvailable = true;
    }
}

// Booking Class
class Booking {
    String guestName;
    int roomNumber;
    Date checkInDate;
    Date checkOutDate;
    double amountPaid;

    Booking(String guestName, int roomNumber, Date checkInDate, Date checkOutDate, double amountPaid) {
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.amountPaid = amountPaid;
    }

    @Override
    public String toString() {
        return guestName + " | Room: " + roomNumber + " | Check-in: " + checkInDate +
                " | Check-out: " + checkOutDate + " | Paid: Rs." + amountPaid;
    }
}

// Hotel Class
class Hotel {
    List<Room> rooms = new ArrayList<>();
    List<Booking> bookings = new ArrayList<>();
    File bookingFile = new File("bookings.txt");

    // Constructor to initialize rooms & load bookings
    Hotel() {
        // Add multiple rooms for each category
        for (int i = 101; i <= 103; i++)
            rooms.add(new Room(i, "Standard", 1000));
        for (int i = 201; i <= 203; i++)
            rooms.add(new Room(i, "Deluxe", 2000));
        for (int i = 301; i <= 303; i++)
            rooms.add(new Room(i, "Suite", 5000));

        loadBookingsFromFile();
    }

    // Search available rooms
    void searchRooms() {
        System.out.println("\nAvailable Rooms:");
        boolean found = false;
        for (Room r : rooms) {
            if (r.isAvailable) {
                System.out.println(r.roomNumber + " - " + r.category + " - Rs." + r.price);
                found = true;
            }
        }
        if (!found)
            System.out.println("No rooms available at the moment.");
    }

    // Book room
    void bookRoom(String guestName, int roomNumber, Date checkIn, Date checkOut) {
        Room selectedRoom = null;
        for (Room r : rooms) {
            if (r.roomNumber == roomNumber) {
                selectedRoom = r;
                break;
            }
        }
        if (selectedRoom == null) {
            System.out.println("Invalid room number.");
            return;
        }
        if (!selectedRoom.isAvailable) {
            System.out.println("Room not available.");
            return;
        }

        double totalAmount = selectedRoom.price; // Fixed per booking (can extend to days)
        System.out.println("Processing Payment of Rs." + totalAmount + "...");
        System.out.println("Payment Successful!");

        selectedRoom.isAvailable = false;
        Booking b = new Booking(guestName, roomNumber, checkIn, checkOut, totalAmount);
        bookings.add(b);
        saveBookingsToFile();
        System.out.println("✅ Booking Confirmed: " + b);
    }

    // Cancel booking
    void cancelBooking(String guestName, int roomNumber) {
        Booking toCancel = null;
        for (Booking b : bookings) {
            if (b.guestName.equalsIgnoreCase(guestName) && b.roomNumber == roomNumber) {
                toCancel = b;
                break;
            }
        }
        if (toCancel != null) {
            bookings.remove(toCancel);
            for (Room r : rooms) {
                if (r.roomNumber == roomNumber) {
                    r.isAvailable = true;
                    break;
                }
            }
            saveBookingsToFile();
            System.out.println("❌ Booking cancelled for " + guestName);
        } else {
            System.out.println("No booking found for cancellation.");
        }
    }

    // View all bookings
    void viewBookings() {
        System.out.println("\nAll Bookings:");
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }
        for (Booking b : bookings) {
            System.out.println(b);
        }
    }

    // Save bookings to file
    void saveBookingsToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(bookingFile))) {
            for (Booking b : bookings) {
                pw.println(b.guestName + "," + b.roomNumber + "," + b.checkInDate.getTime() + ","
                        + b.checkOutDate.getTime() + "," + b.amountPaid);
            }
        } catch (IOException e) {
            System.out.println("Error saving bookings: " + e.getMessage());
        }
    }

    // Load bookings from file
    void loadBookingsFromFile() {
        if (!bookingFile.exists())
            return;
        try (BufferedReader br = new BufferedReader(new FileReader(bookingFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String guest = parts[0];
                int roomNo = Integer.parseInt(parts[1]);
                Date checkIn = new Date(Long.parseLong(parts[2]));
                Date checkOut = new Date(Long.parseLong(parts[3]));
                double paid = Double.parseDouble(parts[4]);
                bookings.add(new Booking(guest, roomNo, checkIn, checkOut, paid));
                for (Room r : rooms) {
                    if (r.roomNumber == roomNo)
                        r.isAvailable = false;
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading bookings: " + e.getMessage());
        }
    }
}

// Main Class
public class HotelReservationSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Hotel hotel = new Hotel();

        while (true) {
            System.out.println("\n=== Hotel Reservation System ===");
            System.out.println("1. Search Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View All Bookings");
            System.out.println("5. Exit");
            System.out.print("Choose: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    hotel.searchRooms();
                    break;
                case 2:
                    System.out.print("Guest Name: ");
                    String name = sc.nextLine();
                    System.out.print("Room Number: ");
                    int room = sc.nextInt();
                    sc.nextLine();
                    hotel.bookRoom(name, room, new Date(), new Date());
                    break;
                case 3:
                    System.out.print("Guest Name: ");
                    String cancelName = sc.nextLine();
                    System.out.print("Room Number: ");
                    int cancelRoom = sc.nextInt();
                    hotel.cancelBooking(cancelName, cancelRoom);
                    break;
                case 4:
                    hotel.viewBookings();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }
}
