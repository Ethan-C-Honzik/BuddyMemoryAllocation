/**
 * Assignment8
 * Operating Systems
 * Code written by Ethan Honzik
 * 5/20/2024
 * Simulation of the buddy memory allocation algorithm
 */
public class Main {

    void test1() {
        //Setup
        String output = "Ethan Honzik, 5.20.2024, Assignment 8\n\n";
        output += "Initializing buffers \n" +
                " Expected values: 10 512 size buffers, Status Ok\n\n";
        MemoryAllocator allocator = new MemoryAllocator(512, 10);

        //Test too large allocation
        output += (allocator.getAllocationData().toString() + "\n");
        output += ("Status is:\n" + allocator.getStatus() + "\n\n");
        output += "Requesting 700, expected values: \n Assigned address: -2\n\n";
        int address = allocator.allocate(700);
        output += ("Actual = assigned address: " + address + "\n\n");

        //Test 6 allocation
        output += "Requesting 6,  Expected values: \n" +
                " 9 510 size buffers, 1 254 size buffer, 1 126 size buffer, \n" +
                " 1 62 size buffer, 1 30 size buffer, 1 14 size buffer and 1 6 size buffer, Status OK\n\n";
        address = allocator.allocate(6);
        output += ("Actual = assigned address: " + address + "\n\n");
        output += (allocator.getAllocationData().toString() + "\n");
        output += ("Status is:\n" + allocator.getStatus() + "\n\n");

        //Test 6 deallocation
        output += "returning 6,  Expected values: \n" +
                " 10 510 size buffers, Status OK \n\n";
        allocator.deallocate(address);
        output += ("Actual = assigned address: " + address + "\n\n");
        output += (allocator.getAllocationData().toString() + "\n");
        output += ("Status is:\n" + allocator.getStatus() + "\n\n");

        //test allocating all available memory
        output += "Requesting 10 510 buffers,  Expected values: \n" +
                " 0 510 buffers, 0 for all buffers, Status Tight\n\n";
        int[] addresses = new int[10];
        for (int i = 0; i < 10; i++) {
            addresses[i] = allocator.allocate(510);
            output += ("Actual = assigned address: " + addresses[i] + "\n\n");
        }
        output += (allocator.getAllocationData().toString() + "\n");
        output += ("Status is:\n" + allocator.getStatus() + "\n\n");

        //request non existent buffer
        output += "Request additional buffer, expected values: \n Assigned address: -1\n\n";
        address = allocator.allocate(510);
        output += ("Actual = assigned address: " + address + "\n\n");

        output += (allocator.getAllocationData().toString() + "\n");
        output += ("Status is:\n" + allocator.getStatus() + "\n\n");

        //return all buffers
        output += "Return 10 510 buffers \n" +
                " Expected values:\n" +
                " 10 510 buffers, Status OK\n\n";
        for (int i = 0; i < 10; i++) {
            allocator.deallocate(addresses[i]);
        }
        output += (allocator.getAllocationData().toString() + "\n");
        output += ("Status is:\n" + allocator.getStatus() + "\n\n");

        //Reqeust 19 254 buffs
        addresses = new int[19];
        output += "Expected values: \n" +
                " 0 510 buffers, 1 254 size buffers, 0 126 size buffers, \n" +
                " 0 62 size buffers, 0 30 size buffers, 0 14 size buffers,  0 6 size buffers Status Tight \n\n";
        for (int i = 0; i < 19; i++) {
            addresses[i] = allocator.allocate(254);
            output += ("Actual = assigned address: " + addresses[i] + "\n\n");
        }
        output += (allocator.getAllocationData().toString() + "\n");
        output += ("Status is:\n" + allocator.getStatus() + "\n\n");

        //request non existent buffer
        output += "Request 1 510 Buffer after there is only 1 254 left, expected values: \n Assigned address: -1\n\n";
        address = allocator.allocate(510);
        output += ("Actual = assigned address: " + address + "\n\n");
        System.out.println(output);
        //return all buffers
        output += "Return 19 254 buffers \n" +
                " Expected values:\n" +
                " 10 510 buffers, Status OK\n\n";
        for (int i = 18; i >= 0; i--) {
            allocator.deallocate(addresses[i]);
        }
        output += (allocator.getAllocationData().toString() + "\n");
        output += ("Status is:\n" + allocator.getStatus() + "\n\n");

        System.out.println(output);
    }

    public void run() {
        test1();
    }

    public static void main(String[] args) {
        new Main().run();
    }
}