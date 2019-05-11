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
