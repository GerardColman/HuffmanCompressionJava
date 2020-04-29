import HelperCode.BinaryStdIn;
import HelperCode.BinaryStdOut;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    private SortedMap<Integer,Character> frequencyMap = new TreeMap<Integer, Character>();
    public HuffmanAlgorithm(){ }

    public SortedMap<Integer, Character> createFrequencyTable(File file) throws FileNotFoundException {

        try {
            Scanner in = new Scanner(file);
            String inputWord = "";

            int[][] frequencyArray = new int[27][1];

            while(in.hasNext())
            {
                inputWord = in.next();

                char[] inputArray = inputWord.toCharArray();
                char[] spaceArray = in.next("\\s+").toCharArray();

                for(int i = 0; i < inputArray.length; i++){

                    int index = (inputArray[i] - 65);
                    frequencyArray[index][1] += 1;
                }

                frequencyArray[26][1] += spaceArray.length;
            }

            in.close();

            for(int j = 0; j < frequencyArray.length; j++)
            {
                char character = (char) frequencyArray[j][0];

                if(j == 26)
                {
                    character = ' ';
                }

                int frequency = frequencyArray[j][1];

                frequencyMap.put(frequency, character);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }

        return frequencyMap;
    }

    public String compressedInput(File file){  //Input for compressed file
        String output = "";

        return output;
    }

    //OUTPUT FUNCTION TAKE IN STRING RETURN NOTHING AND WRITE TO FILE

    public static void main(String[] args) {
        HuffmanAlgorithm Huffman = new HuffmanAlgorithm();
        //Huffman.encode(Huffman.createTree(), " ");
    }

    public void encode(){ //Takes in name of output file
        /*Getting File input*/
        String input = BinaryStdIn.readString();
        char[] in = input.toCharArray();
        /*Getting Frequency Table*/
        //Get frequencys - Lukas
        /*Creating Encoding Tree*/
        Node root = createTree();
        /*Creating code word table*/
        String[] codewordTable = new String[256];
        buildCodeTable(codewordTable,root,"");
        /*Writing tree to file*/
        writeTree(root);
        /*Encoding*/
        for(int i =0;i<input.length();i++){
            String code = codewordTable[in[i]]; //Gets code for letter
            for(int j = 0;j<code.length();j++){
                if(code.charAt(j) == '0'){
                    BinaryStdOut.write(false);
                }
                if(code.charAt(j) == '1'){
                    BinaryStdOut.write(true);
                }
            }
        }
        BinaryStdOut.close();
    }
    public void decompress(){
        Node root = readTreeFromFile();
        int n = BinaryStdIn.readInt();
        //Decoding
        for(int i = 0;i<n;i++){
            Node iter = root;
            while(!iter.isLeaf()){
                boolean nextBit = BinaryStdIn.readBoolean();
                if(nextBit){
                    iter = iter.rightChild;
                }else{
                    iter = iter.leftChild;
                }
            }
            BinaryStdOut.write(iter.data,8);
        }
        BinaryStdOut.close();
    }
    private Node readTreeFromFile(){
        boolean leafMeAlone = BinaryStdIn.readBoolean();
        if(leafMeAlone){
            return new Node(BinaryStdIn.readChar(),-1,null,null);
        }else{
            return new Node('\0',-1,readTreeFromFile(),readTreeFromFile());
        }
    }

    /** Creates a huffman binary tree based on frequencyMap
     * @author Gerard Colman - 18327576
     * @return root Returns root of the huffman encoding tree+
     */
    public Node createTree(){
        SortedMap<Integer, Node> PQ = new TreeMap<Integer, Node>(); //Sorted map to store nodes
        while(!frequencyMap.isEmpty()){ //Putting characters from frequencyMap into a node and storing those nodes in another sorted map
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
    private void buildCodeTable(String[] st, Node node, String n){
        if(!node.isLeaf()){
            buildCodeTable(st,node.leftChild, n+'0');
            buildCodeTable(st,node.rightChild, n+'1');
        }else{
            st[node.data] = n;
        }
    }
    private void writeTree(Node root){
        if(root.isLeaf()){ //Base Case
            BinaryStdOut.write(true);
            BinaryStdOut.write(root.data,8);
            return;
        }
        BinaryStdOut.write(false);
        writeTree(root.leftChild);
        writeTree(root.rightChild);
    }
}