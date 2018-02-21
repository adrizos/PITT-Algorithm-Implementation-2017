

import java.util.*;

public class RandomDemo {
   public static void main( String args[] ){
   // create random object
   Random randomno = new Random();

   // create byte array
   byte[] nbyte = new byte[30];

   // put the next byte in the array
   randomno.nextBytes(nbyte);

   // check the value of array
   for (int i = 0; i < nbyte.length; i++){
       System.out.println(nbyte[i]);
   }
   //String newstr = new String (nbyte);
   //System.out.println("Value of byte array: " + newstr);
   }
}
