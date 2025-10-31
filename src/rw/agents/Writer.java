package rw.agents;

import java.util.*;
import java.util.concurrent.locks.Lock;

public class Writer extends Agent implements Runnable {
    public Writer(ArrayList<String> DB, Lock lock) {
        super(DB, lock);
    }

    // Escreve 100 vezes "MODIFICADO" no BD e dorme por 1ms
    public void run() {
        lock.lock();
        try {
            for (int i = 0; i < 100; i++) {
                write();
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

    private void write() {
        DB.set(getRandDBIndex(), "MODIFICADO");
    }
}
