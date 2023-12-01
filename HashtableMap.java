// --== CS400 Fall 2023 File Header Information ==--
// Name: Cole Movsessian
// Email: movsessian@wisc.edu
// Group: G27
// TA: Grant Waldow
// Lecturer: Florian
// Notes to Grader: <optional extra notes>

import org.junit.Test;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Assertions;
import java.lang.Math.*;

/**
 * Stored key, value pairs in a map
 * @param <KeyType> Key type
 * @param <ValueType> Value type
 */
public class HashtableMap<KeyType, ValueType> implements MapADT{
    /**
     * Stores key and value in pair object
     */
    protected class Pair {
        public KeyType key;
        public ValueType value;

        public Pair(KeyType key, ValueType value){
            this.key = key;
            this.value = value;
        }
    }

    protected LinkedList<Pair>[] table;
    protected int tableCapacity = 30;

    /**
     * Default constructor for HashmapTable
     * @param capacity
     */
    @SuppressWarnings("unchecked")
    public HashtableMap(int capacity){
        this.tableCapacity = capacity;
        table = new LinkedList[capacity];
    }

    /**
     * Constructor to set table to custom capacity
     */
    @SuppressWarnings("unchecked")
    public HashtableMap(){
        table = new LinkedList[tableCapacity];
    }

    /**
     * Adds a new key,value pair/mapping to this collection.
     * @param key the key of the key,value pair
     * @param value the value that key maps to
     * @throws IllegalArgumentException if key already maps to a value
     */
    @Override
    public void put(Object key, Object value) throws IllegalArgumentException {
        // Checks for null or already existing key
        if(key == null || containsKey(key)){
            throw new IllegalArgumentException("Key is null or already stored");
        }

        Pair putPair = new Pair((KeyType) key, (ValueType) value);
        int keyHash = Math.abs(((key.hashCode()) % tableCapacity)); // calculates hash code to determine table placement

        if(table[keyHash] == null){
            table[keyHash] = new LinkedList<>();
        }

        table[keyHash].add(putPair);

        // Checks if table capacity needs to be doubled
        if(((double)getSize()/(double)getCapacity()) >= 0.75){
            doubleHashtable();
        }
    }

    /**
     * Doubles the capacity of the hashtable and rehashes stored values
     */
    private void doubleHashtable(){
        this.tableCapacity = tableCapacity*2;
        LinkedList<Pair>[] newTable = new LinkedList[tableCapacity];

        // Visits each pairChain
        for(LinkedList<Pair> pairChain : table){
            if(pairChain != null){
                // Visits each pair and copies it to new table
                for(Pair pair : pairChain){
                    // Calculates new hash with doubled table capacity
                    int newHash = Math.abs((pair.key.hashCode())) % tableCapacity;

                    if(newTable[newHash] == null){
                        newTable[newHash] = new LinkedList<>();
                    }
                    newTable[newHash].add(pair);
                }
            }
        }

        table = newTable;
    }

