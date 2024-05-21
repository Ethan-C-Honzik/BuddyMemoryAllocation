import java.util.ArrayList;
import java.util.List;

/**
 * Assignment8
 * Operating Systems
 * Code written by Ethan Honzik
 * 5/20/2024
 * Simulation of the buddy memory allocation algorithm
 */
public class Main {
    MemoryAllocator allocator = new MemoryAllocator(512, 10);
    String output = "";

    void tooLargeTest() {
        //Test too large allocation
        output += (allocator.getAllocationData().toString() + "\n");
        output += ("Status is:\n" + allocator.getStatus() + "\n\n");
        output += "Requesting 700, expected values: \n Assigned address: -2\n\n";
        int address = allocator.allocate(700);
        output += ("Actual = assigned address: " + address + "\n\n");
    }

    void alloc_dealloc_6() {
        //Test 6 allocation
        output += "Requesting 6,  Expected values: \n" +
                " 9 510 size buffers, 1 254 size buffer, 1 126 size buffer, \n" +
                " 1 62 size buffer, 1 30 size buffer, 1 14 size buffer and 1 6 size buffer, Status OK\n\n";
        int address = allocator.allocate(6);
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
    }

    void test_filling_memory() {
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
        int address = allocator.allocate(510);
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
    }

    void test_19_254_buffs() {
        //Reqeust 19 254 buffs
        int[] addresses = new int[19];
        output += "Request 19 254 size buffers" +
                "Expected values: \n" +
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
        int address = allocator.allocate(510);
        output += ("Actual = assigned address: " + address + "\n\n");
        //return all buffers
        output += "Return 19 254 buffers \n" +
                " Expected values:\n" +
                " 10 510 buffers, Status OK\n\n";
        for (int i = 18; i >= 0; i--) {
            allocator.deallocate(addresses[i]);
        }
        output += (allocator.getAllocationData().toString() + "\n");
        output += ("Status is:\n" + allocator.getStatus() + "\n\n");
    }

    void mixed_size_test() {
        //test multiple requests
        List<Integer> addressList = new ArrayList<>(17);
        output += "testing multiple requests: 5 size 6, 2 size 254, 2 size 126, 7 510 size \n" +
                " Expected values: \n" +
                " 1 510 size buffers,  0 254 size buffer, 1 126 size buffer,\n" +
                " 1 62 size buffer, 0 30 size buffer, 1 14 size buffer \tand 1 6 size buffer, Status tight \n\n";
        output += "Actual = Assigned Addresses for size 6:\n\n";
        for (int i = 0; i < 5; i++) {
            addressList.add(allocator.allocate(6));
            output += ("Actual = assigned address: " + addressList.get(addressList.size() - 1) + "\n\n");
        }
        output += "Actual = Assigned Addresses for size 126:\n\n";
        for (int i = 0; i < 2; i++) {
            addressList.add(allocator.allocate(126));
            output += ("Actual = assigned address: " + addressList.get(addressList.size() - 1) + "\n\n");
        }
        output += "Actual = Assigned Addresses for size 254:\n\n";
        for (int i = 0; i < 2; i++) {
            addressList.add(allocator.allocate(254));
            output += ("Actual = assigned address: " + addressList.get(addressList.size() - 1) + "\n\n");
        }
        output += "Actual = Assigned Addresses for size 510:\n\n";
        for (int i = 0; i < 7; i++) {
            addressList.add(allocator.allocate(510));
            output += ("Actual = assigned address: " + addressList.get(addressList.size() - 1) + "\n\n");
        }

        output += "Return 6 size buffers first, call debug and status between each return\n\n";
        for (int i = 0; i < 5; i++) {
            output += (allocator.getAllocationData().toString() + "\n");
            output += ("Status is:\n" + allocator.getStatus() + "\n\n");
            output += "Returning mem address:" + addressList.get(i) + ", a size 6 buffer \n" +
                    " DebugOutput: \n\n";
            allocator.deallocate(addressList.get(i));
        }

        for (int i = 7; i < 9; i++) {
            output += (allocator.getAllocationData().toString() + "\n");
            output += ("Status is:\n" + allocator.getStatus() + "\n\n");
            output += "Returning mem address:" + addressList.get(i) + ", a size 254 buffer \n" +
                    " DebugOutput: \n\n";
            allocator.deallocate(addressList.get(i));
        }

        for (int i = 5; i < 7; i++) {
            output += (allocator.getAllocationData().toString() + "\n");
            output += ("Status is:\n" + allocator.getStatus() + "\n\n");
            output += "Returning mem address:" + addressList.get(i) + ", a size 126 buffer \n" +
                    " DebugOutput: \n\n";
            allocator.deallocate(addressList.get(i));
        }

        output += "there should now only be 7 512 size buffers remaining with no other allocations";
    }


    void test1() {
        /*
        while(!addressList.isEmpty()){
            int randomAdrIdx = (int) (Math.random() * addressList.size());
            int adr = addressList.get(randomAdrIdx);
            addressList.remove(adr);
            allocator.deallocate(adr);
        }
        */
    }

    public void run() {
        //Setup
        output = "Ethan Honzik, 5.20.2024, Assignment 8\n\n";
        output += "Initializing buffers \n" +
                " Expected values: 10 512 size buffers, Status Ok\n\n";
        tooLargeTest();
        alloc_dealloc_6();
        test_filling_memory();
        test_19_254_buffs();
        mixed_size_test();
        System.out.println(output);
    }

    public static void main(String[] args) {
        new Main().run();
    }
}