
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;

public class BTree {
    private int degree;
    private BTreeNode root;
    private int currentOffset;
    private int nodeSize;
    private int insertPoint;
    private int seqLength;
    private File binFile;
    private RandomAccessFile disk;

    /**
     * Constructor of the BTree
     *
     * @param degree    - degree of the BTree
     * @param file      - the file to be written to.
     * @param seqLength - the length of the DNA sequence
     */

    public BTree(int degree, String file, int seqLength) {
        this.degree = degree;
        this.seqLength = seqLength;
        nodeSize = (32 * degree - 3);
        currentOffset = 16;
        insertPoint = (currentOffset + nodeSize);
        BTreeNode temp = new BTreeNode();
        root = temp;
        root.setOffset(currentOffset);
        temp.setLeaf(true);
        temp.setNumKeys(0);

        try {
            binFile = new File(file);
            binFile.delete();
            binFile.createNewFile();
            disk = new RandomAccessFile(file, "rw");
        } catch (FileNotFoundException ee) {
            System.err.println("file is missing!");
            System.exit(-1);
        } catch (IOException ioe) {
            System.err.println("IO Exception occurred!");
            System.exit(-1);
        }

    }

    /**
     * Returns the root of the BTree
     *
     * @return
     */
    public BTreeNode getRoot() {
        return root;
    }

    /**
     * Returns the degree of the BTree.
     *
     * @return
     */
    public int getDegree() {
        return this.degree;
    }

    /**
     * Returns the value of the node associated with the specified key.
     *
     * @param key
     * @return
     */
    public long get(long key) {
        return key; // should return the value at the key location
    }

    /**
     * Inserts a value into a specified location.
     *
     * @param node
     * @param key
     */
    public void insert(long key) {
        BTreeNode start = root;
        int i = start.getNumKeys();
        if (i == (2 * degree) - 1) {
            TreeObject object = new TreeObject(key);
            while (i > 0 && object.compareTo(start.getKey(i - 1)) == 0) {
                i--;
            }
            if (i < start.getNumKeys()) {
            }
            if (i > 0 && object.compareTo(start.getKey(i - 1)) == 0) {
                start.getKey(i - 1).increaseFreq();
            } else {
                BTreeNode n = new BTreeNode();
                n.setOffset(start.getOffset());
                root = n;
                start.setOffset(insertPoint);
                start.setParent(n.getOffset());
                n.setLeaf(false);
                n.addChild(start.getOffset());
                splitChild(n, 0, start);
                BTreeInsertNotFull(n, key);
            }
        } else {
            BTreeInsertNotFull(start, key);
        }
    }

    /**
     * Inserts a value when the node is not full.
     *
     * @param start
     * @param key
     */
    public void BTreeInsertNotFull(BTreeNode start, long key) {
        int i = start.getNumKeys();

        TreeObject object = new TreeObject(key);
        if (start.isLeaf()) {
            if (start.getNumKeys() != 0) {
                while (i > 0 && object.compareTo(start.getKey(i - 1)) < 0) {
                    i--;
                }
            }
            if (i > 0 && object.compareTo(start.getKey(i - 1)) == 0) {
                start.getKey(i - 1).increaseFreq();
            } else {
                start.addKey2(object, i);
                start.setNumKeys(start.getNumKeys() + 1);
            }
            writeNode(start, start.getOffset());
        } else {
            while (i > 0 && object.compareTo(start.getKey(i - 1)) < 0) {
                i--;
            }
            if (i > 0 && object.compareTo(start.getKey(i - 1)) == 0) {
                start.getKey(i - 1).increaseFreq();
                writeNode(start, start.getOffset());
                return;
            }
            int off = start.getChild(i);
            BTreeNode c = readNode(off);
            if (c.getNumKeys() == (2 * degree) - 1) {
                int j = c.getNumKeys();
                while (j > 0 && object.compareTo(c.getKey(j - 1)) < 0) {
                    j--;
                }
                if (j > 0 && object.compareTo(c.getKey(j - 1)) == 0) {
                    c.getKey(j - 1).increaseFreq();
                    return;
                } else {
                    splitChild(start, i, c);
                    if (object.compareTo(start.getKey(i)) > 0) {
                        i++;
                    }
                }

            }
            off = start.getChild(i);
            BTreeNode child = readNode(off);
            BTreeInsertNotFull(child, key);
        }
    }