    /**
     * Checks whether a key maps to a value in this collection.
     * @param key the key to check
     * @return true if the key maps to a value, and false is the
     *         key doesn't map to a value
     */
    @Override
    public boolean containsKey(Object key) {
        int keyHash = Math.abs((key.hashCode()) % tableCapacity);
        LinkedList<Pair> pairChain = table[keyHash];

        if(table[keyHash] == null){
            return false;
        }

        // Visits each pair in pairChain with the key hash
        for(Pair pair : pairChain){
            if(pair.key.equals(key)){
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the specific value that a key maps to.
     * @param key the key to look up
     * @return the value that key maps to
     * @throws NoSuchElementException when key is not stored in this
     *         collection
     */
    @Override
    public Object get(Object key) throws NoSuchElementException {
        // Confirms key is stored
        if(!(this.containsKey(key))){
            throw new NoSuchElementException("Key not stored");
        }

        int keyHash = Math.abs((key.hashCode()) % tableCapacity);
        LinkedList<Pair> pairChain = table[keyHash];

        // Visits each pair in pairChain with the key hash
        for(Pair pair : pairChain){
            if(pair.key.equals(key)){
                return pair.value;
            }
        }

        throw new NoSuchElementException("Key not stored");
    }

    /**
     * Remove the mapping for a key from this collection.
     * @param key the key whose mapping to remove
     * @return the value that the removed key mapped to
     * @throws NoSuchElementException when key is not stored in this
     *         collection
     */
    @Override
    public Object remove(Object key) throws NoSuchElementException {
        // Confirms key is stored
        if(!(containsKey(key))){
            throw new NoSuchElementException("Key not stored");
        }

        Pair removePair = null;
        Object removeValue = null;
        int keyHash = Math.abs((int)key % tableCapacity);
        LinkedList<Pair> pairChain = table[keyHash];

        // Visits each pair in pairChain
        for(Pair pair : pairChain){
            // When key is found pair is removed
            if(pair.key.equals(key)){
                removePair = pair;
                removeValue = pair.value;
                break;
            }
        }

        // Removes pair
        if(removeValue != null){
            pairChain.remove(removePair);
        }else{
            throw new NoSuchElementException("Key not stored");
        }
        return removeValue;
    }

    /**
     * Removes all key,value pairs from this collection.
     */
    @Override
    public void clear() {
        // Iterates through every pair in every pairChain and clears values
        for(LinkedList<Pair> pairChain : table){
            if(pairChain != null) {
                pairChain.clear();
            }
        }
    }

    /**
     * Retrieves the number of keys stored in this collection.
     * @return the number of keys stored in this collection
     */
    @Override
    public int getSize() {
        int size = 0;

        // Iterates through each pairChain in table
        for(LinkedList<Pair> pairChain : table){
            // For each non-null pairChain iterates through pairs and increments size
            if(pairChain != null){
                for(Pair pair : pairChain){
                    size++;
                }
            }
        }

        return size;
    }

    /**
     * Retrieves this collection's capacity.
     * @return the size of te underlying array for this collection
     */
    @Override
    public int getCapacity() {
        return tableCapacity;
    }

    /**
     *  Tests collisions on pair put
     */
    @Test
    public void testPutCollision(){
        HashtableMap<Integer, Integer> testMap = new HashtableMap();

        try{
            testMap.put(32,100);
            testMap.put(62,200);
            testMap.put(92,300);
        }catch(Exception e){
            Assertions.fail(e.getMessage());
        }
    }

    /**
     * Tests contains key method
     */
    @Test
    public void testContainsKey(){
        HashtableMap<Integer, Integer> testMap = new HashtableMap(10);

        testMap.put(12,100);
        testMap.put(2,100);

        Assertions.assertTrue(testMap.containsKey(2));
        Assertions.assertTrue(!(testMap.containsKey(3)));
    }

    /**
     * Tests remove method
     */
    @Test
    public void testRemove(){
        HashtableMap<Integer, Integer> testMap = new HashtableMap(10);

        testMap.put(10, 99);
        testMap.put(12,200);
        testMap.put(2,100);
        testMap.put(32,300);
        testMap.put(3, 45);
        testMap.put(9, 69);

        testMap.remove(12);
        testMap.remove(9);

        Assertions.assertTrue(!testMap.containsKey(12) && !testMap.containsKey(9));
        Assertions.assertEquals(4, testMap.getSize());
    }

    /**
     * Tests get method
     */
    @Test
    public void testGet(){
        HashtableMap<Integer, Integer> testMap = new HashtableMap(10);

        testMap.put(12,200);
        testMap.put(2,100);
        testMap.put(32,300);

        Assertions.assertEquals(100, testMap.get(2));
    }

    /**
     * Tests get size and capacity methods
     */
    @Test
    public void testGetSizeAndCapacity(){
        HashtableMap<Integer, Integer> testMap = new HashtableMap(10);

        testMap.put(10, 99);
        testMap.put(12,200);
        testMap.put(2,100);
        testMap.put(32,300);
        testMap.put(3, 45);
        testMap.put(9, 69);

        Assertions.assertEquals(10, testMap.getCapacity());
        Assertions.assertEquals(6, testMap.getSize());
    }

    /**
     * Tests clear method
     */
    @Test
    public void testClear(){
        HashtableMap<Integer, Integer> testMap = new HashtableMap(10);

        testMap.put(10, 99);
        testMap.put(12,200);
        testMap.put(2,100);
        testMap.put(32,300);
        testMap.put(3, 45);
        testMap.put(9, 69);

        testMap.clear();

        Assertions.assertEquals(0, testMap.getSize());
    }
}
