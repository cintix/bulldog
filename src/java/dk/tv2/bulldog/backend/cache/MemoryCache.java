/*
 */
package dk.tv2.bulldog.backend.cache;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * Simple InMemory Cache class
 *
 * @author migo
 * @param <K> Generic key type
 * @param <T> Generic value type
 */
public class MemoryCache<K, T> {
    private final long timeToLive;
    private final LinkedHashMap<K, MemoryCacheObject> cacheMap;
    private final int maxItems;

    /**
     * Local Cache class
     */
    protected class MemoryCacheObject {

        /**
         * last accessed time
         */
        public long timeCreated = System.currentTimeMillis();

        /**
         * Generic value
         */
        public T value;

        /**
         *
         * @param value
         */
        protected MemoryCacheObject(T value) {
            this.value = value;
        }
    }

    /**
     * Create a InMemoryCache with maximum items and TTL and Max items
     *
     * @param timeToLive in seconds
     * @param maxItems
     */
    public MemoryCache(long timeToLive, int maxItems) {
        if (timeToLive != -1) {
            this.timeToLive = timeToLive * 1000;
        } else {
            this.timeToLive = -1;
        }

        this.maxItems = maxItems;
        cacheMap = new LinkedHashMap<>(maxItems);
    }

    /**
     * gets all items in the map
     *
     * @return
     */
    public List<T> getAll() {
        synchronized (cacheMap) {
            List<T> list = new ArrayList<>();
            for (MemoryCacheObject instance : cacheMap.values()) {
                list.add(instance.value);
            }
            return list;
        }
    }

    /**
     * Store InMemory Object in cache
     *
     * @param key
     * @param value
     */
    @SuppressWarnings("unchecked")
    public void put(K key, T value) {
        synchronized (cacheMap) {
            if (cacheMap.size() == maxItems) {
                K[] keys = (K[]) Array.newInstance(key.getClass(), maxItems);
                cacheMap.keySet().toArray(keys);
                cacheMap.remove(keys[0]);
            }
            cacheMap.put(key, new MemoryCacheObject(value));
            //for(CacheListener c: cacheListeners) c.update(key);

        }
    }

    /**
     *
     * Access InMemory Object from cache
     *
     * @param key
     * @return Generic value
     */
    @SuppressWarnings("unchecked")
    public T get(K key) {
        MemoryCacheObject c;
        synchronized (cacheMap) {
            c = (MemoryCacheObject) cacheMap.get(key);
        }

        cleanup();
        if (c == null) {
            return null;
        } else {
            return c.value;
        }

    }

    /**
     * Conatains key
     *
     * @param key
     * @return
     */
    public boolean contains(K key) {
        cleanup();
        return cacheMap.containsKey(key);
    }

    /**
     * Renew key in cache
     *
     * @param key
     */
    public void renew(K key) {
        synchronized (cacheMap) {
            MemoryCacheObject cacheObject = cacheMap.get(key);
            cacheObject.timeCreated = System.currentTimeMillis();
            cacheMap.put(key, cacheObject);
        }
    }

    /**
     * Remove InMemory Object
     *
     * @param key
     */
    public void remove(K key) {
        synchronized (cacheMap) {
            cacheMap.remove(key);
        }
    }

    /**
     * Get items count from InMemory
     *
     * @return size
     */
    public int size() {
        synchronized (cacheMap) {
            return cacheMap.size();
        }
    }

    /**
     * Returns cachetime
     *
     * @param key
     * @return
     */
    public long getCacheTimeInSeconds(K key) {
        synchronized (cacheMap) {
            long time = System.currentTimeMillis() - cacheMap.get(key).timeCreated;
            return time / 1000;
        }
    }

    /**
     * Remove all expired Objects from the InMemory Cache
     */
    @SuppressWarnings("unchecked")
    public void cleanup() {

        long now = System.currentTimeMillis();
        ArrayList<K> deleteKey;

        synchronized (cacheMap) {
            Iterator itr = cacheMap.keySet().iterator();

            deleteKey = new ArrayList<>((cacheMap.size() / 2) + 1);
            K key;
            MemoryCacheObject c;

            while (itr.hasNext()) {
                key = (K) itr.next();
                c = (MemoryCacheObject) cacheMap.get(key);
                if (c != null && timeToLive != -1 && (now > (timeToLive + c.timeCreated))) {
                    deleteKey.add(key);
                }
            }
        }

        for (K key : deleteKey) {
            synchronized (cacheMap) {
                cacheMap.remove(key);
            }
        }
    }

    /**
     * Clear cache
     */
    public void clear() {
        synchronized (cacheMap) {
            cacheMap.clear();
        }
    }


}
