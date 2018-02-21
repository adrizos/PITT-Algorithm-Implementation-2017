/*************************************************************************
*  Compilation:  javac LZW.java
*  Execution:    java LZW - < input.txt   (compress)
*  Execution:    java LZW + < input.txt   (expand)
*  Dependencies: BinaryIn.java BinaryOut.java
*
*  Compress or expand binary input from standard input using LZW.
*
*
*************************************************************************/

public class LZWmod {
private static final int R = 256;            // number of input chars
private static final int MAXCODES = 65536; //max number of code words at bit length 16
public static void compress() {
        int l = 512; //codeword bit length threshold initialized to 2^W
        int W = 9;  // initial codeword width 9 bits
        DLB <Integer> dictionary = new DLB<>();
        //initalize dictionary with ascii table
        for (int i = 0; i < R; i++) {
                dictionary.add(new StringBuilder("" + (char) i), i);
        }
        int code = R + 1; //r is the codeword for the end of file

        //book keeping variables
        boolean largestPreFound;;
        StringBuilder prefix = new StringBuilder();
        char saveChar;
        boolean increaseBits = false; //pre flag for increasing codeword bit length
        //loop until no bytes left to read
        while (true) {
                largestPreFound = false;
                while (!largestPreFound) {
                        //try to read as many bytes as possible until largest prefix is read
                        try {
                                prefix.append(BinaryStdIn.readChar());
                        }
                        catch (RuntimeException e) {
                            //add final codeword output
                            BinaryStdOut.write(dictionary.search(prefix).intValue(),W); //write the codeword to compressed file
                            //file clean up
                            BinaryStdOut.write(R, W);
                            BinaryStdOut.close();
                            return;
                        }
                        //check if updated prefix is in the dictionary
                        if (dictionary.search(prefix) != null) {
                                largestPreFound = false;

                        }
                        else {
                                largestPreFound = true; // return to master loop
                        }
                } // end of inner loop
                //largest prefix has been found, write output and update dictionary
                saveChar = prefix.charAt(prefix.length()-1); // save last bit tried that caused the prefix to not be found
                //add new prefix to dictionary
                if (code < MAXCODES) {
                    //check whether codeword bit length will need increased
                    if (code == l){
                        increaseBits = true;
                    }
                    dictionary.add(prefix, code++);

                }
                //codeword output
                prefix.deleteCharAt(prefix.length()-1); //remove the last byte we tried to add
                BinaryStdOut.write(dictionary.search(prefix).intValue(), W); //write the codeword to compressed file
                //check for codeword bit length increase flag
                if (increaseBits){
                    W++; //increment codeword bit length
                    l*=2; //double threshold
                    increaseBits = false; //reset
                }
                prefix = new StringBuilder("" + saveChar); //update prefix past the last char written to compressed file
                //start master loop again and look for next longest prefix
        } // end of master loop

}


public static void expand() {
        String[] st = new String[MAXCODES];
        int i; // next available codeword value
        int l = 512; //codeword bit length threshold initialized to 2^W
        int W = 9;  // initial codeword width 9 bits
        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
                st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(W);
        String val = st[codeword];

        while (true) {
                BinaryStdOut.write(val);
                if (i == l && l < MAXCODES){
                    l*=2;
                    W++;
                }
                codeword = BinaryStdIn.readInt(W);
                if (codeword == R) break;
                String s = st[codeword];
                if (i == codeword) s = val + val.charAt(0); // special case hack
                if (i < MAXCODES) st[i++] = val + s.charAt(0);
                val = s;
        }
        BinaryStdOut.close();
}



public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new RuntimeException("Illegal command line argument");
}

}
