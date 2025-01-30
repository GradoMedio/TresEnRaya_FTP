package Cliente;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

/*
    https://es.stackoverflow.com/questions/422067/cargar-imagen-al-hacer-clic-en-boton


 */
public class Cliente extends JDialog {
    private JPanel contentPane;
    private JButton b00;
    private JButton b01;
    private JButton b02;
    private JButton b10;
    private JButton b11;
    private JButton b12;
    private JButton b20;
    private JButton b21;
    private JButton b22;
    private  ImageIcon cruz;
    private  ImageIcon circulo;
    private Socket cliente;
    private boolean jugador;




    public Cliente() {
        setContentPane(contentPane);
        setModal(false);

        b00.setName("0/0");
        b01.setName("0/1");
        b02.setName("0/2");
        b10.setName("1/0");
        b11.setName("1/1");
        b12.setName("1/2");
        b20.setName("2/0");
        b21.setName("2/1");
        b22.setName("2/2");

        try {
            cliente = new Socket("localhost",22222);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cruz = new ImageIcon(getClass().getClassLoader().getResource("Fotos/cruz.png"));
        circulo = new ImageIcon(getClass().getClassLoader().getResource("Fotos/circulo.png"));
        b00.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                click(b00);
            }
        });

        b01.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                click(b01);
            }
        });


        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void click(JButton b){
        try {
            ObjectOutputStream salida = new ObjectOutputStream(cliente.getOutputStream());
            if(jugador){
                System.out.println(b.getName());
            }

            salida.writeUTF(b.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        Cliente dialog = new Cliente();
        dialog.pack();
        dialog.setVisible(true);
        //System.exit(0);
        try {
            ObjectInputStream entrada= new ObjectInputStream(dialog.cliente.getInputStream());
            String accion = entrada.readUTF();
            dialog.jugador = entrada.readBoolean();
            Integer tablero [][];
            while(accion.equals("/Fin")){
                accion = entrada.readUTF();

                if(accion.equals("/Correcto")) {
                    tablero = (Integer[][]) entrada.readObject();

                    for (int i = 0; i < tablero.length; i++) {
                        for (int j = 0; j < tablero[i].length; j++) {
                            if (tablero[i][j] == 1) {
                                dialog.b00.setIcon(dialog.cruz);
                            } else if (tablero[i][j] == 2) {
                                dialog.b00.setIcon(dialog.circulo);
                            }
                        }
                    }
                }else{
                    System.out.println("EORRR");
                }

                accion = entrada.readUTF();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
