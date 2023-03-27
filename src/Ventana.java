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
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;


public class Ventana extends JFrame implements ActionListener {
    private JPanel contentPanel;
    private ImageIcon imagenOriginal, imagenProcesada;
    private JLabel contenedorImgOriginal, contenedorImgProcesada, histograma1, histograma2;
    private JButton btnNegativo, btnGris, btnBrillo, btnBinarizacion, btnUmbral, btnComposicion, btnSuma, btnResta, btnReiniciar, btnAtras;

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
        setBounds(50, 50, 1400, 900);

        //Creamos el contenedor dentro del JFrame para despues agregar todos los elementos que tendra la interfaz
        contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.gray);
        setContentPane(contentPanel);

        imagenOriginal = new ImageIcon(PATH);
//        imagenProcesada = new ImageIcon("C:\\Users\\daros\\OneDrive\\Escritorio\\Fotos\\bob.png");

        //IMAGEN ORIGINAL
        contenedorImgOriginal = new JLabel(imagenOriginal);
        contenedorImgOriginal.setBounds(50, 50, imagenOriginal.getIconWidth(), imagenOriginal.getIconHeight());

        //HISTOGRAMA
        histograma1 = new JLabel();
        histograma1.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(0, 0, 0)));
        histograma1.setBounds(50, 550, imagenOriginal.getIconWidth(), 100);
        contentPanel.add(histograma1);

        //HISTOGRAMA 2
        histograma2 = new JLabel();
        histograma2.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(0, 0, 0)));
        histograma2.setBounds(700, 550, imagenOriginal.getIconWidth(), 100);
        contentPanel.add(histograma2);

        //IMAGEN PROCESADA
        contenedorImgProcesada = new JLabel();
        contenedorImgProcesada.setBounds(700, 50, imagenOriginal.getIconWidth(), imagenOriginal.getIconHeight());

        contentPanel.add(contenedorImgOriginal);
        contentPanel.add(contenedorImgProcesada);


        //BOTONES
        btnNegativo = new JButton("Negativo");
        btnNegativo.setBounds(50, 700, 100, 20);
        btnNegativo.addActionListener(this);
        contentPanel.add(btnNegativo);

        btnGris = new JButton("Gris");
        btnGris.setBounds(170, 700, 100, 20);
        btnGris.addActionListener(this);
        contentPanel.add(btnGris);

        btnBrillo = new JButton("Brillo");
        btnBrillo.setBounds(290, 700, 100, 20);
        btnBrillo.addActionListener(this);
        contentPanel.add(btnBrillo);

        btnBinarizacion = new JButton("Binarizacion");
        btnBinarizacion.setBounds(410, 700, 100, 20);
        btnBinarizacion.addActionListener(this);
        contentPanel.add(btnBinarizacion);

        btnUmbral = new JButton("Umbral");
        btnUmbral.setBounds(530, 700, 100, 20);
        btnUmbral.addActionListener(this);
        contentPanel.add(btnUmbral);

        btnComposicion = new JButton("Composicion");
        btnComposicion.setBounds(650, 700, 100, 20);
        btnComposicion.addActionListener(this);
        contentPanel.add(btnComposicion);

        btnSuma = new JButton("Suma");
        btnSuma.setBounds(770, 700, 100, 20);
        btnSuma.addActionListener(this);
        contentPanel.add(btnSuma);

        btnResta = new JButton("Resta");
        btnResta.setBounds(900, 700, 100, 20);
        btnResta.addActionListener(this);
        contentPanel.add(btnResta);

        btnAtras = new JButton("Atras");
        btnAtras.setBounds(1020, 700, 100, 20);
        btnAtras.addActionListener(this);
        contentPanel.add(btnAtras);

        btnReiniciar = new JButton("Reiniciar");
        btnReiniciar.setBounds(1140, 700, 100, 20);
        btnReiniciar.addActionListener(this);
        contentPanel.add(btnReiniciar);

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
            contenedorImgOriginal.setIcon(imagenOriginal);
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

        //Se obtienen los valores de RGB, se hace una suma ponderada de estos valores y si es mayor a 128 el pixel se establece en blanco, sino se establece en negro
        }else if(e.paramString().indexOf("Binarizacion") != -1){
            System.out.println("Binarización");

            BufferedImage imgBin = null;
            try {
                imgBin = ImageIO.read(new File(PATH));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            BufferedImage bin = new BufferedImage(ancho, alto, BufferedImage.TYPE_BYTE_BINARY);

            for (int i = 0; i < ancho; i++) {
                for (int j = 0; j < alto; j++) {
                    Color c = new Color(imgBin.getRGB(i, j));
                    double r = (c.getRed() * 0.299);
                    double g = (c.getGreen() * 0.587);
                    double b = (c.getBlue() * 0.114);
                    double suma = r + g + b;
                    if (suma > 128) {
                        bin.setRGB(i, j, Color.WHITE.getRGB());
                    } else {
                        bin.setRGB(i, j, Color.BLACK.getRGB());
                    }
                }
            }
            ImageIcon prueba = new ImageIcon(bin);

            contenedorImgProcesada.setIcon(prueba);

            // Segmentación por Umbrales
        }else if(e.paramString().indexOf("Umbral") != -1){
            System.out.println("Umbral");

            BufferedImage imgUmb = null;
            try {
                imgUmb = ImageIO.read(new File(PATH));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            BufferedImage umb = new BufferedImage(ancho, alto, BufferedImage.TYPE_BYTE_BINARY);

            int umbral1 = 120; // Umbral 1
            int umbral2 = 180; // Umbral 2
            for (int i = 0; i < ancho; i++) {
                for (int j = 0; j < alto; j++) {
                    Color c = new Color(imgUmb.getRGB(i, j));
                    double red = c.getRed();
                    double green = c.getGreen();
                    double blue = c.getBlue();
                    double grayLevel = (red + green + blue) / 3;
                    if (grayLevel < umbral1 || grayLevel > umbral2) {
                        umb.setRGB(i, j, Color.WHITE.getRGB());
                    } else {
                        umb.setRGB(i, j, Color.BLACK.getRGB());
                    }
                }
            }
            ImageIcon prueba = new ImageIcon(umb);

            contenedorImgProcesada.setIcon(prueba);
        }else if(e.paramString().indexOf("Composicion") != -1){
            System.out.println("Composición");

            BufferedImage imgComp = null;
            try {
                imgComp = ImageIO.read(new File(PATH));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            Rectangle rect = contenedorImgProcesada.getBounds();
            int x = rect.x + 58;
            int y = rect.y + 104;
            rect.setLocation(x, y);

            Robot robot = null;
            try {
                robot = new Robot();

            } catch (AWTException ex) {
                throw new RuntimeException(ex);
            }

            System.out.println(rect);
            BufferedImage imgCaptura = robot.createScreenCapture(rect);

            BufferedImage imgProcesada = imgCaptura.getSubimage(0, 0, rect.width, rect.height);

            BufferedImage imgResultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

            // Dibujar la imagen1 en la imagen de destino
            Graphics2D g2d = imgResultado.createGraphics();
            g2d.drawImage(imgComp, 0, 0, null);

            // Dibujar la imagen2 en la imagen de destino con una opacidad del 50%
            g2d.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, 0.5f));
            g2d.drawImage(imgProcesada, 0, 0, null);
            g2d.dispose();

            ImageIcon prueba = new ImageIcon(imgResultado);

            contenedorImgProcesada.setIcon(prueba);
        }else if(e.paramString().indexOf("Suma") != -1){
            System.out.println("Suma");

            BufferedImage imgSum = null;
            try {
                imgSum = ImageIO.read(new File(PATH));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            Rectangle rect = contenedorImgProcesada.getBounds();
            int x = rect.x + 58;
            int y = rect.y + 104;
            rect.setLocation(x, y);

            Robot robot = null;
            try {
                robot = new Robot();

            } catch (AWTException ex) {
                throw new RuntimeException(ex);
            }

            System.out.println(rect);
            BufferedImage imgCaptura = robot.createScreenCapture(rect);

            BufferedImage imgProcesada = imgCaptura.getSubimage(0, 0, ancho, alto);

            BufferedImage imgResultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

            for(int i = 0; i < ancho; i++){
                for(int j = 0; j < alto; j++){
                    int valorPixel1 = imgSum.getRGB(i, j);
                    int valorPixel2 = imgProcesada.getRGB(i, j);
                    int valorResultado = sumarValoresPixel(valorPixel1, valorPixel2);

                    imgResultado.setRGB(i, j, valorResultado);
                }
            }
            ImageIcon prueba = new ImageIcon(imgResultado);

            contenedorImgProcesada.setIcon(prueba);


        }else if(e.paramString().indexOf("Resta") != -1){
            System.out.println("Resta");

            BufferedImage imgRes = null;
            try {
                imgRes = ImageIO.read(new File(PATH));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            Rectangle rect = contenedorImgProcesada.getBounds();
            int x = rect.x + 58;
            int y = rect.y + 104;
            rect.setLocation(x, y);

            Robot robot = null;
            try {
                robot = new Robot();

            } catch (AWTException ex) {
                throw new RuntimeException(ex);
            }

            System.out.println(rect);
            BufferedImage imgCaptura = robot.createScreenCapture(rect);

            BufferedImage imgProcesada = imgCaptura.getSubimage(0, 0, ancho, alto);

            BufferedImage imgResultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

            for(int i = 0; i < ancho; i++){
                for(int j = 0; j < alto; j++){

                    Color color1 = new Color(imgRes.getRGB(i, j));
                    Color color2 = new Color(imgProcesada.getRGB(i, j));

                    int r = Math.max(color1.getRed() - color2.getRed(), 0);
                    int g = Math.max(color1.getGreen() - color2.getGreen(), 0);
                    int b = Math.max(color1.getBlue() - color2.getBlue(), 0);
                    Color nuevoColor = new Color(r, g, b);

                    imgResultado.setRGB(i, j, nuevoColor.getRGB());
                }
            }
            ImageIcon prueba = new ImageIcon(imgResultado);

            contenedorImgProcesada.setIcon(prueba);

            //Reiniciar
        } else if (e.paramString().indexOf("Atras") != -1) {
            
        } else if(e.paramString().indexOf("Reiniciar") != -1){


        }
    }

    public static int sumarValoresPixel(int valorPixel1, int valorPixel2) {
        int alpha = (valorPixel1 >> 24) & 0xff;
        int red = (valorPixel1 >> 16) & 0xff;
        int green = (valorPixel1 >> 8) & 0xff;
        int blue = valorPixel1 & 0xff;

        int alpha2 = (valorPixel2 >> 24) & 0xff;
        int red2 = (valorPixel2 >> 16) & 0xff;
        int green2 = (valorPixel2 >> 8) & 0xff;
        int blue2 = valorPixel2 & 0xff;

        int alphaResultado = Math.min(255, alpha + alpha2);
        int redResultado = Math.min(255, red + red2);
        int greenResultado = Math.min(255, green + green2);
        int blueResultado = Math.min(255, blue + blue2);

        return (alphaResultado << 24) | (redResultado << 16) | (greenResultado << 8) | blueResultado;
    }
}
