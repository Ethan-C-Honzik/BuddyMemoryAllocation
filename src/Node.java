/**
 * Assignment8
 * Operating Systems
 * Code written by Ethan Honzik
 * 5/20/2024
 * Simulation of the buddy memory allocation algorithm
 */

import java.util.Map;

/**
 * Nodes of a binary search tree. Each root level 512 byte allocation will be one tree. leaf nodes are
 * nodes that are available to be allocated
 */
public class Node {
    private final int address;
    private final int size;
    private boolean allocated = false;

    Node left;
    Node right;

    Node(int address, int size) {
        assert size >= 8;
        this.address = address;
        this.size = size;
    }

    /**
     * @param requestedSize the requested amount of memory to allocate
     * @return the memory address where we allocated. -1 if an allocation wasn't possible
     */
    public int allocate(int requestedSize) {
        //if the current node is allocated we can't allocate anything else to it.
        //same if both the children are already allocated
        if (allocated || (left != null && left.allocated) & (right != null && right.allocated)) return -1;

        // if the requested is greater than a split but less than the current size allocate
        // additionally don't bother splitting if requested size is less than 8
        if(requestedSize <= size && (requestedSize > size / 2 || (size/2) < 8)){
            allocated = true;
            return address;
        }else{
            //if the children are null split this node
            if (left == null && right == null) split();
            assert left != null && right != null;
            int addr = left.allocate(requestedSize);
            if(addr < 0) addr = right.allocate(requestedSize);
            return addr;
        }
    }

    /**
     * This will split this node into two children
     */
    public void split() {
        if(allocated){
            throw new RuntimeException("cannot split allocated node");
        }
        left = new Node(address, size / 2);
        right = new Node(address + size / 2, size / 2);
    }

    public boolean deallocate(int address){
        //only leaf nodes can be allocated
        if(left == null && right == null && address == this.address){
            assert allocated;
            allocated = false;
            return true;
        }else{
            assert left != null & right != null;
            boolean success =  left.deallocate(address);
            if(!success) success = right.deallocate(address);
            return success;
        }
    }

    /**
     * if both the left and right nodes are unallocated then set them to null. This effectively merges them
     */
    public void merge() {
        //a node is either split or it isn't. There should never be a state where one child is present and the other
        //isn't
        if (left == null ^ right == null) {
            throw new RuntimeException("One node null other isn't");
        }
        //if we are allocated then no need to merge
        if (allocated) return;
        //this checks if there are potential children to merge
        if (left != null) {
            //if both the left and right children are present and unallocated then we can merge them
            if (!left.allocated && !right.allocated) {
                left = null;
                right = null;
            } else {
                //if one is allocated then recursively travel down the tree merging any mergeable nodes
                left.merge();
                right.merge();
            }
        }
    }

    public void traverseForData(Map<Integer, Integer> allocatedMap, Map<Integer, Integer> unallocatedMap){
        //if this is allocated log it in the map
        if(allocated){
            allocatedMap.putIfAbsent(size, 0);
            allocatedMap.put(size, allocatedMap.get(size) + 1);
        }else if(left == null && right == null){//if this is a leaf node log it in unallocatedMap
            unallocatedMap.putIfAbsent(size, 0);
            unallocatedMap.put(size, unallocatedMap.get(size) + 1);
        }else{
            assert left != null && right != null;
            left.traverseForData(allocatedMap, unallocatedMap);
            right.traverseForData(allocatedMap, unallocatedMap);
        }
    }
}
