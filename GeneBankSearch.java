import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class GeneBankSearch {
public static void main(String[] args) {
  if(args.length != 2 && args.length != 3) {
    printUsage();
    return;
  }
  try {
    File qFile = new File(args[1]);
    Scanner scan = new Scanner(qFile);
    int subLength = 0;
    //This pretty much just tokenizes the file
    scan.useDelimiter("(?m)(?=^[actg]+$)");
    while(scan.hasNext()) {
      String base = scan.next();
      if(subLength == 0) subLength = base.length();
      if(base.length() == subLength) {
        //Since the sequence only contains one side, we really need to search for two.
        Long init = toLong(base);
        //This toggles the first 2 * length bits, since each letter is two bits
        Long alt = init ^ ~(~0<<(2*subLength));
        //Now somehow the tree file needs to be searched for both of these keys.
        //The frequency should be the frequency of both of them added, since they imply eachother
      }
    }
  } catch(FileNotFoundException e) {
    System.err.println("File not found");
  }

}
private static void printUsage () {
            System.err.println("Usage: java GeneBankSearch <btree file> <query file> [<debug level>]\n");
            System.exit(1);
        }
private static Long toLong(String code) {
  String s = code.replaceAll("\\s","");
  s = s.replaceAll("a","00");
  s = s.replaceAll("t","11");
  s = s.replaceAll("c","01");
  s = s.replaceAll("g","10");
  Long m = 1l;
  m = m<<63; //One with 63 zeroes after it, so we can always have 64 bitts
  return (Long.parseLong(s,2) | m); //Mask it so if we ever wanted to see the full binary value we can
}
}
