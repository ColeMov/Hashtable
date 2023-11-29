import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.*;

public class HashtableMap<KeyType, ValueType> implements MapADT{
    protected class Pair {
        public KeyType key;
        public ValueType value;

        public Pair(KeyType key, ValueType value){
            this.key = key;
            this.value = value;
        }
    }


    public HashtableMap(int capacity){
        protected LinkedList<Pair>[] table = (capacity);
    }

    public HashtableMap(){

    }
    @Override
    public void put(Object key, Object value) throws IllegalArgumentException {

    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public Object get(Object key) throws NoSuchElementException {
        return null;
    }

    @Override
    public Object remove(Object key) throws NoSuchElementException {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public int getCapacity() {
        return 0;
    }
}
