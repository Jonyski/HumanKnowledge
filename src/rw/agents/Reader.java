package rw.agents;

import java.util.*;

public class Reader extends Agent implements Runnable {
    private ArrayList<String> wordsRead; // Só existe porque o enunciado pede

    public Reader(ArrayList<String> DB, RWLock lock) {
        super(DB, lock);
        this.wordsRead = new ArrayList<String>();
    }

    // Lê 100 palavras do BD e dorme por 1ms
    public void run() {
        lock.readLock();
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
            lock.readUnlock();
        }
    }

    private void read() {
        wordsRead.add(DB.get(getRandDBIndex()));
    }
}
