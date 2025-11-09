package rw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.nio.file.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import rw.agents.*;

public class ReadersWriters {
    private static final int numObjs = 100;
    private final int numReaders;
    public ArrayList<Thread> threadObjs; // Lista de agentes (Readers e Writers)
    public ArrayList<String> DB; // Lista de palavras do bd.txt
    private Lock DBLock; // Um lock único - TODO: substituir por lógica "sofisticada"

    public ReadersWriters(int numReaders) {
        this.numReaders = numReaders;
        this.DB = new ArrayList<String>();
        this.initDB();
        this.DBLock = new ReentrantLock();
        this.threadObjs = new ArrayList<Thread>();
        this.initThreads();
    }

    public static void main(String[] args) {
        try (PrintWriter log = new PrintWriter(new FileWriter("log-blocking.txt"))) {
            for (int i = 0; i < 100; i++) {
                // Armazenando os tempos de cada iteração para calcular a média
                ArrayList<Long> times = new ArrayList<Long>();
                for (int j = 0; j < 50; j++) {
                    ReadersWriters rw = new ReadersWriters(i);
                    long startTime = System.currentTimeMillis();
                    // Iniciando as threads de todos os Leitores e Escritores
                    rw.threadObjs.forEach(t -> t.start());
                    // Esperando elas terminarem
                    rw.threadObjs.forEach(t -> {
                        try {
                            t.join();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    });
                    // Calculando quanto tempo demorou essa brincadeira
                    long deltaTime = System.currentTimeMillis() - startTime;
                    times.add(deltaTime);
                }
                long totalTime = 0;
                for (long t : times) {
                    totalTime += t;
                }
                long meanTime = totalTime / 50;
                String output = String.format("Tempo médio para %d leitores e %d escritores: %dms", i, 100 - i, meanTime);

                log.println(output); // escreve no arquivo de log
                log.flush();
                System.out.println(output); // imprime no console
            }
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo de log: " + e.getMessage());
        }
    }

    private void initDB() {
        String DBPath = Paths.get("bd", "bd.txt").toString();
        try (BufferedReader DBReader = new BufferedReader(new FileReader(DBPath))) {
            String word;
            while ((word = DBReader.readLine()) != null) {
                this.DB.add(word);
            }
        } catch (IOException e) {
            System.err.println("Erro lendo o BD: " + e.getMessage());
        }
    }

    private void initThreads() {
        // Da forma como está, todas as threads recebem o mesmo Lock
        // e portanto nunca compartilham o recurso de verdade
        for (int i = 0; i < ReadersWriters.numObjs; i++) {
            if (i < numReaders) {
                threadObjs.add(new Thread(new Reader(DB, DBLock)));
            } else {
                threadObjs.add(new Thread(new Writer(DB, DBLock)));
            }
            Collections.shuffle(threadObjs);
        }
    }
}
