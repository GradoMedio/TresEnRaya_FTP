package Servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) {
        try {
            ServerSocket servidor = new ServerSocket(22222);

            while(true){
                Integer [][] tablero = new Integer[3][3];
                for (int i = 0; i < tablero.length; i++) {
                    for (int j = 0; j < tablero[i].length; j++) {
                        tablero[i][j] = 0;
                    }
                }

                Socket cliente = servidor.accept();
                Thread hilo = new Thread(new Hilo(cliente,true,tablero));
                hilo.start();
                cliente = servidor.accept();
                Hilo h = new Hilo(cliente,false,tablero);
                h.run();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
