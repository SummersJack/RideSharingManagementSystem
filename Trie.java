// src/Trie.java
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

    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
        }
        node.isEndOfWord = true;
    }

    public List<String> autocomplete(String prefix) {
        List<String> results = new ArrayList<>();
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            node = node.children.get(c);
            if (node == null) return results;
        }
        findWords(node, results, new StringBuilder(prefix));
        return results;
    }

    private void findWords(TrieNode node, List<String> results, StringBuilder sb) {
        if (node.isEndOfWord) results.add(sb.toString());
        for (char c : node.children.keySet()) {
            findWords(node.children.get(c), results, sb.append(c));
            sb.deleteCharAt(sb.length() - 1);
        }
    }
}