    /**
     * Splits the child node according to BTree logic.
     *
     * @param start
     * @param i
     * @param c
     */
    public void splitChild(BTreeNode start, int i, BTreeNode c) {
        BTreeNode d = new BTreeNode();
        d.setLeaf(c.isLeaf());
        d.setParent(c.getParent());
        for (int j = 0; j < degree - 1; j++) {
            d.addKey(c.removeKey(degree));
            d.setNumKeys(d.getNumKeys() + 1);
            c.setNumKeys(c.getNumKeys() - 1);
        }
        if (!c.isLeaf()) {
            for (int j = 0; j < degree; j++) {
                d.addChild(c.removeChild(degree));
            }
        }
        start.addKey2(c.removeKey(degree - 1), i);
        start.setNumKeys(start.getNumKeys() + 1);
        c.setNumKeys(c.getNumKeys() - 1);
        if (start == root && start.getNumKeys() == 1) {
            writeNode(c, insertPoint);
            insertPoint += nodeSize;
            d.setOffset(insertPoint);
            start.addChild2(d.getOffset(), i + 1);
            writeNode(d, insertPoint);
            writeNode(start, currentOffset);
            insertPoint += nodeSize;
        } else {
            writeNode(c, c.getOffset());
            d.setOffset(insertPoint);
            writeNode(d, insertPoint);
            start.addChild2(d.getOffset(), i + 1);
            writeNode(start, start.getOffset());
            insertPoint += nodeSize;
        }
    }

    /**
     * Searches for a specific TreeObject containing a specified key.
     *
     * @param start
     * @param key
     * @return
     */
    public TreeObject search(BTreeNode start, long key) {
        int i = 0;
        TreeObject obj = new TreeObject(key);
        while (i < start.getNumKeys() && (obj.compareTo(start.getKey(i)) > 0)) {
            i++;
        }
        if (i < start.getNumKeys() && obj.compareTo(start.getKey(i)) == 0) {
            return start.getKey(i);
        }
        if (start.isLeaf()) {
            return null;
        } else {
            int offset = start.getChild(i);
            BTreeNode y = readNode(offset);
            return search(y, key);
        }
    }

    /**
     * Converts a given long back into a DNA string.
     *
     * @param key
     * @return
     */
    public String convertToDNA(long key) {
        String binKey = Long.toBinaryString(key);
        String str = "";
        String ret = "";
        for (int i = 0; i < (seqLength * 2) - (binKey.length()); i++) {
            str += "0";
        }
        str += binKey;
        for (int i = 0; i <= str.length() - 2; i += 2) {
            if (str.substring(i, i + 2).equals("00")) {
                ret += "a";
            } else if (str.substring(i, i + 2).equals("01")) {
                ret += "c";
            } else if (str.substring(i, i + 2).equals("11")) {
                ret += "t";
            } else if (str.substring(i, i + 2).equals("10")) {
                ret += "g";
            }
        }
        return ret;
    }

    /**
     * Prints the keys using an in order traversal.
     *
     * @param node
     */
    public void inOrderPrint(BTreeNode node) {
        if (node.isLeaf() == true) {
            for (int i = 0; i < node.getNumKeys(); i++) {
                System.out.println(convertToDNA(node.getKey(i).getKey()) + ": " + node.getKey(i).getFreq());
            }
            return;
        }
        for (int i = 0; i < node.getNumKeys() + 1; ++i) {
            int offset = node.getChild(i);
            BTreeNode y = readNode(offset);
            inOrderPrint(y);
            if (i < node.getNumKeys())
                System.out.println(convertToDNA(node.getKey(i).getKey()) + ": " + node.getKey(i).getFreq());

        }

    }

    /**
     * Writes a node to the disk.
     *
     * @param nd
     * @param off
     */
    public void writeNode(BTreeNode nd, int off) {
        int i = 0;
        try {
            writeNodeData(nd, nd.getOffset());
            disk.writeInt(nd.getParent());
            for (i = 0; i < (2 * degree) - 1; i++) {
                if (i < nd.getNumKeys() + 1 && !nd.isLeaf()) {
                    disk.writeInt(nd.getChild(i));
                } else if (i >= nd.getNumKeys() + 1 || nd.isLeaf()) {
                    disk.writeInt(0);
                }
                if (i < nd.getNumKeys()) {
                    long data = nd.getKey(i).getKey();
                    disk.writeLong(data);
                    int frequency = nd.getKey(i).getFreq();
                    disk.writeInt(frequency);
                } else if (i >= nd.getNumKeys() || nd.isLeaf()) {
                    disk.writeLong(0);
                }

            }
            if (i == nd.getNumKeys() && !nd.isLeaf()) {
                disk.writeInt(nd.getChild(i));
            }
        } catch (IOException ioe) {

        }
    }

    /**
     * Writes tree data to the disk.
     */
    public void writeTreeData() {
        try {
            disk.seek(0);
            disk.writeInt(seqLength);
            disk.writeInt(degree);
            disk.writeInt(32 * degree - 3);
            disk.writeInt(16);
        } catch (IOException ioe) {
            System.err.println("IO Exception occurred!");
            System.exit(-1);
        }
    }

