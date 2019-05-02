import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

class BTree {
    private int degree;
    private BTreeNode root;
    private int currentOffset;
    private int nodeSize;
    private int insertPoint;
    private int maxChild, maxKeys;
    private static int seqLength;
    public final int STARTING_ADDRESS = 32;

    private File binFile;
    private RandomAccessFile disk;

    public BTree(int degree, String file, int seqSize) {
        this.degree = degree;
        maxChild = 2 * degree;
        maxKeys = (2 * degree) - 1;
        seqLength = seqSize;

        nodeSize = (32 * degree - 3);
        currentOffset = 12;
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
        }
        catch (FileNotFoundException e){
            System.err.println("File not found exception occurred.");
            System.exit(-1);
        }
        catch (IOException ioe){
            System.err.println("IO Exception occurred.");
            System.exit(-1);
        }
    }
    public BTree(int degree, File file) {
        try {
        } catch (FileNotFoundException e){
            System.err.println("File not found exception occurred.");
        }
    }
    public BTreeNode getRoot(){
        return root;
    }

    public int getDegree() {
        return this.degree;
    }

    public int getNumNodes() {
        return numNodes;
    }

    public int getSeqLength(){
        return seqLength;
    }

    public int getHeight(){
        return height;
    }

    public long getKey(long key){
        return key; //Returns value at the key location.
    }

    public void insert(long key) {
        BTreeNode start = root;
        int i = start.getNumKeys();
        if (i == maxKeys) {
            TreeObject object = new TreeObject (key);
            while (i > 0 && object.compareTo(start.getKey(i-1)) == 0) {
                i--;
            }
            if (i < start.getNumKeys()) {

            }
            if (i > 0 && object.compareTo(start.getKey(i-1)) == 0) {
                start.getKey(i-1).increaseFreq();
            } else {
                //BTreeNode n = new BTreeNode();
               // n.setOffset(start.getOffset());
               // root = n;
              //  start.setOffset(insertionPoint);
              //  start.setParent(n.getOffset());
              //  n.setLeaf(false);
              //  n.addChild(start.getOffset());
             //   splitChild(n, 0, start);

            }
        }
    }
}

class BTreeNode {

}
