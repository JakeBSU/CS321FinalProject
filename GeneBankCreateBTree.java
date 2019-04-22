public class GeneBankCreateBTree {
public static void main(String[] args) throws IOException {
}
private static void printUsage() {
        System.err.println("Usage: java GeneBankCreateBTree <degree> <gbk file> <sequence length> [<debuglevel>]");
        System.err.println("<degree>: degree of the BTree (0 for default)");
        System.err.println("<gbk file>: GeneBank file");
        System.err.println("<sequence length>: 1-31");
        System.err.println("[<debug level>]: 0/1 (no/yes)");
        System.exit(1);
    }
}
