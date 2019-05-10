import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;
import java.util.LinkedList;

public class GeneBankSearch {
  private static int seqLength;
  private static int degree;
  private static int nodeSize;
  private static int offset;
  private static BTreeNode root;
public static void main(String[] args) {
  if(args.length != 2 && args.length != 3) {
    printUsage();
    return;
  }
  try {
    File qFile = new File(args[1]);
    File treeFile = new File(args[0]);
    Scanner scan = new Scanner(qFile);

    //SubLength will be read from the btree file

    RandomAccessFile nodeReader = new RandomAccessFile(treeFile, "r");
    nodeReader.seek(0);
    seqLength = nodeReader.readInt();
    degree = nodeReader.readInt();
    nodeSize = nodeReader.readInt();
    offset = nodeReader.readInt();
    root = readNode(nodeReader, 16);
    //This pretty much just tokenizes the file
    scan.useDelimiter("(?m)(?=^[actg]+$)");
    while(scan.hasNext()) {
      String base = scan.next();
        base = base.replaceAll("\\s","");
        if(base.length() != seqLength) continue;
        //Since the sequence only contains one side, we really need to search for two.
        Long init = toLong(base);
        TreeObject n = search(nodeReader, root,init);
        int total = 0;
        if(n != null) total = n.getFreq();
        Long alt = init ^ ~(~0<<(2*seqLength));
        n = search(nodeReader, root,alt);
        if(n != null) total += n.getFreq();
        System.out.println(base+": "+total);
        //Now somehow the tree file needs to be searched for both of these keys.
        //The frequency should be the frequency of both of them added, since they imply eachother
    }
  } catch(FileNotFoundException e) {
    System.err.println("File not found");
  } catch(IOException e) {
    System.err.println("IO");
  }

}
private static void printUsage () {
            System.err.println("Usage: java GeneBankSearch <btree file> <query file> [<debug level>]\n");
            System.exit(1);
        }
private static Long toLong(String code) {
  String s = code.replaceAll("a","00");
  s = s.replaceAll("t","11");
  s = s.replaceAll("c","01");
  s = s.replaceAll("g","10");
  Long m = 1l;
  m = m<<63; //One with 63 zeroes after it, so we can always have 64 bits
  return (Long.parseLong(s,2) | m); //Mask it so if we ever wanted to see the full binary value we can
}
private static String toGene(Long code, int length) {
  String s = Long.toString(code,4);
  s = s.replaceAll("0","a");
  s = s.replaceAll("1","c");
  s = s.replaceAll("2","g");
  s = s.replaceAll("3","t");
  s = s.substring(32-length,32);
  // Long m = 1l;
  // m = m<<63; //One with 63 zeroes after it, so we can always have 64 bits
  // return (Long.parseLong(s,2) | m); //Mask it so if we ever wanted to see the full binary value we can
  return s;
}
public static BTreeNode readNode(RandomAccessFile disk, int off) {
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
public static TreeObject search(RandomAccessFile disk, BTreeNode start, long key) {
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
        BTreeNode y = readNode(disk, offset);
        return search(disk, y, key);
    }
}
}
