import java.util.Random;

public class StudyRoomReservationSystem {
    private StudyRoom[] rooms = new StudyRoom[10];

    private void DisplayayFinalStatus() {
        for (int i = 0; i < 10; i++) {
            System.out.println("Room " + i + " Current Reservations: " + rooms[i].reservations + " Available: " + rooms[i].available);
        }
    }

    private StudyRoomReservationSystem() {
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            rooms[i] = new StudyRoom(i, rand.nextInt(8));
        }
    }

    private void reserveStudyRoom(int roomNumber) throws StudyRoomUnavailableException {
        synchronized (rooms[roomNumber]) {
            StudyRoom room = rooms[roomNumber];
            if (room.available) {
                room.reservations++;
                System.out.println("Room " + roomNumber + " reserved. Reservations: " + room.reservations
                        + " Available: " + room.available);
                if (room.reservations == room.capacity) {
                    room.available = false;
                }

            } else {
                throw new StudyRoomUnavailableException("Room " + roomNumber + " is not available.");
            }

        }
    }

    private void releaseStudyRoom(int roomNumber) {
        // System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        synchronized (rooms[roomNumber]) {
            StudyRoom room = rooms[roomNumber];
            if (room.reservations > 0) {
                System.out.println("Room " + roomNumber + " released. Reservations: " + room.reservations
                        + " Available: " + room.available);
                room.reservations--;
                if (room.reservations < room.capacity) {
                    room.available = true;
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {

        StudyRoomReservationSystem system = new StudyRoomReservationSystem();
        Random rand = new Random();
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 200; i++) {
                    // System.out.println(i);
                    try {
                        system.reserveStudyRoom(rand.nextInt(10));
                    } catch (StudyRoomUnavailableException e) {
                        // TODO Auto-generated catch block
                        System.out.println(e.getMessage());
                    }
                    // sleep for 10 milliseconds
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                for (int i = 0; i < 50; i++) {
                    system.releaseStudyRoom(rand.nextInt(10));
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        
        System.out.println("\n\nReservation process completed\n\n");

        system.DisplayayFinalStatus();

        System.out.println("\n\nDone");



    }
}
