import java.io.File;
import java.util.*;

/**
 * @author Gerard Colman - 18327576
 * @author Lukasz Filanowski - LUKAS FILL IN STUDENT NUMBER!
 * COMP20290 Assignment 2, Huffman Compression
 * This was completed as a team
 */
public class HuffmanAlgorithm {
    public static class Node{
        private char data;
        private int frequency;
        private Node leftChild, rightChild;
        public Node(char data, int frequency, Node leftChild, Node rightChild){
            this.data = data;
            this.frequency = frequency;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }
        public Node(){
            leftChild = null;
            rightChild = null;
        }
        public boolean isLeaf(){
            return leftChild==null && rightChild==null;
        }
        public int compareTo(Node node){
            return this.frequency - node.frequency;
        }
    }
    Node root;
    private SortedMap<Integer,Character> frequencyMap = new TreeMap<Integer, Character>();
    public void encode(int[] priorityArray){
    }

    /** Creates a huffman binary tree based on frequencyMap
     * @author Gerard Colman - 18327576
     * @return root Returns root of the huffman encoding tree
     */
    public Node createTree(){
        SortedMap<Integer, Node> PQ = new TreeMap<Integer, Node>();
        /* REMINDERS
        * Gerard when frequency map is made remove casts
         */
        while(!frequencyMap.isEmpty()){ //Putting characters from frequencyMap into a node and storing those nodes in
            Integer key = frequencyMap.firstKey(); //Key = frequency, firstKey gets lowest key
            Character data = frequencyMap.get(key);
            Node node = new Node(data, key, null,null);
            PQ.put(key,node);
            frequencyMap.remove(key);
        }
        while(PQ.size() > 1){
            Node node1 = PQ.get(PQ.firstKey()); //Gets first lowest node
            PQ.remove(PQ.firstKey()); //Removes it from frequency Map
            Node node2 = PQ.get(PQ.firstKey()); //Gets second Lowest node
            PQ.remove(PQ.firstKey());
            Node parentNode = new Node();
            parentNode.leftChild = node1;
            parentNode.rightChild = node2;
            Integer combinedFreq = node1.frequency + node2.frequency; //Gets combined frequency
            parentNode.frequency = combinedFreq;
            PQ.put(combinedFreq, parentNode);
        }
        return PQ.get(PQ.lastKey()); //Returns root of tree
    }
}
