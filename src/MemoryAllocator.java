import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MemoryAllocator {
    private final Node[] roots;
    private final int maxSize;
    private final int buffCount;
    //the flag that is set when there is two or less 512 buffers remaining
    private boolean lowMemory = false;

    public String getStatus(){
        if(lowMemory){
            return "Tight";
        }else{
            return "OK";
        }
    }

    public MemoryAllocator(int maxBuffSize, int buffCount) {
        maxSize = maxBuffSize;
        this.buffCount = buffCount;
        roots = new Node[buffCount];
        for (int i = 0; i < buffCount; i++) {
            roots[i] = new Node(i * maxBuffSize, maxBuffSize);
        }
    }

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

    public void deallocate(int address) {
        //the root nodes are 512 words. This means if we divide by 512 we get the index of the root
        int idx = address / maxSize;
        roots[idx].deallocate(address);
        roots[idx].merge();
        //if there is less than two 512 sized buffers left set tight memory flag to true
        lowMemory = getAllocationData().unallocatedBufferMap.getOrDefault(maxSize, buffCount) <= 2;
    }

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

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Free Buffer Count:\n");
            for (Object entry : unallocatedBufferMap.entrySet().toArray()) {
                String entryStr = entry.toString();
                sb.append(entryStr.split("=")[1]);
                sb.append(" ");
                sb.append(entryStr.split("=")[0]);
                sb.append(" Size Buffers\n");
            }
            return sb.toString();
        }
    }

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
