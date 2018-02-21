import java.io.*;
import java.util.*;

public class Assig1b {

/* Alex Drizos CS 1501 - Algorithm implementation
 * Project 1b - Anagrams, Dictionary, Multiway Tries, dlB
 */
static final int WORDMASK = 2;
static final int PREFIXMASK = 1;

public static void main(String [] args) throws IOException {

  // set up I/O
  //get input filename from command line arg
  String fileName = args[0];
  //create output writer and set filename to command line arg
  PrintWriter outputfile = new PrintWriter (args[1]);
  //determine whether to use MyDictionary or DLB implimentation of the DictInterface

  //create dictionary of legal words for comparison
  DictInterface dictionary = createDictionary(args[2]);

  //create array of test file data
  ArrayList <StringBuilder> testData = getTestData(fileName);

  //debugging
  //print the input from file
  for (StringBuilder s: testData){
    System.out.println(s);
  }

  //create initial time stamp for runtime
  long start = getStartTime();

  //print all permutations
  StringBuilder prefix = new StringBuilder();
  for (int i = 0; i < testData.size(); i++){
    System.out.printf("\nHere are the results for %s:\n", testData.get(i));
    System.out.println("1 word solutions:");
    for (String s: findAnagrams(testData.get(i), dictionary)){
      System.out.println(s);
    }
  }



  //print total runtime
  printRunTime(start);


}    // end of main

private static StringBuilder removeSpaces(StringBuilder sb){
  StringBuilder sbNew = new StringBuilder();
  for (int i = 0; i < sb.length(); i++){
    if (sb.charAt(i) != ' '){
      sbNew.append(sb.charAt(i));
    }
  }
  return sbNew;

}//end of removeSpaces

private static ArrayList<StringBuilder> removeDuplicates(ArrayList<StringBuilder> list) {


        ArrayList<StringBuilder> result = new ArrayList<>();


        HashSet<String> set = new HashSet<>();


        for (StringBuilder item : list) {


            if (!set.contains(item.toString())) {
                result.add(item);
                set.add(item.toString());
            }
        }
        return result;
    }

private static long getStartTime(){
  long _start = System.nanoTime();
  return _start;
} //end of getStartTime

private static void printRunTime(long start){
  double runtime = System.nanoTime() - start;
  System.out.println("\nRuntime: " + (double)runtime / 1000000000.0 + " seconds.");
} //end of getEndTime

private static DictInterface createDictionary(String flag) throws IOException {
  Scanner fileScan = new Scanner(new FileInputStream("dictionary.txt"));
  String st;
  //determine whether to impliment DLB or MyDictionary implimentation of Dictionary
  DictInterface D = null; //default
  if (flag.equals("orig")){
      D = new MyDictionary();
  }
  else if (flag.equals("dlb")){
    D = new DLB();
  }

  while (fileScan.hasNext())
  {
    st = fileScan.nextLine();
    D.add(st);
  }

  return D;
} //end of createDictionary

private static String getUserString() throws IOException {
  Scanner keyboard = new Scanner(System.in);
  System.out.print("Please enter file name for test data: ");
  String filename = keyboard.nextLine();
  return filename;
}

private static ArrayList<StringBuilder> getTestData(String _fileName) throws IOException {
  Scanner fileScan = new Scanner(new FileInputStream(_fileName));
  String str;
  ArrayList <StringBuilder> sbArrayList = new ArrayList<>();

  while (fileScan.hasNext()){
    str = fileScan.nextLine();
    StringBuilder sb = new StringBuilder(str);
    sbArrayList.add(sb);
  } //end of while loop
  return sbArrayList;
} //end of getTestData


//wrapper function for main recursive algorithm that handles calling it, sorting, and removing duplicates and spaces
 private static ArrayList <String> findAnagrams(StringBuilder inChars, DictInterface dictionary){
     StringBuilder _prefix = new StringBuilder(); //empty stringbuilder for recursive helper
     inChars = removeSpaces(inChars);
     ArrayList <StringBuilder> output = removeDuplicates(findAnagramsHelper(inChars, _prefix, dictionary));
     ArrayList <String> stringList = new ArrayList<>();
     for (StringBuilder sb : output){
       stringList.add(sb.toString());
     }
     Collections.sort(stringList); //alphabetize
     return stringList;


 } //end of findAnagrams

private static ArrayList<StringBuilder> findAnagramsHelper(StringBuilder inChars, StringBuilder prefix, DictInterface dictionary){
  ArrayList <StringBuilder> output = new ArrayList<>();
  //check for base case
  if (inChars.length() == 0){
    return output;
  }
  //master loop
  for (int i = 0; i < inChars.length(); i++){
      char tempChar = inChars.charAt(i);
      inChars.deleteCharAt(i);
      prefix.append(tempChar); //update prefix string for output pruning
      //PRUNING CHECKS

      //pruning: only add to output if its a word
      //use bitwise AND to avoid multiple searches
      if ((dictionary.searchPrefix(prefix) & WORDMASK) != 0 && inChars.length() == 0 ){
          StringBuilder prefixClone = new StringBuilder(prefix);
          output.add(prefixClone);
      }
      //if not a valid prefix
      if ((dictionary.searchPrefix(prefix) & PREFIXMASK) == 0){

      }
      else
      {
          ArrayList <StringBuilder> perm = findAnagramsHelper(inChars, prefix, dictionary);
          for (int p = 0; p < perm.size(); p++){
            //perm.get(p).insert(0, tempChar);
            output.add(perm.get(p));
          }
      }
      //end of pruning checks ^
      inChars.insert(i, tempChar);
      prefix.deleteCharAt(prefix.length()-1); //book keeping for prefix backtracking

  }//end of master for loop
  return output;
} //end of findAnagramsHelper


} //end of Assig1a class
