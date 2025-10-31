package rw.agents;

import rw.Agent;
import java.util.*;
import java.util.concurrent.locks.Lock;

public class Reader extends Agent implements Runnable {
    private ArrayList<String> wordsRead;

    public Reader(ArrayList<String> DB, Lock lock) {
        super(DB, lock);
        this.wordsRead = new ArrayList<String>();
    }

    public void run() {
        lock.lock();
        try {
            for (int i = 0; i < 100; i++) {
                read();
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        } finally {
            lock.unlock();
        }
    }

    private void read() {
        wordsRead.add(DB.get(getRandDBIndex()));
    }
}
