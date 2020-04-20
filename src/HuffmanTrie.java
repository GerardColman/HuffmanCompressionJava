public class HuffmanTrie {

    private class Node{
        String data = "";
        boolean isEndOfWord;
        Node parent;
        Node leftChild;
        Node rightChild;
        Node(Node leftChild, Node rightChild, Node parent){
            isEndOfWord = false;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
            this.parent = parent;
        }
    }
}
