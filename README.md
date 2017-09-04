# TextCompressor
Compresses simple text file using move-to-front heuristic.
The TextCompressor.java program will prompt the user to enter a file name.  It then reads the file, determines whether the
file needs to be compressed or decompressed.  If the text begins with "0" then it will be decompressed.  Else it will be 
compressed.  

Compresson and decompression will use a FrequencyStack to build a library of words that are used.   Using an MTF algorithm,
more common words will be stored at the front of the stack and less frequent words will be moved to the back of the stack.

The original file is overwritten with the compressed/decompressed result.  If the file is being compressed, the program will
output to the terminal the original file size and the size of the compressed data in bytes.

Sample files have been included in the files folder:
files/small.txt
files/medium.txt
files/large.txt
files/input1.txt
files/input2.txt
files/input3.txt
files/input4.txt
files/input5.txt
files/input6.txt
files/input7.txt
files/input8.txt
files/input9.txt
files/input10.txt
