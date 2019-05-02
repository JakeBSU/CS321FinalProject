import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.IOException;

public class GeneBankCreateBTree {
public static void main(String[] args) throws IOException {
  if(args.length != 4 || Integer.parseInt(args[2]) < 1 || Integer.parseInt(args[2]) > 31) {
    printUsage();
    return;
  }
  Long[] keys = Parse(args[1],Integer.parseInt(args[2]));
  //When everything is all set up, this thrhows all the keys into the tree
  BTree tree = new BTree(Integer.parseInt(args[0]), args[1]+".btree.data."+args[2]+"."+args[0], Integer.parseInt(args[2]));
  for(Long key : keys) {
    tree.insert(key);
  }
}
private static void printUsage() {
        System.err.println("Usage: java GeneBankCreateBTree <degree> <gbk file> <sequence length> [<debuglevel>]");
        System.err.println("<degree>: degree of the BTree (0 for default)");
        System.err.println("<gbk file>: GeneBank file");
        System.err.println("<sequence length>: 1-31");
        System.err.println("[<debug level>]: 0/1 (no/yes)");
        System.exit(1);
    }
    private static Long[] Parse(String fileName, int length) throws FileNotFoundException {
          //Open file in a scanner
          File fi = new File(fileName);
          Scanner scan = new Scanner(fi);
          //Instead of going through until I find ORIGIN, set the delimiter to a regex that splits by trash
          scan.useDelimiter("(?s)(?:(?:\\/\\/|\\A).*?ORIGIN|\\/\\/.*(?!ORIGIN))");
          String buff = "";
          //Filter out any whitespace and numbers, then put it all in a buffer
          while(scan.hasNext()) {
            buff += scan.next().replaceAll("[\\s0-9]*","");
          }
          //Instead of looping through I created a regular expression that finds all possible
          //overlapped combinations of X number of actg characters. This is a regular expression
          //so anything that is not actg is automatically glanced over
          Pattern p = Pattern.compile("(?i)(?=([actg]{"+length+"}))");
          Matcher m = p.matcher(buff);
          // //Thing I found on stack overflow to get the total number of matches so I can create an array.
          // //Not sure if I will need an array later, but I have it now
          int total = m.replaceAll("\0").split("\0", -1).length - 1;
          System.out.println(total+" total matches found. Now converting to array");
          // //The total thing messed with m, so I reset it
          m = p.matcher(buff);
          Long[] subs = new Long[total];
          int i=0;
          //Continue to find matches and shove them into the array
          while(m.find()) {
            subs[i] = toLong(m.group(1));
            if(i==0)System.out.println("Code: "+m.group(1)+" long: "+Long.toBinaryString(subs[0]));
            i++;
          }
          System.out.println("Done");

          return subs;
    }
    private static Long toLong(String code) {
      String s = code.toLowerCase();
      s = s.replaceAll("a","00");
      s = s.replaceAll("t","11");
      s = s.replaceAll("c","01");
      s = s.replaceAll("g","10");
      Long m = 1l;
      m = m<<63; //One with 63 zeroes after it, so we can always have 64 bitts
      return (Long.parseLong(s,2) | m); //Mask it so if we ever wanted to see the full binary value we can
    }
}
