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
  //When everything is all set up, this thrhows all the keys into the tree
  BTree tree = new BTree(Integer.parseInt(args[0]), args[1]+".btree.data."+args[2]+"."+args[0], Integer.parseInt(args[2]));
  Parse(args[1],Integer.parseInt(args[2]),tree);
  tree.writeTreeData();
}
private static void printUsage() {
        System.err.println("Usage: java GeneBankCreateBTree <degree> <gbk file> <sequence length> [<debuglevel>]");
        System.err.println("<degree>: degree of the BTree (0 for default)");
        System.err.println("<gbk file>: GeneBank file");
        System.err.println("<sequence length>: 1-31");
        System.err.println("[<debug level>]: 0/1 (no/yes)");
        System.exit(1);
    }
    private static void Parse(String fileName, int length, BTree destination) throws FileNotFoundException {
          //Open file in a scanner
          int total = 0;
          int lineNo = 1;
          File fi = new File(fileName);
          Scanner scan = new Scanner(fi);
          boolean toggle = false;
          Pattern p = Pattern.compile("(?i)(?=([actg]{"+length+"}))");
          while(scan.hasNextLine()) {
            lineNo++;
            String line = scan.nextLine().trim();
            if(!toggle) {
              if(line.equals("ORIGIN")) {
                toggle = true;
              }
          } else {
            if(line.equals("//")) {
              toggle = false;
              continue;
            }
            line = line.replaceAll("[\\s0-9]*","");
            Matcher m = p.matcher(line);

            while(m.find()) {
              total++;
            destination.insert(toLong(m.group(1)));
          }
          }

          }
          System.out.println(total+" total matches.");
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
