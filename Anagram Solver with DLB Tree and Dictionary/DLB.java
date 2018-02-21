// Alex Drizos
// CS1501 - Assignment 1
//DLB tree class - impliments DictInterface class


//imports
import java.util.*;
import java.io.*;


public class DLB implements DictInterface{

  //variables
  private final char TERMINATOR = '$';
  private int numWords;
  private DlbNode root;

  //constructors
  public DLB()
  {

  }







  //functions as required by DictInterface
  public boolean add(String s){
      DlbNode currNode = root; //default

      for (int i = 0; i < s.length(); i++){
        if (i == 0){
            if (root == null){
                root = new DlbNode();
                root.data = s.charAt(i); //create a dlbNode with first letter
                currNode = root;
            }//no first letters have been added yet
            else {
                currNode = findOrInsertSibling(s.charAt(i),root);
            }//letters have been place by previous words
        } // first letter of s

        //not the first char in s
        else {
          currNode = findOrInsertChild(s.charAt(i), currNode);
        }

      }//end of master for

      //add terminating character
      findOrInsertChild(TERMINATOR, currNode);

      return true;
  }//end of add

  public int searchPrefix(StringBuilder s){
    /* Returns 0 if s is not a word or prefix within the DictInterface
  	 * Returns 1 if s is a prefix within the DictInterface but not a
  	 *         valid word
  	 * Returns 2 if s is a word within the DictInterface but not a
  	 *         prefix to other words
  	 * Returns 3 if s is both a word within the DictInterface and a
  	 *         prefix to other words
  	 */
     DlbNode currNode = root; //default

     for (int i = 0; i < s.length(); i++){
       if (i == 0){
           if (root == null){
               return 0;
           }//no first letters have been added yet

           else {
               currNode = traverseSiblings(s.charAt(i),root); //traverse siblings to find char match
               if (currNode == null){
                 return 0;
               }
           }
       } // first letter of s

       //not the first char in s
       else {
         currNode = traverseChildren(s.charAt(i), currNode);
         if (currNode == null){
           return 0;
         }
       }
     }//end of master for

     if (currNode.child == null){
       return 0; //unlikely but will check if
     }

     //is this a prefix
     boolean prefix = false;
     if (currNode.child.data != TERMINATOR){
       prefix = true;
     }
     else if (currNode.child.sibling != null){
       prefix = true;
     }

     //look for terminating character (is it a word)
     currNode = traverseSiblings(TERMINATOR, currNode.child);
     boolean word;
     if (currNode == null){
        word = false;
     }
     else {
       word = true;
     }
     //we now have enough info to determine return cases 1-3
     if (prefix && !word){
       return 1;
     }
     else if (!prefix && word){
       return 2;
     }
     else if (prefix && word){
       return 3;
     }

     return 0; //default

  }

  public int searchPrefix(StringBuilder s, int start, int end){
      return -1; //currently not using this
  }

  //helper functions
  private DlbNode traverseSiblings(char c, DlbNode node){
    DlbNode currNode;
    currNode = node;
    if (node.data == c){
      return node;
    }
    while (currNode.sibling != null){
      currNode = currNode.sibling;
      if (currNode.data == c){
        return currNode;
      }
    }//end of while
    return null; //default
  }

  private DlbNode traverseChildren(char c, DlbNode node){

    //if node doesn't have children
    DlbNode nextNode;
    if (node.child == null){
      return null;
    }
    else {
      nextNode = findOrInsertSibling(c, node.child); //check node version if error
    }
    return nextNode;
  }

  private DlbNode findOrInsertChild(char c, DlbNode node){
    //CASES
    //if node doesn't have children, insert c
    DlbNode nextNode;
    if (node.child == null){
      nextNode = new DlbNode();
      nextNode.data = c;
      node.child = nextNode;
    }
    else {
      nextNode = findOrInsertSibling(c, node.child); //SAME as above
    }
    return nextNode;
  }

  private DlbNode findOrInsertSibling(char c, DlbNode node){
      DlbNode nextNode, currNode;
      currNode = node;
      if (node.data == c){
        return node;
      }
      while (currNode.sibling != null){
        currNode = currNode.sibling;
        if (currNode.data == c){
          return currNode;
        }
      }//end of while
      //none found so we create additional node
      nextNode = new DlbNode();
      nextNode.data = c;
      currNode.sibling = nextNode;
      return nextNode;
    }

  //inner Node class
  private static class DlbNode {
    //variables
    private DlbNode sibling;
    private DlbNode child;
    private char data; // if data == '$', this marks the end of a word


  }//end of inner dlbNode class
}//end of DLB class
