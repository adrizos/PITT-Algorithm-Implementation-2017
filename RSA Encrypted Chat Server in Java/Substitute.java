import java.util.*;

public class Substitute implements SymCipher {
final int MAX = 256;
final int POSITIVECONVERTER = 128; //helper value to deal with potential negative indicies
byte[] permByteArray = new byte[MAX];   //holds a permutation of the 256 possible byte values
byte[] inversePermByteArray = new byte[MAX]; // holds the inverse of the permByteArray for decoding
public Substitute(byte[] byteArray) {
        permByteArray = byteArray;
        //create inverse of permByteArray
        for (int p = 0; p < MAX; p++){
            inversePermByteArray[permByteArray[p]+ POSITIVECONVERTER] = (byte)(p - POSITIVECONVERTER);
        }
}

public Substitute() {
        makeKey();
}

private byte [] makeKey(){
        //create new Random object
        Random rand = new Random();
        //fill array of 256 byte values to use as key (-128 to 127 randomly permuted)
        //fill array with required members
        for (int i = 0; i < 256; i++){
            permByteArray[i] = (byte)(i-128);
        }
        //permute elements in place
        int ptr = 0;
        while(ptr < MAX){
            byte tempValu;
            int swapped;
            swapped = rand.nextInt(MAX - ptr) + ptr; //choose an index after ptr and up to the MAX
            tempValu = permByteArray[ptr];
            permByteArray[ptr] = permByteArray[swapped];
            //fill inverse array for decoding
            inversePermByteArray[permByteArray[ptr]+ POSITIVECONVERTER] = (byte)(ptr - POSITIVECONVERTER);
            permByteArray[swapped] = tempValu;
            ptr++;
        }
        return permByteArray;
}
// Return an array of bytes that represent the key for the cipher
public byte[] getKey(){
    return permByteArray;

}

// Encode the string using the key and return the result as an array of
// bytes.  Note that you will need to convert the String to an array of bytes
// prior to encrypting it.  Also note that String S could have an arbitrary
// length, so your cipher may have to "wrap" when encrypting.
public byte [] encode(String s){
        //create byte array from the string to encode
        byte[] byteArray = s.getBytes();
        for (int i = 0; i < byteArray.length; i++){
            byteArray[i] = permByteArray[byteArray[i] + POSITIVECONVERTER];
        }
        return byteArray;
}

// Decrypt the array of bytes and generate and return the corresponding String.
public String decode(byte [] bytes){
        for (int i = 0; i < bytes.length; i++){
            bytes[i] = inversePermByteArray[bytes[i]+POSITIVECONVERTER];
        }
        String decodedStr = new String(bytes);
        return decodedStr;
}

}//end of class
