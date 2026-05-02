package com.homework.hashmap;

@SuppressWarnings("unchecked")
public class MyHashMap<K, V> {

    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

    private Node<K, V>[] table;
    private int size;

    private static class Node<K, V> {
        final K key;
        V value;
        final int hash;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_INITIAL_CAPACITY];
        size = 0;
    }

    public V put(K key, V value) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        Node<K,V> node = table[index];

        if (node == null) {
            table[index] = new Node<>(hash, key, value, null);
            size++;
            return null;
        }

        Node<K,V> prev = node;
        while (node != null) {
            if (node.hash == hash && (node.key == key || (key != null && key.equals(node.key)))) {
                V oldValue = node.value;
                node.value = value;
                return oldValue;
            }

            prev = node;
            node = node.next;
        }

        prev.next = new Node<>(hash, key, value, null);
        size++;

        return null;
    }

    public V get(K key) {
        Node<K, V> node = getNode(key);

        if (node == null) {
            return null;
        }

        return node.value;
    }

    public V remove(K key) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);
        Node<K,V> node = table[index];

        if (node == null) {
            return null;
        }

        if (node.hash == hash && (node.key == key || (key != null && key.equals(node.key)))) {
            table[index] = node.next;
            size--;
            return node.value;
        }

        Node<K,V> prev = node;
        node = node.next;
        while (node != null) {
            if (node.hash == hash && (node.key == key || (key != null && key.equals(node.key)))) {
                prev.next = node.next;
                size--;
                return node.value;
            }
            prev = node;
            node = node.next;
        }

        return null;
    }

    public int size() {
        return size;
    }

    public boolean containsKey(K key) {
        return getNode(key) != null;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private int hash(K key) {
        if (key == null ) {
            return 0;
        }

        int hash = key.hashCode();
        return hash ^ (hash >>> 16);
    }

    private int indexFor(int hash, int capacity) {
        return hash & (capacity - 1);
    }


    private Node<K, V> getNode(K key) {
        int hash = hash(key);
        int index = indexFor(hash, table.length);

        Node<K,V> node = table[index];

        while (node != null) {
            if (node.hash == hash && (node.key == key || (key != null && key.equals(node.key)))) {
                return node;
            } else {
                node = node.next;
            }
        }

        return null;
    }

}