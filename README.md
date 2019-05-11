# CS321FinalProject
Boise State University CS321 class final project from 2019

# Team Members
* Ryan Cox
* Jake Halopoff
* Colton Hix

# Notes
* We __are__ using line wrapping when searching for sub sequences.
* As of 5/10/2019 10:50 AM there is a cache option, but no cache is implemented in either option.
* The dump file is in <frequency> <DNA string> format.
* There may be unused methods that were replaced further on with other ones.
* Tree metadata is stored at the beginning of the file in the following order:
  * Int (seqLength) - Sequench length of this BTree
  * Int (degree) - Degree of this BTree
  * Int (32 * degree - 3) - Size of a new node
  * Int (16) - Current offset

# Files
 Btree.java: An implementation of a BTree for gene bank files. 
 inner class (BTreeNode): A Node class used by the BTree.
 TreeObject.java: Object class used by BTree.java.
 GeneBankCreateBTree.java: Creates the BTree file for the genebank with all gene sequences.
 GeneBankSearch.java: Searches the BTree and prints the frequency of each gene sequence found, as specified in a query file.
 
# Compile and Run
 $ javac *.java
 
 Usage:
    GeneBankCreateBTree.java:
        $ java GeneBankCreateBTree <cache(not implemented, 0/1)> <degree, 0 uses optimal> <gbk file> <sequence length> [<cache size>] [<debug level>]
    GeneBankSearch.java:
		      $ java GeneBankSearch <cache(not implemented, 0/1)> <btree file> <query file> [<cache size>] [<debug level>]
 Example:
        $ java GeneBankCreateBTree 0 15 test1.gbk 4 1 (creates btree degree 15 from test1.gbk sequence size 4 with dump file)
