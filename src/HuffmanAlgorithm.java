import javax.xml.crypto.Data;
import java.io.*;
import java.util.*;

/**
 * @author Gerard Colman - 18327576
 * @author Lukasz Filanowski - 18414616
 * COMP20290 Assignment 2, Huffman Compression
 * This was completed as a team
 */
public class HuffmanAlgorithm {
    /* TODO LIST
     * 1. Write Javadocs
     * 2. Check this actually works
     * 3. Do question 3 - Lukas
     */
    public static class Node {
        private char data;
        private int frequency;
        private Node leftChild, rightChild;

        public Node(char data, int frequency, Node leftChild, Node rightChild) {
            this.data = data;
            this.frequency = frequency;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }

        public Node() {
            leftChild = null;
            rightChild = null;
        }

        public boolean isLeaf() {
            return leftChild == null && rightChild == null;
        }

        public int compareTo(Node node) {
            return this.frequency - node.frequency;
        }
    }

    private SortedMap<Integer, Character> frequencyMap = new TreeMap<Integer, Character>();

    public HuffmanAlgorithm() {
    }

    public int[][] createFrequencyTable(File file) throws FileNotFoundException {

        Scanner in = new Scanner(file);
        String inputWord = "";

        try {
            int[][] frequencyArray = new int[27][1];

            while (in.hasNext()) {
                inputWord = in.nextLine();

                char[] inputArray = inputWord.toCharArray();
                // \\s+
                for (int i = 0; i < inputArray.length; i++) {
                    int index = 0;
                    if (inputArray[i] == 32) { //If input array contains a space
                        index = 26;
                    } else if (inputArray[i] >= 33 && inputArray[i] <= 64) { //Ignore punctuation symbols and numbers
                        i++;
                    } else if (inputArray[i] >= 65 && inputArray[i] <= 90) { //Handling capital letters
                        index = (inputArray[i] - 65);
                    } else if (inputArray[i] >= 123) { //Ignore misc symbols
                        i++;
                    } else { //Handling lowercase letters
                        index = (inputArray[i] - 97);
                    }
                    frequencyArray[index][0] += 1;
                }
            }
            in.close();
            return frequencyArray;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    } //DONE

    public String compressedInput(File file) throws FileNotFoundException {  //Input for compressed file
        String output = "";
        Scanner in = new Scanner(file);

        try {
            while (in.hasNext()) {

                output += in.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    } //DONE


    public static void main(String[] args) {
        HuffmanAlgorithm huffman = new HuffmanAlgorithm();
        String inputFileName = "input.txt"; //Change this to change input file
        huffman.encode(inputFileName);
    }

    public void encode(String inputFileName) { //Takes in name of output file
        try {
            File inputFile = new File(inputFileName);
            Scanner scanner = new Scanner(inputFile);
            /*Getting File input*/
            StringBuilder input = new StringBuilder();
            while (scanner.hasNext()) { //Gets entire file as string
                input.append(scanner.next());
            }
            char[] in = input.toString().toCharArray();
            /*Getting Frequency Table*/
            createFrequencyTable(inputFile);
            /*Creating Encoding Tree*/
            Node root = createTree();
            printTree(root);
            /*Creating code word table*/
            String[] codewordTable = new String[256];
            buildCodeTable(codewordTable, root, "");
            /*Writing tree to file*/
            writeTree(root);
            /*Encoding*/

            for (int i = 0; i < input.length(); i++) {
                String code = codewordTable[in[i]]; //Gets code for letter
                for (int j = 0; j < code.length(); j++) {
                    if (code.charAt(j) == '0') {
                        BinaryStdOut.write(false);
                    }
                    if (code.charAt(j) == '1') {
                        BinaryStdOut.write(true);
                    }
                }
            }
            scanner.close();
            BinaryStdOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    } //DONE

    public void decompress(String inputFileName) {
        try {
            File inputFile = new File(inputFileName);
            Scanner scanner = new Scanner(inputFile);
            Node root = readTreeFromFile(scanner);
            int n = scanner.nextInt();
            //Decoding
            for (int i = 0; i < n; i++) {
                Node iter = root;
                while (!iter.isLeaf()) {
                    boolean nextBit = scanner.nextBoolean();
                    if (nextBit) {
                        iter = iter.rightChild;
                    } else {
                        iter = iter.leftChild;
                    }
                }
                BinaryStdOut.write(iter.data, 8);
            }
            BinaryStdOut.close();
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    } //DONE

    private Node readTreeFromFile(Scanner scanner) {
        boolean leafMeAlone = scanner.nextBoolean();
        if (leafMeAlone) {
            return new Node(scanner.next().charAt(0), -1, null, null);
        } else {
            return new Node('\0', -1, readTreeFromFile(scanner), readTreeFromFile(scanner));
        }
    } //DONE

    /**
     * Creates a huffman binary tree based on frequencyMap
     *
     * @return root Returns root of the huffman encoding tree+
     * @author Gerard Colman - 18327576
     */
    public Node createTree() {
        SortedMap<Integer, Node> PQ = new TreeMap<Integer, Node>(); //Sorted map to store nodes
        while (!frequencyMap.isEmpty()) { //Putting characters from frequencyMap into a node and storing those nodes in another sorted map
            Integer key = frequencyMap.firstKey(); //Key = frequency, firstKey gets lowest key
            Character data = frequencyMap.get(key);
            Node node = new Node(data, key, null, null);
            PQ.put(key, node);
            frequencyMap.remove(key);
        }
        while (PQ.size() > 1) {
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
    } //ERROR

    private void buildCodeTable(String[] st, Node node, String n) {
        if (!node.isLeaf()) { //Base Case
            buildCodeTable(st, node.leftChild, n + "0"); //Recursive Call
            buildCodeTable(st, node.rightChild, n + "1"); //Recursive Call
        } else {
            st[node.data] = n;
        }
    } //ERROR

    private void writeTree(Node root) {
        //Find alternate method of writing bits
        if (root.isLeaf()) { //Base Case
            BinaryStdOut.write(true);
            BinaryStdOut.write(root.data, 8);
            return;
        }
        BinaryStdOut.write(false);
        writeTree(root.leftChild);
        writeTree(root.rightChild);
    } //DONE

    private void printTree(Node root) {
        if (root.isLeaf()) {
            return;
        }
        System.out.println(root.data);
        printTree(root.leftChild);
        printTree(root.rightChild);
    } //TEST FUNCTION
}