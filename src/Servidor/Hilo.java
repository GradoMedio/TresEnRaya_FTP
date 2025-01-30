package Servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Hilo implements Runnable{
    private Socket cliente;
    private boolean jugador;
    private Integer [][] tablero;

    public Hilo(Socket cliente,boolean jugador, Integer[][] tablero) {
        this.cliente = cliente;
        this.tablero = tablero;
        this.jugador = jugador;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream salida = new ObjectOutputStream(cliente.getOutputStream());
            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());

            salida.writeUTF("/Correcto");
            salida.writeBoolean(jugador);
            salida.writeObject(tablero);

            boolean fin = false;
            String pos;
            Integer sep [] = new Integer[2];
            while(!fin){
                pos = entrada.readUTF();
                sep[0] = Integer.parseInt(pos.split("/")[0]);
                sep[1] = Integer.parseInt(pos.split("/")[1]);

                if(tablero[sep[0]][sep[1]] == 0){
                    salida.writeUTF("/Correcto");
                    if(jugador){
                        tablero[sep[0]][sep[1]] = 1;
                    }else{
                        tablero[sep[0]][sep[1]] = 2;
                    }
                    salida.writeObject(tablero);
                }else{
                    salida.writeUTF("/Ocupado");
                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
