// Alex Drizos
// CS1501 - Assignment 3
//DLB tree class - impliments DictInterface class. moddified for LZW compression program


//imports
import java.util.*;
import java.io.*;


public class DLB<T> {

  //variables
  private final char TERMINATOR = '$';
  private int numWords;
  private DlbNode root;

  //constructors
  public DLB()
  {

  }

  //functions
  public boolean add(StringBuilder s, T value){

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

      //add st value
      currNode.value = value;
      return true;
  }//end of add

  public T search(StringBuilder s){

     DlbNode currNode = root; //default

     for (int i = 0; i < s.length(); i++){
       if (i == 0){
           if (root == null){
               return null;
           }//no first letters have been added yet

           else {
               currNode = traverseSiblings(s.charAt(i),root); //traverse siblings to find char match
               if (currNode == null){
                 return null;
               }
           }
       } // first letter of s

       //not the first char in s
       else {
         currNode = traverseChildren(s.charAt(i), currNode);
         if (currNode == null){
           return null;
         }
       }
     }//end of master for

    return currNode.value;

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
  private class DlbNode {
    //variables
    private DlbNode sibling;
    private DlbNode child;
    private char data; // if data == '$', this marks the end of a word
    private T value;

  }//end of inner dlbNode class
}//end of DLB class
