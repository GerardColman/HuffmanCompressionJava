/******************************************************************************
 *  Compilation:  javac Huffman.java
 *
 *  Compress or expand a binary input stream using the Huffman algorithm.
 *
 * Add instructions and documentation related to your Huffman algorithm here...
 *
 ******************************************************************************/


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author Gerard Colman - 18327576
 * @author Lukasz Filanowski - 18414616
 * COMP20290 Assignment 2, Huffman Compression
 * This was completed as a team
 */
public class Huffman {

    // alphabet size of extended ASCII
    private static final int R = 256;

    // Do not instantiate.
    private Huffman() { }

    // Huffman trie node
    private static class Node implements Comparable<Node> {
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch    = ch;
            this.freq  = freq;
            this.left  = left;
            this.right = right;
        }

        // is the node a leaf node?
        private boolean isLeaf() {
            assert ((left == null) && (right == null)) || ((left != null) && (right != null));
            return (left == null) && (right == null);
        }

        // compare, based on frequency
        public int compareTo(Node that) {
            return this.freq - that.freq;
        }
    }
    public int[] createFrequencyTable(File file) {

        try {
            Scanner in = new Scanner(file);
            String inputWord = "";
            int[] frequencyArray = new int[R];

            while (in.hasNext()) {
                inputWord = in.nextLine();
                char[] inputArray = inputWord.toCharArray();
                // \\s+
                for (int i = 0; i < inputArray.length; i++) {
                    int index = (int) inputArray[i];
                    frequencyArray[index]++;
                }
            }
            in.close();
            return frequencyArray;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    } //DONE
    /**
     * Reads a sequence of 8-bit bytes from standard input; compresses them
     * using Huffman codes with an 8-bit alphabet; and writes the results
     * to standard output.
     */
    public void compress(String fileName) {
        try{
            File input = new File(fileName);
            Scanner scanner = new Scanner(input);
            char[] in = getInput(scanner).toCharArray();

            int[] frequency = createFrequencyTable(input);

            Node root = buildTrie(frequency);

            String[] codeTable = new String[R];
            String s = "";
            buildCode(codeTable,root,s);

            writeTrie(root);

            System.out.println("Uncompressed Size: " + countBytes(frequency)); //Printing uncompressed size
            BinaryStdOut.write(countBytes(frequency));

            for (int i = 0; i < in.length; i++) {
                String code = codeTable[in[i]]; //Gets code for letter
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
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public int countBytes(int[] freq){
        int total = 0;
        for(int i = 0;i<freq.length;i++){
            total += freq[i];
        }
        return total;
    }
    public static String getInput(Scanner scan){
        StringBuilder output = new StringBuilder();
        while(scan.hasNext()){
            output.append(scan.nextLine());
        }
        return output.toString();
    }
    /**
     * Reads a sequence of bits that represents a Huffman-compressed message from
     * standard input; expands them; and writes the results to standard output.
     */
    public void decompress(String fileName) {
        try{
            File input = new File(fileName);
            Scanner scanner = new Scanner(input);
            char[] in = getInput(scanner).toCharArray();

            Node root = readTrie();

            int bytes = BinaryStdIn.readInt();

            for (int i = 0; i < bytes; i++) {
                Node iter = root;
                while (!iter.isLeaf()) {
                    boolean nextBit = scanner.nextBoolean();
                    if (nextBit) {
                        iter = iter.right;
                    } else {
                        iter = iter.left;
                    }
                }
                BinaryStdOut.write(iter.ch, 8);
            }
            BinaryStdOut.close();
            scanner.close();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    // build the Huffman trie given frequencies
    private Node buildTrie(int[] freq) {

        // initialze priority queue with singleton trees
        MinPQ<Node> pq = new MinPQ<Node>();
        for (char i = 0; i < R; i++)
            if (freq[i] > 0)
                pq.insert(new Node(i, freq[i], null, null));

        // special case in case there is only one character with a nonzero frequency
        if (pq.size() == 1) {
            if (freq['\0'] == 0) pq.insert(new Node('\0', 0, null, null));
            else                 pq.insert(new Node('\1', 0, null, null));
        }

        // merge two smallest trees
        while (pq.size() > 1) {
            Node left  = pq.delMin();
            Node right = pq.delMin();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.insert(parent);
        }
        return pq.delMin();
    }


    // write bitstring-encoded trie to standard output
    private static void writeTrie(Node x) {
        if (x.isLeaf()) {
            BinaryStdOut.write(true);
            BinaryStdOut.write(x.ch, 8);
            return;
        }
        BinaryStdOut.write(false);
        writeTrie(x.left);
        writeTrie(x.right);
    }

    // make a lookup table from symbols and their encodings
    private void buildCode(String[] st, Node x, String s) {
        if (!x.isLeaf()) {
            buildCode(st, x.left,  s + '0');
            buildCode(st, x.right, s + '1');
        }
        else {
            st[x.ch] = s;
        }
    }



    private static Node readTrie() {
        boolean isLeaf = BinaryStdIn.readBoolean();
        if (isLeaf) {
            return new Node(BinaryStdIn.readChar(), -1, null, null);
        }
        else {
            return new Node('\0', -1, readTrie(), readTrie());
        }
    }

    /**
     * Sample client that calls {@code compress()} if the command-line
     * argument is "compress" an {@code decompress()} if it is "decompress".
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        Huffman huffman = new Huffman();
        if(args[0].equals("compress")){
            huffman.compress(args[1]);
        }
        if(args[0].equals("decompress")){
            huffman.decompress(args[1]);
        }
    }

}

