/* HashTableChained.java */

package dict;
import list.*;
import java.lang.Math.*;

/**
 *  HashTableChained implements a Dictionary as a hash table with chaining.
 *  All objects used as keys must have a valid hashCode() method, which is
 *  used to determine which bucket of the hash table an entry is stored in.
 *  Each object's hashCode() is presumed to return an int between
 *  Integer.MIN_VALUE and Integer.MAX_VALUE.  The HashTableChained class
 *  implements only the compression function, which maps the hash code to
 *  a bucket in the table's range.
 *
 *  DO NOT CHANGE ANY PROTOTYPES IN THIS FILE.
 **/

public class HashTableChained implements Dictionary {

  /**
   *  Place any data fields here.
   **/
  List[] buckets;
  int size;


  /** 
   *  Construct a new empty hash table intended to hold roughly sizeEstimate
   *  entries.  (The precise number of buckets is up to you, but we recommend
   *  you use a prime number, and shoot for a load factor between 0.5 and 1.)
   **/
  private static Boolean isPrime(int num) {
    if (num < 2) return false;
    if (num == 2) return true;
    if (num % 2 == 0) return false;
    for (int i=3; i*i <= num; i+=2) {
      if (num % i == 0) return false;
    }
    return true;
  }

  public HashTableChained(int sizeEstimate) {
    // Your solution here.
    int num_bucket = (sizeEstimate *4) /3;
    while (!isPrime(num_bucket)) {
      num_bucket++;
    }
    buckets = new SList[num_bucket];
    for (int i=0; i<num_bucket; i++) {
      buckets[i] = new SList();
    }
    size = 0;
  }
  /** 
   *  Construct a new empty hash table with a default size.  Say, a prime in
   *  the neighborhood of 100.
   **/

  public HashTableChained() {
    // Your solution here.
    buckets = new SList[101];
    for (int i=0; i<101; i++) {
      buckets[i] = new SList();
    }
    size = 0;
  }

  /**
   *  Converts a hash code in the range Integer.MIN_VALUE...Integer.MAX_VALUE
   *  to a value in the range 0...(size of hash table) - 1.
   *
   *  This function should have package protection (so we can test it), and
   *  should be used by insert, find, and remove.
   **/

  protected int compFunction(int code) {
    // Replace the following line with your solution.
    return (code % 16908799) % (buckets.length);
  }

  /** 
   *  Returns the number of entries stored in the dictionary.  Entries with
   *  the same key (or even the same key and value) each still count as
   *  a separate entry.
   *  @return number of entries in the dictionary.
   **/

  public int size() {
    // Replace the following line with your solution.
    return size;
  }

  /** 
   *  Tests if the dictionary is empty.
   *
   *  @return true if the dictionary has no entries; false otherwise.
   **/

  public boolean isEmpty() {
    // Replace the following line with your solution.
    return (size == 0);
  }

  /**
   *  Create a new Entry object referencing the input key and associated value,
   *  and insert the entry into the dictionary.  Return a reference to the new
   *  entry.  Multiple entries with the same key (or even the same key and
   *  value) can coexist in the dictionary.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the key by which the entry can be retrieved.
   *  @param value an arbitrary object.
   *  @return an entry containing the key and value.
   **/

  public Entry insert(Object key, Object value) {
    // Replace the following line with your solution.
    int compress;
    compress = compFunction(key.hashCode());
    compress = Math.abs(compress);
//    System.out.printf("compress = %d\n", compress);
    Entry entry = new Entry();
    entry.key = key;
    entry.value = value;
    buckets[compress].insertBack(entry);
    size++;
    return entry;
  }

  /** 
   *  Search for an entry with the specified key.  If such an entry is found,
   *  return it; otherwise return null.  If several entries have the specified
   *  key, choose one arbitrarily and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   **/

  public Entry find(Object key) {
    // Replace the following line with your solution.
    try {
      int compress = compFunction((int) key);
      ListNode node = buckets[compress].front();
       while (node != null) {
         if (((Entry)node.item()).key() == key) {
           return (Entry) node.item();
         }
         else {
           node = node.next();
         }
       }
      return null;
    } catch (InvalidNodeException e) {
      System.err.println("Catch InvalidNodeException in find().");
      return null;
    }
  }

  /** 
   *  Remove an entry with the specified key.  If such an entry is found,
   *  remove it from the table and return it; otherwise return null.
   *  If several entries have the specified key, choose one arbitrarily, then
   *  remove and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   */

  public Entry remove(Object key) {
    // Replace the following line with your solution.
    try {
      int compress = compFunction((int) key);
      ListNode node = buckets[compress].front();
      while (node != null) {
       if (((Entry)node.item()).key() == key) {
         node.remove();
         size--;
         return (Entry) node.item();
       }
     }
      return null;
    } catch (InvalidNodeException e) {
      System.err.println("Catch InvalidNodeException in remove().");
      return null;
    }
  }

  /**
   *  Remove all entries from the dictionary.
   */
  public void makeEmpty() {
    // Your solution here.
    for (int i=0; i<buckets.length; i++) {
      buckets[i] = new SList();
    }
    size = 0;
  }

  public void analysis() {
    int collisions = 0;
    int total_size = 0;
    double exp_collision = 0.0;
    for (int i=0; i<buckets.length; i++) {
      System.out.printf("[%d]", buckets[i].length());
      if (i % 10 == 9) System.out.println();
      total_size += buckets[i].length();
      if (buckets[i].length() > 1) {
        collisions += (buckets[i].length() - 1);
      }
    }
    float length = buckets.length;
    exp_collision = total_size - length + length*(Math.pow((length-1)/length,total_size));
    System.out.printf("\nTotal buckets number = %d\n", buckets.length);
    System.out.printf("Total entries number = %d\n", total_size);
    System.out.printf("Total collisions = %d\n", collisions);
    System.out.printf("Expected collisions = %.2f\n", exp_collision);
  }

}