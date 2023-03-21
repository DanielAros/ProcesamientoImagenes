import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Ventana extends JFrame implements ActionListener {
    private JPanel contentPanel;
    private ImageIcon imagenOriginal, imagenProcesada;
    private JLabel contenedorImgOriginal, contenedorImgProcesada;
    private JButton btnGris;

    public Ventana(){
        super("Procesamiento de imagenes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Asignamos la posicion inicial y las dimensiones de la ventana.
        setBounds(250, 100, 1000, 600);

        //Creamos el contenedor dentro del JFrame para despues agregar todos los elementos que tendra la interfaz
        contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.gray);
        setContentPane(contentPanel);

        imagenOriginal = new ImageIcon("C:\\Users\\daros\\OneDrive\\Escritorio\\Fotos\\bob.png");
        imagenProcesada = new ImageIcon("C:\\Users\\daros\\OneDrive\\Escritorio\\Fotos\\bob.png");

        contenedorImgOriginal = new JLabel(imagenOriginal);
        contenedorImgOriginal.setBounds(100, 100, imagenOriginal.getIconWidth(), imagenOriginal.getIconHeight());

        contenedorImgProcesada = new JLabel();
        contenedorImgProcesada.setBounds(600, 100, imagenProcesada.getIconWidth(), imagenProcesada.getIconHeight());

        contentPanel.add(contenedorImgOriginal);
        contentPanel.add(contenedorImgProcesada);

        btnGris = new JButton("Gris");
        btnGris.setBounds(100, 500, 100, 20);
        btnGris.addActionListener(this);
        contentPanel.add(btnGris);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
//        contenedorImgProcesada.setIcon(imagenOriginal);
        BufferedImage imgN = null;
        try {
            imgN = ImageIO.read(new File("C:\\Users\\daros\\OneDrive\\Escritorio\\Fotos\\bob.png"));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        int ancho = imgN.getWidth();
        int alto = imgN.getHeight();

        double[][] m = new double[ancho][alto];
        int[][] mr = new int[ancho][alto];
        int[][] mg = new int[ancho][alto];
        int[][] mb = new int[ancho][alto];
        for (int i = 0; i < ancho; i++){
            for (int j = 0; j < alto; j++){
                m[i][j] = imgN.getRGB(i, j); // RGB = (R*65536)+(G*256)+B , (when R is RED, G is GREEN and B is BLUE)
                mr[i][j] = ((int)m[i][j]>> 16) & 0x000000FF;
                mg[i][j] = ((int)m[i][j]>> 8) & 0x000000FF;
                mb[i][j] = ((int)m[i][j]) & 0x000000FF;
            }
        }

        for (int i = 0; i < imgN.getWidth(); i++){
            for (int j = 0; j < imgN.getHeight(); j++){
                double r = 255-mr[i][j];
                double g = 255-mg[i][j];
                double b = 255-mb[i][j];
                double neg = (r*65536)+(g*256)+(b);
                imgN.setRGB(i, j, (int)neg);
            }
        }
//        printnegativo(ancho, alto, imgN, "Negativo");
        System.out.println("Imagen -> Negativo");

        ImageIcon prueba = new ImageIcon(imgN);

        contenedorImgProcesada.setIcon(prueba);
    }
}
