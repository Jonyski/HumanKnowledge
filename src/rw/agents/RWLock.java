package rw.agents;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RWLock {
    private int readers = 0; 
    private int writers = 0; 
    private Lock lock = new ReentrantLock();
    // duas Conditions para gerenciar leitores e escritores
    private Condition okToRead = lock.newCondition(); 
    private Condition okToWrite = lock.newCondition(); 

    // lock de leitura
    public void readLock() {
        lock.lock(); 
        try {
            // Um leitor só espera se um escritor estiver ativo -> prioridade para leitores
            while (writers > 0) {
                okToRead.await(); // leitor espera
            }
            readers++; 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    // Libera o lock de leitura
    public void readUnlock() {
        lock.lock(); 
        try {
            readers--; 
            if (readers == 0) {
                okToWrite.signal(); // acorda apenas uma thread na fila de escrita
            }
        } finally {
            lock.unlock();
        }
    }

    // lock de escrita
    public void writeLock() {
        lock.lock(); 
        try {
            // Um escritor espera se já existirem leitores lá dentro (readers > 0)
            // ou já existe outro escritor lá dentro (writers > 0)
            while (readers > 0 || writers > 0) {
                okToWrite.await(); 
            }
            writers++; 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    // Libera o lock de escrita
    public void writeUnlock() {
        lock.lock(); 
        try {
            writers--;
            // acorda todos os leitores que estavam esperando.
            okToRead.signalAll(); 
            
            // acorda um escritor para garantir que a fila de escrita não morra (starvation)
            okToWrite.signal();
        } finally {
            lock.unlock(); 
        }
    }
}