import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue;
    private int size;

    public RandomizedQueue() {
        queue = (Item[]) new Object[2];
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("Item cannot be null");
        if (size == queue.length) resize(2 * queue.length);
        queue[size++] = item;
    }

    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue is empty");
        int randomIndex = StdRandom.uniform(size);
        Item item = queue[randomIndex];
        queue[randomIndex] = queue[size - 1];
        queue[size - 1] = null;
        size--;

        if (size > 0 && size == queue.length / 4) resize(queue.length / 2);
        return item;
    }

    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Queue is empty");
        int randomIndex = StdRandom.uniform(size);
        return queue[randomIndex];
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            copy[i] = queue[i];
        }
        queue = copy;
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private final Item[] randomizedItems;
        private int currentIndex;

        public RandomizedQueueIterator() {
            randomizedItems = (Item[]) new Object[size];
            System.arraycopy(queue, 0, randomizedItems, 0, size);
            StdRandom.shuffle(randomizedItems);
            currentIndex = 0;
        }

        public boolean hasNext() {
            return currentIndex < size;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return randomizedItems[currentIndex++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        rq.enqueue("A");
        rq.enqueue("B");
        rq.enqueue("C");

        System.out.println("Sample: " + rq.sample());
        System.out.println("Dequeue: " + rq.dequeue());

        for (String s : rq) {
            System.out.println(s);
        }
    }
}
