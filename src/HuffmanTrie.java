public class HuffmanTrie {
    static final int ALPHABET_SIZE = 26;

    private class Node{
        String data = "";
        Node[] children = new Node[ALPHABET_SIZE];
        boolean isEndOfWord;
        Node(){
            isEndOfWord = false;
            for(int i = 0;i<ALPHABET_SIZE;i++){
                children[i] = null;
            }
        }
    }
}
