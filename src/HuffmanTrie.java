public class HuffmanTrie {
    private class Node{
        String data = "";
        int frequency;
        boolean isEndOfWord;
        Node parent;
        Node leftChild;
        Node rightChild;
        Node(Node leftChild, Node rightChild, Node parent, String character, int frequency){
            isEndOfWord = false;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
            this.parent = parent;
            data = character;
            this.frequency = frequency;
        }
    }
    Node root = new Node(null,null,null, "", 0);
}
