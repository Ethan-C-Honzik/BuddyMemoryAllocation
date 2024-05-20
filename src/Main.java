/**
 * Assignment8
 * Operating Systems
 * Code written by Ethan Honzik
 * 5/20/2024
 * Simulation of the buddy memory allocation algorithm
 */
public class Main {

    void test1(){
        MemoryAllocator allocator = new MemoryAllocator(512, 10);
        for (int i = 0; i < 20; i++) {
            int address = allocator.allocate(256);
            System.out.println("address is " + address);
            System.out.println(allocator.getAllocationData().toString());
            System.out.println("Status is:\n" + allocator.getStatus() + "\n");
        }
        //allocator.deallocate(address);
        //System.out.println("address is " + address);
        //System.out.println(allocator.getAllocationData().toString());
    }

    public void run(){
        test1();
    }

    public static void main(String[] args) {
        new Main().run();
    }
}