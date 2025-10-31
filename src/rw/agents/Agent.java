package rw.agents;

import java.util.*;
import java.util.concurrent.locks.Lock;

public abstract class Agent {
    protected ArrayList<String> DB;
    protected Lock lock;

    public Agent(ArrayList<String> DB, Lock lock) {
        this.DB = DB;
        this.lock = lock;
    }

    // Como o BD é um texto de 36242 linhas, essa função gera um número de 0 a 36242
    public int getRandDBIndex() {
        Random rand = new Random();
        return rand.nextInt(DB.size());
    }
}
