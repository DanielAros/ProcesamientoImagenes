
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
    private JLabel contenedorImgOriginal, contenedorImgProcesada, histograma1, histograma2;
    private JButton btnNegativo, btnGris, btnBrillo;

    double[][] back;
    double n = 0;
    private String PATH;

    public Ventana(String PATH){
        super("Procesamiento de imagenes");
        this.PATH = PATH;
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

        imagenOriginal = new ImageIcon(PATH);
//        imagenProcesada = new ImageIcon("C:\\Users\\daros\\OneDrive\\Escritorio\\Fotos\\bob.png");


        //IMAGEN ORIGINAL
        contenedorImgOriginal = new JLabel(imagenOriginal);
        contenedorImgOriginal.setBounds(100, 100, imagenOriginal.getIconWidth(), imagenOriginal.getIconHeight());
        //HISTOGRAMA
        histograma1 = new JLabel();
        histograma1.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(0, 0, 0)));
        histograma1.setBounds(100, 400, 200, 50);
        contentPanel.add(histograma1);


        //IMAGEN PROCESADA
        contenedorImgProcesada = new JLabel();
        contenedorImgProcesada.setBounds(400, 100, imagenOriginal.getIconWidth(), imagenOriginal.getIconHeight());

        contentPanel.add(contenedorImgOriginal);
        contentPanel.add(contenedorImgProcesada);


        //BOTONES
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
        
        btnBrillo = new JButton("Binarizacion");
        btnBrillo.setBounds(460, 500, 100, 20);
        btnBrillo.addActionListener(this);
        contentPanel.add(btnBrillo);
        
        btnBrillo = new JButton("Umbral");
        btnBrillo.setBounds(580, 500, 100, 20);
        btnBrillo.addActionListener(this);
        contentPanel.add(btnBrillo);
        



        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println(e.paramString());
//        contenedorImgProcesada.setIcon(imagenOriginal);
        BufferedImage imgN = null;
        try {
            imgN = ImageIO.read(new File(PATH));
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
            
        }if(e.paramString().indexOf("Binarizacion") != -1){
            
            int umbral = 128;
            for (int y = 0; y < imgN.getHeight(); y++) {
                for (int x = 0; x < imgN.getWidth(); x++) {
                    // Obtener el color del pixel original
                    Color colorOriginal = new Color(imgN.getRGB(x, y));

                    // Calcular la luminosidad del pixel (0-255)
                    int luminosidad = (int) (0.2126 * colorOriginal.getRed() + 0.7152 * colorOriginal.getGreen() + 0.0722 * colorOriginal.getBlue());

                    // Asignar el color blanco o negro según el umbral
                    if (luminosidad > umbral) {
                        imgN.setRGB(x, y, Color.WHITE.getRGB());
                    } else {
                        imgN.setRGB(x, y, Color.BLACK.getRGB());
                    }
                }
            }
            
            System.out.println("Imagen -> Binarizacion");
            ImageIcon prueba = new ImageIcon(imgN);
            
            contenedorImgProcesada.setIcon(prueba);
            
        }if(e.paramString().indexOf("Umbral") != -1){
            
        	int threshold = 20; // Umbral predefinido
        	for (int y = 0; y < imgN.getHeight(); y++) {
        	    for (int x = 0; x < imgN.getWidth(); x++) {
        	        int rgb = imgN.getRGB(x, y);
        	        int r = (rgb >> 16) & 0xFF;
        	        int g = (rgb >> 8) & 0xFF;
        	        int b = rgb & 0xFF;
        	        int gray = (int) (0.299 * r + 0.587 * g + 0.114 * b);
        	        int value = gray > threshold ? 255 : 0;
        	        imgN.setRGB(x, y, (value << 16) | (value << 8) | value);
        	    }
        	}
            
            System.out.println("Imagen -> Umbral");
            ImageIcon prueba = new ImageIcon(imgN);
            
            contenedorImgProcesada.setIcon(prueba);
            
        } else if(e.paramString().indexOf("Brillo") != -1){
            System.out.println("Brillo");

            BufferedImage imgB = null;
            try {
                imgB = ImageIO.read(new File(PATH));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            BufferedImage finalImgB = imgB;
            btnBrillo.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent l) {
                    if(l.getKeyChar() == '-'){
                        n++;
                    }

                    if(l.getKeyChar() == '+'){
                        n--;
                    }
                    if (n > 0){
                        for (int i = 0; i < ancho; i++){
                            for (int j = 0; j < alto; j++){
                                double r = Math.round((Math.pow(((mr[i][j])/255.0), n))*255.0);
                                double g = Math.round((Math.pow(((mg[i][j])/255.0), n))*255.0);
                                double b = Math.round((Math.pow(((mb[i][j])/255.0), n))*255.0);
                                double brillop = (r*65536)+(g*256)+(b);;
                                finalImgB.setRGB(i, j, (int)brillop);
                            }
                        }
                    }
                    if (n < 0){
                        for (int i = 0; i < ancho; i++){
                            for (int j = 0; j < alto; j++){
                                double r = Math.round((Math.pow(((mr[i][j])/255.0), 1/(Math.abs(n))))*255.0);
                                double g = Math.round((Math.pow(((mg[i][j])/255.0), 1/(Math.abs(n))))*255.0);
                                double b = Math.round((Math.pow(((mb[i][j])/255.0), 1/(Math.abs(n))))*255.0);
                                double brillop = (r*65536)+(g*256)+(b);
                                finalImgB.setRGB(i, j, (int)brillop);
                            }
                        }
                    }
                    if (n == 0){
                        for (int i = 0; i < ancho; i++){
                            for (int j = 0; j < alto; j++){
                                finalImgB.setRGB(i, j, (int)back[i][j]);}
                        }
                    }
                    contenedorImgProcesada.repaint();
                    System.out.println("Factor de brillo: " + n);
                }
            });
            ImageIcon prueba = new ImageIcon(finalImgB);

            contenedorImgProcesada.setIcon(prueba);
        }
        
        
    }
}