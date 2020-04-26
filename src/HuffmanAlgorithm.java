import java.io.File;
import java.util.Map;
import java.util.SortedMap;

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
        public boolean isLeaf(){
            return leftChild==null && rightChild==null;
        }
        public int compareTo(Node node){
            return this.frequency - node.frequency;
        }
    }
    Node root;
    SortedMap frequencyMap;
    public void encode(int[] priorityArray){
    }

    /** Creates a huffman binary tree based on frequencyMap
     * @author Gerard Colman - 18327576
     */
    public void createTree(){
        while(!frequencyMap.isEmpty()){

        }
    }
}
