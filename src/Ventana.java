import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Ventana extends JFrame implements ActionListener {
    private JPanel contentPanel;
    private ImageIcon imagenOriginal, imagenProcesada;
    private JLabel contenedorImgOriginal, contenedorImgProcesada;
    private JButton btnNegativo, btnGris, btnBrillo;

    private double n = 0;
    private double[][] back;
    public Ventana(){
        super("Procesamiento de imagenes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crea la barra de menú
        JMenuBar barraMenu = new JMenuBar();

        // Crea los menús
        JMenu menuPuntual = new JMenu("Procesamiento puntual");
        JMenu menuEspacial = new JMenu("Procesamiento espacial");

        // Crea los items del menú Puntual
        JMenuItem nuevoItem = new JMenuItem("item1");

        // Agrega los items al menú Archivo
        menuPuntual.add(nuevoItem);

        // Agrega los menús a la barra de menú
        barraMenu.add(menuPuntual);
        barraMenu.add(menuEspacial);

        setJMenuBar(barraMenu);

        //Asignamos la posicion inicial y las dimensiones de la ventana.
        setBounds(250, 100, 700, 600);

        //Creamos el contenedor dentro del JFrame para despues agregar todos los elementos que tendra la interfaz
        contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.gray);
        setContentPane(contentPanel);

        imagenOriginal = new ImageIcon("C:\\Users\\daros\\OneDrive\\Escritorio\\Fotos\\bob.png");
//        imagenProcesada = new ImageIcon("C:\\Users\\daros\\OneDrive\\Escritorio\\Fotos\\bob.png");

        contenedorImgOriginal = new JLabel(imagenOriginal);
        contenedorImgOriginal.setBounds(100, 100, imagenOriginal.getIconWidth(), imagenOriginal.getIconHeight());

        contenedorImgProcesada = new JLabel();
        contenedorImgProcesada.setBounds(400, 100, imagenOriginal.getIconWidth(), imagenOriginal.getIconHeight());

        contentPanel.add(contenedorImgOriginal);
        contentPanel.add(contenedorImgProcesada);

        btnNegativo = new JButton("Negativo");
        btnNegativo.setBounds(100, 500, 100, 20);
        btnNegativo.addActionListener(this);
        contentPanel.add(btnNegativo);

        btnGris = new JButton("Gris");
        btnGris.setBounds(220, 500, 100, 20);
        btnGris.addActionListener(this);
        contentPanel.add(btnGris);

        btnBrillo = new JButton("Brillo");
        btnBrillo.setBounds(340, 500, 100, 20);
        btnBrillo.addActionListener(this);
        contentPanel.add(btnBrillo);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println(e.paramString());
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

        back = m;

        if(e.paramString().indexOf("Negativo") != -1){
            for (int i = 0; i < imgN.getWidth(); i++){
                for (int j = 0; j < imgN.getHeight(); j++){
                    double r = 255-mr[i][j];
                    double g = 255-mg[i][j];
                    double b = 255-mb[i][j];
                    double neg = (r*65536)+(g*256)+(b);
                    imgN.setRGB(i, j, (int)neg);
                }
            }

            System.out.println("Imagen -> Negativo");

            ImageIcon prueba = new ImageIcon(imgN);

            contenedorImgProcesada.setIcon(prueba);
        } else if (e.paramString().indexOf("Gris") != -1) {
            System.out.println("Gris");
            double[][] mG = new double[ancho][alto];
            int[][] mrgbG = new int[ancho][alto];

            for (int i = 0; i < ancho; i++){
                for (int j = 0; j < alto; j++){
                    double rgb = (mr[i][j]+mg[i][j]+mb[i][j])/3;
                    double gris = (rgb*65536)+(rgb*256)+(rgb);
                    imgN.setRGB(i, j, (int)gris);
                }
            }

            for (int i = 0; i < ancho; i++){
                for (int j = 0; j < alto; j++){
                    mG[i][j] = imgN.getRGB(i, j);
                    mrgbG[i][j] = ((int)m[i][j]) & 0x000000FF;
                }
            }

            ImageIcon prueba = new ImageIcon(imgN);

            contenedorImgProcesada.setIcon(prueba);
        } else if(e.paramString().indexOf("Brillo") != -1){
            System.out.println("Brillo");

            BufferedImage imagenModificada = new BufferedImage(imgN.getWidth(), imgN.getHeight(), imgN.getType());
            Color colorPixel;

            for (int i = 0; i < imgN.getWidth(); i++) {
                for (int j = 0; j < imgN.getHeight(); j++) {
                    // Obtener el color del pixel en (i,j)
                    colorPixel = new Color(imgN.getRGB(i, j));

                    // Ajustar el brillo del pixel sumando o restando el valor dado
                    int r = Math.min(255, Math.max(0, colorPixel.getRed() + -200));
                    int g = Math.min(255, Math.max(0, colorPixel.getGreen() + -200));
                    int b = Math.min(255, Math.max(0, colorPixel.getBlue() + -200));

                    // Crear el nuevo color del pixel
                    Color nuevoColor = new Color(r, g, b);

                    // Establecer el nuevo color del pixel en la imagen modificada
                    imagenModificada.setRGB(i, j, nuevoColor.getRGB());
                }
            }

            ImageIcon prueba = new ImageIcon(imagenModificada);

            contenedorImgProcesada.setIcon(prueba);
        }
    }
}
