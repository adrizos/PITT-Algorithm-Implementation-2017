/*************************************************************************
 *
 *  Alex Drizos
 *  CS1501 - University of Pittsburgh
 *  Project 2
 *
 *  PHPArray Class - will approximate the PHP array class using Java.
 *  Test Driver File - Assig2.java
 *************************************************************************/

//imports
import java.util.*;
import java.lang.*;

public class PHPArray<V> implements Iterable<V> {
  //constants
  private static final int START_SIZE = 5;
  private static final String STRNULL = "null";
  private static final String STRKEY = "Key";
  private static final String STRVALUE = "Value:";
  //linkedlist variables
  private Node<V> head;
  private Node<V> tail;
  private Node<V> eachNode;
  //hashtables vars
  private int N;           // number of key-value pairs in the symbol table
  private int M;           // size of linear probing table
  private Node<V>[] nodeArr; //array of nodes to store data


  public PHPArray(int tableSize){
    M = tableSize;
    nodeArr = (Node<V>[]) new Node<?>[M];
  }

  //methods

  // return the number of key-value pairs in the symbol table
  public int size() {
      return N;
  }
  public boolean isEmpty() {
      return size() == 0;
  }

  public void put(int intString, V value){
    put(Integer.toString(intString), value);
  }

  public void put(String key, V value){
    int hashCode = getHashCode(key);

    if (value == null) unset(key);

    // double table size if 50% full or greater
    if (N >= M/2){
      System.out.printf("\tSize: %d -- Resizing from %d to %d\n", N, M, M*2);
      resize(2*M);
    }

    int i;
    for (i = hashCode; nodeArr[i] != null; i = (i + 1) % M) {
        //same data has already been put
        if (nodeArr[i].key.equals(key)){
           nodeArr[i].value = value;
           return;
        }
    }
    //otherwise set data
    nodeArr[i] = new Node<V>();
    nodeArr[i].key = key;
    nodeArr[i].value = value;

    //linkedlist book keeping
    //case in which the list is empty
    if (head == null){
      head = nodeArr[i];
      tail = nodeArr[i];
      eachNode = nodeArr[i];
    }
    //case in which the list is not empty
    else {
      tail.next = nodeArr[i];
      nodeArr[i].prev = tail;
      tail = nodeArr[i];
    }

    N++; //increment counter
  }//end of put


  public int getHashCode(String key){
    return (key.hashCode() & 0x7fffffff) % M;
  }//end of getHashCode

  public void resize(int capacity){
    PHPArray<V> temp = new PHPArray<V>(capacity);
    Node<V> traverseNode = head;
    while (traverseNode != null){
      temp.put(traverseNode.key, traverseNode.value);
      traverseNode = traverseNode.next;
    }
    nodeArr = temp.nodeArr;
    M    = temp.M;
    head = temp.head;
    tail = temp.tail;
    eachNode = temp.eachNode;
  }

  public Iterator <V> iterator(){
    return new PHPArrayIterator();
  }

  public Pair<V> each(){
    Pair<V> tempPair = new Pair<V>();
    if (eachNode != null){
      tempPair.key = eachNode.key;
      tempPair.value = eachNode.value;
      eachNode = eachNode.next;
      return tempPair;
    }
    else{
      return null;
    }
  }

  public ArrayList<String> keys(){
    ArrayList<String> strings = new ArrayList<>();
    Node<V> iterateNode = new Node<V>();
    iterateNode = head;
    while (iterateNode != null){
      strings.add(iterateNode.key);
      iterateNode = iterateNode.next;
    }
    return strings;
  }

  public ArrayList<V> values(){
    ArrayList<V> vals = new ArrayList<>();
    Node<V> iterateNode = new Node<V>();
    iterateNode = head;
    while (iterateNode != null){
      vals.add(iterateNode.value);
      iterateNode = iterateNode.next;
    }
    return vals;
  }

  public void showTable(){
    System.out.println("\tRaw Hash Table Contents");
    for (int i = 0; i < M; i ++){
      if (nodeArr[i] == null){
        System.out.printf("%d: %s\n", i, STRNULL);
      }
      else {
        System.out.printf("%d: %s: %s %s %s\n", i, STRKEY, nodeArr[i].key, STRVALUE, nodeArr[i].value);
      }
    }
  }

  public V get(String s){
      int hashCode = getHashCode(s);
      for (int i = hashCode; nodeArr[i] != null; i = (i + 1) % M)
          if (nodeArr[i].key.equals(s)){
              return nodeArr[i].value;
          }
      return null;
  }
  //additional get() method to support user entering an int type for "value" field
  public V get(int intString){
    return get(Integer.toString(intString));
  }

  public int length(){
    return N;
  }

  // check if a key-value pair with the submitted key exists in the table
  public boolean contains(String key) {
      return get(key) != null;
  }

  public void reset(){
    eachNode = head; //reset eachNode iterator Node back to the head to allow each() to iterate again from begininning
  }

