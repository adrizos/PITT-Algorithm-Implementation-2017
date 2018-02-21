import java.util.*;

public class Add128 implements SymCipher {

byte[] keyArray = new byte[128];

//constructors
public Add128() {
        makeKey(); // generate a random array of bytes for key
}
public Add128(byte[] byteArrayKey){
        keyArray = byteArrayKey; //set this keyarray to a passed key array from caller
}

// Return an array of bytes that represent the key for the cipher
private void makeKey(){
        //create new Random object
        Random rand = new Random();
        //create array of 128 random bytes to use as key
        rand.nextBytes(keyArray);
}
// Return an array of bytes that represent the key for the cipher
public byte[] getKey(){
    return keyArray;
}
// Encode the string using the key and return the result as an array of
// bytes.  Note that you will need to convert the String to an array of bytes
// prior to encrypting it.  Also note that String S could have an arbitrary
// length, so your cipher may have to "wrap" when encrypting.
public byte [] encode(String s){
        //create byte array from the string to encode
        byte[] stringBytes = s.getBytes();
        //encode the byte array version of the string, using the key array
        int p = 0;
        for (int i = 0; i < stringBytes.length; i++) {
                //if we've used all the elements of keyArray and there are still string elements
                //to encode, reset p and wrap to finish encoding arbitrary string length
                if (p == keyArray.length) {
                        p = 0;
                }
                stringBytes[i] += keyArray[p];
                p++;
        }
        return stringBytes; //returns encrypted array of string bytes
}

// Decrypt the array of bytes and generate and return the corresponding String.
public String decode(byte [] bytes){
        //decode the byte array
        int p = 0;
        for (int i = 0; i < bytes.length; i++) {
                //if we've used all the elements of keyArray and there are still string elements
                //to decode, reset p and wrap to finish decoding arbitrary string length
                if (p == keyArray.length) {
                        p = 0;
                }
                bytes[i] -= keyArray[p];
                p++;
        }
        //convert back to string and return
        String str = new String(bytes);
        return str;
}



} //end of class
