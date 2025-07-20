import java.util.*;
class DeleteDuplicateFolderInSystem {
    static class Node {
        String name;
        Map<String, Node> children = new HashMap<>();
        boolean toDelete = false;

        Node(String name) {
            this.name = name;
        }
    }

    // Map to track serialization -> count
    Map<String, Integer> serializationCount = new HashMap<>();
    // Map node -> its serialization
    Map<Node, String> nodeSerialization = new HashMap<>();

    public List<List<String>> deleteDuplicateFolder(List<List<String>> paths) {
        Node root = new Node("");

        // Step 1: Build the directory tree
        for (List<String> path : paths) {
            insert(root, path);
        }

        // Step 2: Serialize all subtrees and count them
        serialize(root);

        // Step 3: Mark nodes with duplicate structure
        markDuplicates(root);

        // Step 4: Collect remaining paths
        List<List<String>> result = new ArrayList<>();
        collectPaths(root, new ArrayList<>(), result);
        return result;
    }

    // Build tree
    private void insert(Node root, List<String> path) {
        Node curr = root;
        for (String folder : path) {
            curr.children.putIfAbsent(folder, new Node(folder));
            curr = curr.children.get(folder);
        }
    }

    // Post-order serialization
    private String serialize(Node node) {
        if (node.children.isEmpty()) {
            nodeSerialization.put(node, "");
            return "";
        }

        List<String> childSerials = new ArrayList<>();
        for (Node child : node.children.values()) {
            String childSer = serialize(child);
            childSerials.add("(" + childSer + ")" + child.name);
        }

        Collections.sort(childSerials); // ensure order doesn't affect comparison
        String serial = String.join("", childSerials);
        nodeSerialization.put(node, serial);
        serializationCount.put(serial, serializationCount.getOrDefault(serial, 0) + 1);
        return serial;
    }

    // Mark all duplicate folders
    private void markDuplicates(Node node) {
        if (node == null) return;
        String serial = nodeSerialization.get(node);
        if (!serial.isEmpty() && serializationCount.get(serial) > 1) {
            node.toDelete = true;
        }

        for (Node child : node.children.values()) {
            markDuplicates(child);
        }
    }

    // Collect valid paths (those not marked for deletion)
    private void collectPaths(Node node, List<String> currentPath, List<List<String>> result) {
        for (Node child : node.children.values()) {
            if (!child.toDelete) {
                currentPath.add(child.name);
                result.add(new ArrayList<>(currentPath));
                collectPaths(child, currentPath, result);
                currentPath.remove(currentPath.size() - 1);
            }
        }
    }
}
