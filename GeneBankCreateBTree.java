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
  Parse(args[1],Integer.parseInt(args[2]));
}
private static void printUsage() {
        System.err.println("Usage: java GeneBankCreateBTree <degree> <gbk file> <sequence length> [<debuglevel>]");
        System.err.println("<degree>: degree of the BTree (0 for default)");
        System.err.println("<gbk file>: GeneBank file");
        System.err.println("<sequence length>: 1-31");
        System.err.println("[<debug level>]: 0/1 (no/yes)");
        System.exit(1);
    }
    private static String[] Parse(String fileName, int length) throws FileNotFoundException {
          //Open file in a scanner
          File fi = new File(fileName);
          Scanner scan = new Scanner(fi);
          //Instead of going through until I find ORIGIN, set the delimiter to a regex that splits by trash
          scan.useDelimiter("(?s)(?:(?:\\/\\/|\\A).*?ORIGIN|\\/\\/. *(?!ORIGIN))");
          String buff = "";
          //Filter out any whitespace and numbers, then put it all in a buffer
          while(scan.hasNext()) {
            buff += scan.next().replaceAll("[\\s0-9]*","");
          }
          //Instead of looping through I created a regular expression that finds all possible
          //overlapped combinations of X number of actg characters. This is a regular expression
          //so anything that is not actg is automatically glanced over
          Pattern p = Pattern.compile("(?=([actg]{"+length+"}))");
          Matcher m = p.matcher(buff);
          // //Thing I found on stack overflow to get the total number of matches so I can create an array.
          // //Not sure if I will need an array later, but I have it now
          int total = m.replaceAll("\0").split("\0", -1).length - 1;
          System.out.println(total+" total matches found. Now converting to array");
          // //The total thing messed with m, so I reset it
          m = p.matcher(buff);
          String[] subs = new String[total];
          int i=0;
          //Continue to find matches and shove them into the array
          while(m.find()) {
            subs[i] = (m.group(1));
            i++;
          }
          System.out.println("Done");
          return subs;
    }
}