  public void unset(int intString){
    unset(Integer.toString(intString));
  }
  //delete key, value pair from the symbol table and the linked list
  public void unset(String key){
    if (!contains(key)) return;

    //locate index i of key
    int i = getHashCode(key);
    while (!key.equals(nodeArr[i].key)) {
        i = (i + 1) % M;
    }
    //linked list book keeping
    //3 cases: node to be delete is the only node, a middle node, or an end node
    //1: node is the head
    Node<V> tempNode;

    if (nodeArr[i] == head && nodeArr[i] == tail){
      tail = null;
      head = null;
    }
    else if (nodeArr[i] == head){
      head = head.next;
      head.prev = null;
    }
    else if (nodeArr[i] == tail){
      //3: node is at the end
      tempNode = tail.prev;
      tempNode.next =  null;
      tail = tempNode;
    }
    //2: node is in the middle //instead of traversing to find middle we can use nodeArr[i] ptr
    else {
      tempNode = nodeArr[i].prev;
      tempNode.next = tempNode.next.next;
      nodeArr[i].next.prev = tempNode;
    }

    // delete node
    nodeArr[i] = null;

    //rehash keys in cluster from i+1 to empty space
    i = (i + 1) % M;
    while (nodeArr[i] != null) {
        // delete key and value at i and reinsert
        String   keyToRehash = nodeArr[i].key;
        V valToRehash = nodeArr[i].value;
        Node<V> temp = nodeArr[i];
        nodeArr[i] = null;

        //for grading: print each key that must be rehashed
        System.out.printf("Key: %s rehashed...\n\n", keyToRehash);
        //rehash
        int j;
        int hashCode = getHashCode(keyToRehash);
        for (j = hashCode; nodeArr[j] != null; j = (j + 1) % M) {

        }
        nodeArr[j] = temp;
        i = (i + 1) % M;
    }
    //decrement N
    N--;
  }

  private  Node<V>[] mergeSort(Node<V>[] _nodeArr) {
        if (_nodeArr.length <= 1) {
            return _nodeArr;
        }
        // seperate array into two arrays
        Node<V>[] left = (Node<V>[]) new Node<?>[_nodeArr.length / 2];
        Node<V>[] right = (Node<V>[]) new Node<?>[_nodeArr.length - left.length];
        System.arraycopy(_nodeArr, 0, left, 0, left.length);
        System.arraycopy(_nodeArr, left.length, right, 0, right.length);

        // merge sort each sub array
        mergeSort(left);
        mergeSort(right);

        // merge the sub arrays replacing original array
        merge(left, right, _nodeArr);
        return _nodeArr;
  }

  private void merge(Node<V>[] left, Node<V>[] right, Node<V>[] merged){
    int rightCounter = 0;
    int leftCounter = 0;
    for (int i = 0; i < merged.length; i++){
      if (leftCounter>= left.length){
        merged[i] = right[rightCounter];
        rightCounter++;
      }
      else if (rightCounter >= right.length){
        merged[i] = left[leftCounter];
        leftCounter++;
      }
      else if (compareNodes(left[leftCounter],right[rightCounter]) <=0){
        merged[i] = left[leftCounter];
        leftCounter++;
      }
      else{
        merged[i] = right[rightCounter];
        rightCounter++;
      }
    }
  }

  private int compareNodes(Node<V> l, Node<V> r){
      if (l == null && r == null){
        return 0;
      }
      else if (l == null){
        return 1;
      }
      else if (r == null){
        return -1;
      }
      return ((Comparable)l.value).compareTo((Comparable)r.value);
  }


  public void sort(){
    if (head == null){
      return;
    }
    try {
      Comparable x = (Comparable)head.value;
    }
    catch   (ClassCastException e) {
      throw e;
    }

    nodeArr = mergeSort(nodeArr);
    //rebuilding sorted structure
    PHPArray<V> temp = new PHPArray<V>(M);
    for (int h = 0; h < N; h++){
      temp.put(h, nodeArr[h].value);
    }
    nodeArr = temp.nodeArr;
    M    = temp.M;
    head = temp.head;
    tail = temp.tail;
    eachNode = temp.eachNode;
  }

  public void asort(){
    if (head == null){
      return;
    }
    try {
      Comparable x = (Comparable)head.value;
    }
    catch   (ClassCastException e) {
      throw e;
    }

    nodeArr = mergeSort(nodeArr);
    //rebuilding sorted structure
    PHPArray<V> temp = new PHPArray<V>(M);
    for (int h = 0; h < N; h++){
      temp.put(nodeArr[h].key, nodeArr[h].value);
    }
    nodeArr = temp.nodeArr;
    M    = temp.M;
    head = temp.head;
    tail = temp.tail;
    eachNode = temp.eachNode;
  }

  public PHPArray<String> array_flip(){
    PHPArray<String> temp = new PHPArray<>(M);
    Node<V> traverseNode = head;
    while (traverseNode != null){
      temp.put((String)traverseNode.value,traverseNode.key);
      traverseNode = traverseNode.next;
    }
    return temp;
  }

  //INNER CLASSES
  //inner iterator class
  private class PHPArrayIterator implements Iterator <V>{
    Node<V> curr;
    public PHPArrayIterator(){
      curr = head;
    }
    public V next(){
      Node<V> temp;
      if (curr != null){
        temp = curr;
        curr = curr.next;
        return temp.value;
      }
      else {
        throw new NoSuchElementException();
      }
    }

    public boolean hasNext(){
        if (curr == null){
          return false;
        }
        else {
          return true;
        }
    }
  }
  //inner Node class
  private static class Node <V>{
    Node<V>  prev;
    Node<V> next;
    String key;
    V value;

  }//end of Node class

  //public static inner Pair<V> class
  public static class Pair <V> {
    String key;
    V value;
  } //end of Pair class

}//end of PHPArray class
