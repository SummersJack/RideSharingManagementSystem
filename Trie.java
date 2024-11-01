import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children;
    boolean isEndOfWord;

    public TrieNode() {
        children = new HashMap<>();
        isEndOfWord = false;
    }
}

public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    /**
     * Inserts a word into the Trie.
     *
     * @param word The word to insert.
     */
    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
        }
        node.isEndOfWord = true;
    }

    /**
     * Checks if the word exists in the Trie.
     *
     * @param word The word to search for.
     * @return True if the word exists, false otherwise.
     */
    public boolean search(String word) {
        return searchHelper(root, word, 0);
    }

    private boolean searchHelper(TrieNode node, String word, int index) {
        if (index == word.length()) {
            return node.isEndOfWord; // return true if it's end of a valid word
        }
        
        char c = word.charAt(index);
        TrieNode childNode = node.children.get(c);
        return childNode != null && searchHelper(childNode, word, index + 1);
    }

    /**
     * Deletes a word from the Trie.
     *
     * @param word The word to delete.
     * @return True if the word was deleted, false otherwise.
     */
    public boolean delete(String word) {
        return deleteHelper(root, word, 0);
    }

    private boolean deleteHelper(TrieNode node, String word, int index) {
        if (index == word.length()) {
            if (!node.isEndOfWord) {
                return false; // word doesn't exist
            }
            node.isEndOfWord = false; // unmark the end of the word
            return node.children.isEmpty(); // return true if node has no children
        }

        char c = word.charAt(index);
        TrieNode childNode = node.children.get(c);
        if (childNode == null) {
            return false; // word doesn't exist
        }

        boolean shouldDeleteChild = deleteHelper(childNode, word, index + 1);

        if (shouldDeleteChild) {
            node.children.remove(c); // delete the child node
            return node.children.isEmpty() && !node.isEndOfWord; // return true if node has no children and is not the end of another word
        }

        return false; // keep the child node
    }

    /**
     * Autocompletes a prefix and returns a list of matching words.
     *
     * @param prefix The prefix to autocomplete.
     * @return A list of words that match the prefix.
     */
    public List<String> autocomplete(String prefix) {
        List<String> results = new ArrayList<>();
        TrieNode node = root;
        
        for (char c : prefix.toCharArray()) {
            node = node.children.get(c);
            if (node == null) return results; // no words with this prefix
        }
        
        findWords(node, results, new StringBuilder(prefix));
        return results;
    }

    private void findWords(TrieNode node, List<String> results, StringBuilder sb) {
        if (node.isEndOfWord) results.add(sb.toString());
        
        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            sb.append(entry.getKey());
            findWords(entry.getValue(), results, sb);
            sb.deleteCharAt(sb.length() - 1); // backtrack
        }
    }
}