    /**
     * Writes node data to the disk.
     *
     * @param start
     * @param off
     */
    public void writeNodeData(BTreeNode start, int off) {
        try {
            disk.seek(off);
            disk.writeBoolean(start.isLeaf());
            disk.writeInt(start.getNumKeys());
        } catch (IOException ioe) {
            System.err.println("IOException!");
            System.exit(-1);
        }
    }

    /**
     * Reads a node for the data it holds.
     *
     * @param off
     * @return
     */
    public BTreeNode readNode(int off) {
        BTreeNode n = null;

        n = new BTreeNode();
        TreeObject object = null;
        n.setOffset(off);
        int k = 0;
        try {
            disk.seek(off);
            boolean isLeaf = disk.readBoolean();
            n.setLeaf(isLeaf);
            int temp = disk.readInt();
            n.setNumKeys(temp);
            int parent = disk.readInt();
            n.setParent(parent);
            for (k = 0; k < (2 * degree) - 1; k++) {
                if (k < n.getNumKeys() + 1 && !n.isLeaf()) {
                    int child = disk.readInt();
                    n.addChild(child);
                } else if (k >= n.getNumKeys() + 1 || n.isLeaf()) {
                    disk.seek(disk.getFilePointer() + 4);
                }
                if (k < n.getNumKeys()) {
                    long value = disk.readLong();
                    int frequency = disk.readInt();
                    object = new TreeObject(value, frequency);
                    n.addKey(object);
                }
            }
            if (k == n.getNumKeys() && !n.isLeaf()) {
                int child = disk.readInt();
                n.addChild(child);
            }
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
            System.exit(-1);
        }

        return n;
    }

}

class BTreeNode implements Comparable<BTreeNode> {

    LinkedList<TreeObject> keys;
    LinkedList<Integer> child;
    private int parent;
    private int offset;
    private boolean isLeaf;
    public int numKeys;

    /**
     * Constructor of the BTreeNode
     *
     * @param height    - height of the node being created
     * @param isRoot    - is the node a root node?
     * @param isLeaf    - or is it a leaf?
     * @param nodeCount - number of nodes in the tree
     */
    public BTreeNode() {
        keys = new LinkedList<TreeObject>();
        child = new LinkedList<Integer>();

        numKeys = 0;
        parent = -1;

    }

    /**
     * Returns the key give my param from the BTree
     *
     * @param key
     * @return
     */
    public TreeObject getKey(int key) {
        TreeObject object = keys.get(key);
        return object;
    }

    /**
     * Adds a key using our TreeObject
     *
     * @param object
     */
    public void addKey(TreeObject object) {
        keys.add(object);
    }

    /**
     * Adds a key using our TreeObject and a integer
     *
     * @param object
     * @param key
     */
    public void addKey2(TreeObject object, int key) {
        keys.add(key, object);
    }

    /**
     * removes a key from out TreeObject using a key
     *
     * @param key
     * @return
     */
    public TreeObject removeKey(int key) {
        return keys.remove(key);
    }

    /**
     * returns the amount of keys in the BTree
     *
     * @return
     */
    public LinkedList<TreeObject> getKeys() {
        return keys;
    }

    /**
     * Returns the child specified in the BTree
     *
     * @param key
     * @return
     */
    public int getChild(int key) {
        return child.get(key);
    }

    /**
     * Adds a child into our BTree
     *
     * @param key
     */
    public void addChild(int key) {
        child.add(key);
    }

    /**
     * Adds a child into our BTree using two parameters
     *
     * @param c
     * @param key
     */
    public void addChild2(Integer c, int key) {
        child.add(key, c);
    }

    /**
     * returns the removed child from the BTree
     *
     * @param key
     * @return
     */
    public int removeChild(int key) {
        return child.remove(key);
    }

    /**
     * Returns the amount of childs in the BTree
     *
     * @return
     */
    public LinkedList<Integer> getChild() {
        return child;
    }

    /**
     * Sets the offset of the given node.
     *
     * @param index - the index to set the node to.
     */
    public void setOffset(int index) {
        offset = index;
    }

    /**
     * Returns if the node is a leaf or not.
     *
     * @return
     */
    public boolean isLeaf() {
        return isLeaf;
    }

    /**
     * Returns the offset of the BTree.
     *
     * @return
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Returns the number of key value for the BTree
     *
     * @return
     */
    public int getNumKeys() {
        return numKeys;
    }

    /**
     * Sets the number of keys in the BTree
     *
     * @param numKeys
     */
    public void setNumKeys(int numKeys) {
        this.numKeys = numKeys;
    }

    /**
     * Sets the given node as a leaf node.
     *
     * @param isLeaf
     */
    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    /**
     * Sets the amount of parents a node has.
     *
     * @param parent - number of parents
     */
    public void setParent(int parent) {
        this.parent = parent;
    }

    /**
     * Returns the amount of parents a node has.
     *
     * @return
     */
    public int getParent() {
        return parent;
    }

    @Override
    public int compareTo(BTreeNode o) {
        return offset - o.offset;
    }

}
