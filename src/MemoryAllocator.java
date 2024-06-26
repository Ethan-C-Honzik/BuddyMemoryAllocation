import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MemoryAllocator {
    //the actual memory that is allocated out to callers
    public final int[] memory;
    private final Node[] roots;
    //the max size a buffer can be, 512 in our case
    private final int maxSize;
    //the amount of max size buffers present
    private final int buffCount;
    //the flag that is set when there is two or less 512 buffers remaining
    private boolean lowMemory = false;

    public MemoryAllocator(int maxBuffSize, int buffCount) {
        memory = new int[maxBuffSize * buffCount];
        maxSize = maxBuffSize;
        this.buffCount = buffCount;
        roots = new Node[buffCount];
        for (int i = 0; i < buffCount; i++) {
            roots[i] = new Node(i * maxBuffSize, maxBuffSize);
        }
    }

    /**
     * Return tight if less than two max size buffers remaining
     * @return a string based on amount of remaining memory
     */
    public String getStatus(){
        if(lowMemory){
            return "Tight";
        }else{
            return "OK";
        }
    }

    /**
     * Gets a new memory address that can be allocated
     * @param size the size of the buffer we are requesting
     * @return a memory address (in our case and index into the memory array)
     */
    public int allocate(int size) {
        if (size > maxSize) return -2;
        for (Node root : roots) {
            int address = root.allocate(size);
            if (address > -1) {
                //if there is less than two 512 sized buffers left set tight memory flag to true
                lowMemory = getAllocationData().unallocatedBufferMap.getOrDefault(maxSize, buffCount) <= 2;
                return address;
            }
        }
        return -1;
    }

    /**
     * frees previously allocated memory so that it can be reallocated later
     * @param address the address to free
     */
    public void deallocate(int address) {
        //the root nodes are 512 words. This means if we divide by 512 we get the index of the root
        int idx = address / maxSize;
        roots[idx].deallocate(address);
        //this recursively merges buffers together into the max possible size
        roots[idx].merge();
        //if there is less than two 512 sized buffers left set tight memory flag to true
        lowMemory = getAllocationData().unallocatedBufferMap.getOrDefault(maxSize, buffCount) <= 2;
    }

    /**
     * A class that holds two maps that store data on the current state of the memory
     */
    public static class AllocationData {
        Map<Integer, Integer> allocatedBufferMap;
        Map<Integer, Integer> unallocatedBufferMap;

        public AllocationData(Map<Integer, Integer> allocatedBufferMap, Map<Integer, Integer> unallocatedBufferMap) {
            this.allocatedBufferMap = allocatedBufferMap;
            this.unallocatedBufferMap = unallocatedBufferMap;
            for (int i = 8; i <= 512; i *= 2) {
                this.allocatedBufferMap.putIfAbsent(i, 0);
                this.unallocatedBufferMap.putIfAbsent(i, 0);
            }
        }

        /**
         * Used for debugging to print out the current free buffers
         * @return a debug string
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Free Buffer Count:\n");
            //put into a tree map so that the results are in order
            Object[] resultArr = new TreeMap<>(unallocatedBufferMap).entrySet().toArray();
            for (int i = resultArr.length-1; i >=0; i--) {
                Object entry = resultArr[i];
                String entryStr = entry.toString();
                sb.append(entryStr.split("=")[1]);
                sb.append(" ");
                sb.append(entryStr.split("=")[0]);
                sb.append(" Size Buffers\n");
            }
            return sb.toString();
        }
    }

    /**
     * This returns an allocation data object that gives information on the current state of memory
     * @return an allocation data object
     */
    public AllocationData getAllocationData() {
        Map<Integer, Integer> allocatedBufferMap = new HashMap<Integer, Integer>();
        Map<Integer, Integer> unallocatedBufferMap = new HashMap<Integer, Integer>();
        for (Node node : roots) {
            node.traverseForData(allocatedBufferMap, unallocatedBufferMap);
        }
        /*
        System.out.println("allocated:");
        System.out.println(Arrays.toString(allocatedBufferMap.entrySet().toArray()));
        System.out.println("unallocated:");
        System.out.println(Arrays.toString(unallocatedBufferMap.entrySet().toArray()));
        */
        return new AllocationData(allocatedBufferMap, unallocatedBufferMap);
    }
}
