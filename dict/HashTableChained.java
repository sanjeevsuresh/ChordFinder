/* HashTableChained.java */

package dict;
import list.*;

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

  protected int size = 0; 
  protected DList [] hash_table;
  protected int num_buckets; 

  /** 
   *  Construct a new empty hash table intended to hold roughly sizeEstimate
   *  entries.  (The precise number of buckets is up to you, but we recommend
   *  you use a prime number, and shoot for a load factor between 0.5 and 1.)
   **/

  public HashTableChained(int sizeEstimate) {
    double buckets = sizeEstimate / 0.75;          //initializes number of buckets so that load factor is 0.75
    num_buckets = (int)buckets;
    while (isPrime(num_buckets) == false) {        //increments number of buckets until it is a prime number
      num_buckets += 1; 
    }
    hash_table = new DList[num_buckets];   //creates a hash table with prime number of buckets
  }

  /** 
   *  Construct a new empty hash table with a default size.  Say, a prime in
   *  the neighborhood of 100.
   **/

  public HashTableChained() {
     num_buckets = 101;                      //prime number close to 100
     hash_table = new DList[num_buckets]; 
  }

  boolean isPrime(int n) {
    if (n % 2 == 0) {
      return false;
    }
    for(int i = 3; i * i <= n; i += 2) {
        if (n % i == 0)
            return false;
    }
    return true;
  }
  /**
   *  Converts a hash code in the range Integer.MIN_VALUE...Integer.MAX_VALUE
   *  to a value in the range 0...(size of hash table) - 1.
   *
   *  This function should have package protection (so we can test it), and
   *  should be used by insert, find, and remove.
   **/

  
  int compFunction(int code) {
    if (code % num_buckets < 0) {
      return (code % num_buckets) + num_buckets;
    } else {
    return code % num_buckets;
    } 
    /*int a = 13;
    int b = 23;
    int p = 1000000 * num_buckets;
    while (isPrime(p) == false) {        
      p += 1; 
    }
    return ((a * code + b) % p) % num_buckets; */
  }

  /** 
   *  Returns the number of entries stored in the dictionary.  Entries with
   *  the same key (or even the same key and value) each still count as
   *  a separate entry.
   *  @return number of entries in the dictionary.
   **/

  public int size() {
    return size; 
  }

  /** 
   *  Tests if the dictionary is empty.
   *
   *  @return true if the dictionary has no entries; false otherwise.
   **/

  public boolean isEmpty() {
    if (size == 0) {
      return true;
    } 
     return false;
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
    if (size / num_buckets > 0.75) { //table must be resized if load factor is too great
        this.resize();
    }
    Entry new_entry = new Entry();
    new_entry.key = key;
    new_entry.value = value;
    int comp_hash = compFunction(new_entry.key.hashCode());  //compresses key hash code
    if (hash_table[comp_hash] == null) {                     
      hash_table[comp_hash] = new DList();
      hash_table[comp_hash].insertFront(new_entry);
    } else {
      hash_table[comp_hash].insertFront(new_entry);
    }
    size++;
    return new_entry;
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
    Entry entry_with_key = new Entry();
    entry_with_key.key = key; 
    int comp_hash = compFunction(entry_with_key.key.hashCode());
    if (hash_table[comp_hash] == null) {
      return null;
    } else {
        DList searching = hash_table[comp_hash];
        for(Object entry: searching){
          if(((Entry) entry).key().equals(key)){
            return (Entry) entry;
          }
        }
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
    if (find(key) == null) {
      return null;
    } else { 
      try{
        int index = compFunction(key.hashCode());
        if (hash_table[index].size() == 1) {
          Entry entry = (Entry)hash_table[index].front().item();
          hash_table[index] = null;
          return entry;
        } else { 
          DListNode current = (DListNode) hash_table[index].front();
          while(current.isValidNode()){
            Entry pair = (Entry) current.item();
            if(pair.key().equals(key)){
              current.remove();
              size--;
              return pair;
            }
            current = (DListNode) current.next();
          }
          
          System.out.println("Couldn't find the item");
          return null;
        }
        
      }catch(InvalidNodeException e){
        return null;
      }
    }
   }  


  
  public void resize() {
    //System.out.println("Rehashing: ");
    try {
    num_buckets = num_buckets * 2;                                              //doubles number of buckets in table
    while (isPrime(num_buckets) == false) {                                     //ensures num_buckets is still prime
      num_buckets += 1; 
    }
    DList [] resizedHash = new DList[num_buckets];                              //creates temporary table 
    for (int i = 0; i < hash_table.length; i++) {                               //maps every entry in old table to new
      if (hash_table[i] != null) {
        DListNode current = (DListNode)hash_table[i].front();
        while (current.isValidNode()) {
          int comp_hash = compFunction(((Entry)current.item()).key.hashCode());  //compresses key hash code
          if (resizedHash[comp_hash] == null) {                     
            resizedHash[comp_hash] = new DList();
            resizedHash[comp_hash].insertFront(current.item());
          } else {
              resizedHash[comp_hash].insertFront(current.item());
          }
          current = (DListNode)current.next();
        }
      }
    }
   // System.out.println("This is table before rehashing: " + this);
    hash_table = resizedHash;                                                     //reassigns resized table to "this" table
    //System.out.println("This is table after rehashing: " + this);
   } catch (InvalidNodeException e) {
  }            
 }     

  /**
   *  Remove all entries from the dictionary.
   */
  public void makeEmpty() { 
    for (int i = 0; i < hash_table.length; i++) {
      if (hash_table[i] != null) {
        hash_table[i] = null;
      }
    }
    size = 0;
  }
  
  public int numCollisions() { 
    int collisions = 0;
    for (int i = 0; i < hash_table.length; i++) {
      if (hash_table[i] == null) {
      } else {
      collisions += hash_table[i].size() - 1;
          }
       }
  return collisions;
 }
  
  public int getSize() {
    return size;
  }
  
  public String toString() {
    String entry_list = "[ ";
    for (int i = 0; i < hash_table.length; i++){
      if (hash_table[i] != null) {
        entry_list += hash_table[i];
      } else { 
        entry_list += "[]";
      }
    }
    entry_list += " ]";
    return entry_list;
  }
   
  public static void main(String[] args) {
     HashTableChained tester = new HashTableChained (4);
     System.out.println(tester.num_buckets);
     System.out.println(tester);
     tester.insert(true, false);
     tester.insert(1, 2);
     System.out.println(tester);
     tester.insert(false, true);
     System.out.println(tester);
     tester.insert(100, null);
     tester.insert("baby", "java");
     tester.insert("61b", "shewchuk");
     tester.insert("test" , "code");
     System.out.println(tester);
     tester.insert("apple", "banana");
     tester.insert("pine", "tree");
     tester.insert("keyboard", "keys");
     tester.insert(105, 110);
     tester.insert(1000, 20);
     tester.insert("chaitanya", "awesome");
     tester.insert("nerd", "geek");
     tester.insert("tennis", "djokovic");
     tester.insert("book", "math");
     tester.insert('a', 'b');
     tester.insert("cs","canBeBoring");
     System.out.println(tester);
   }
}
    
