package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

/**
 * See IDictionary for more details on what this class should do
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private Pair<K, V>[] pairs;
    
    private int currArraySize = 10;
    private int size;
    
    // You're encouraged to add extra fields (and helper methods) though!
    
    public ArrayDictionary() {
        this.pairs = makeArrayOfPairs(currArraySize);
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);

    }

    @Override
    public V get(K key) {
        
          if (this.isEmpty()) {
            throw new NoSuchKeyException();
            } 
            else {
            for (int i = 0; i < size; i++) {
                K keyVal = pairs[i].key;
                if (keyVal == key || keyVal.equals(key)) 
                {
                return pairs[i].value;
                }
            }
            throw new NoSuchKeyException();
        }
    }

    @Override
    public void put(K key, V value) {
        
        if (containsKey(key)) {
            for (int i = 0; i < size; i++) {
                K keyVal = pairs[i].key;
                if (keyVal == key || keyVal.equals(key)) {
                    pairs[i].value = value;
                }
            }
        } 
        else {
            
            if (size == currArraySize) {
                Pair<K, V>[] newArrayPairs = makeArrayOfPairs(currArraySize * 2);
                for (int i = 0; i < size; i++) {
                    newArrayPairs[i]= new Pair<K, V>(pairs[i].key, pairs[i].value);
                }
                pairs = newArrayPairs;
                currArraySize = currArraySize * 2;
                }
                pairs[size] = new Pair<K, V>(key, value);
                size++;
        }
    }

    @Override
    public V remove(K key) {
        
        if (!containsKey(key)) {
            throw new NoSuchKeyException();
        }
        V removedValue = null;
        int index = 0;
        
        for (int i = 0; i < size; i++) {
            K keyVal = pairs[i].key;
            if (keyVal == key || keyVal.equals(key)) {
                removedValue = pairs[i].value;
                pairs[i].key = null;
                pairs[i].value = null;
                index = i;
            }
        }
        
        for (int i = index; i < size; i++) {
            pairs[i] = pairs[i + 1];
        }
        pairs[size] = null;
        size--;
        return removedValue;
    }

    @Override
    public boolean containsKey(K key) {
        for (int i = 0; i < size; i++) {
            K keyVal = pairs[i].key;
            if (keyVal == key || keyVal.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return this.size;
    }

    private static class Pair<K, V> {
        public K key;
        public V value;                 // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
}
