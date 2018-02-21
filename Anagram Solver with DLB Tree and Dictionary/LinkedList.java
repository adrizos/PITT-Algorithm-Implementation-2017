//Alex Drizos
//CS1501 - Assignment 1
//Nodelet Class to support dlb class - includes node and linked list CLASSES


public class LinkedList{

  //variables
  private Node root; //beginning of the linked list
  private Node lastChar; //pointer to end of LL
  private int length;

  //CONSTRUCTORS


  public LinkedList(){
    Node newNode = new Node();
    this.root = newNode;
    length = 0;
    lastChar = newNode;
  }


  //Custom METHODS
  public boolean isEmpty(){
    if (length == 0){
      return true;
    }
    else{
      return false;
    }
  }

  public void add(char c){

  }
  public int size(){
    return length;
  }
  public char get(int i)
  {
    return data;
  }


  //inner Node class
  private static class Node {
  //variables
  private Node sibling;
  private Node child;
  private char data; // if data == '$', this marks the end of a word

  //constructors
  public Node (){

  }

  public Node (char c){
    data = c;
  }

  //METHODS

  public char getData(){
    return data;
  }
  public void setData(char c){
    data = c;
  }

}//end of inner Node class


}//end of LinkedList class
