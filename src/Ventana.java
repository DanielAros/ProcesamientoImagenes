import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.Kernel;
import java.nio.Buffer;
import java.nio.file.Path;




public class Ventana extends JFrame implements ActionListener {
    private JPanel contentPanel;
    private ImageIcon imagenOriginal, imagenProcesada;
    private JLabel contenedorImgOriginal, contenedorImgProcesada, histograma1, histograma2;
    private JButton btnNegativo, btnGris, btnBrillo, btnBinarizacion, btnUmbral, btnComposicion, btnSuma, btnResta, btnReiniciar, btnAtras, btnConvolucion, btnBordes;

    double[][] back;
    double n = 0;
    private String PATH;
    private JPanel panel1;
    private int ancho, alto;
    private double[][] m;
    private int[][] mr, mg, mb;

    private int contador = 0;

    public Ventana(String PATH) throws IOException {
        super("Procesamiento de imagenes");
        this.PATH = PATH;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Asignamos la posicion inicial y las dimensiones de la ventana.
        setBounds(50, 30, 1400, 740);


        //Creamos el contenedor dentro del JFrame para despues agregar todos los elementos que tendra la interfaz
        contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(Color.gray);
        setContentPane(contentPanel);

        inicializacionMenu();


        imagenOriginal = new ImageIcon(PATH);

        //IMAGEN ORIGINAL
        contenedorImgOriginal = new JLabel(imagenOriginal);
        contenedorImgOriginal.setBounds(50, 50, imagenOriginal.getIconWidth(), imagenOriginal.getIconHeight());

        //Creacion del histograma para la imagen original
        BufferedImage imagen = ImageIO.read(new File(PATH));
        Histograma histograma = new Histograma(imagen);

        //Se define donde va a estar el histograma de la imagen izquierda
        contentPanel.add(histograma).setBounds(50, 450, imagenOriginal.getIconWidth(), 100);

        //Se asigna el espacio para la imagen a procesar.
        contenedorImgProcesada = new JLabel();
        contenedorImgProcesada.setBounds(700, 50, imagenOriginal.getIconWidth(), imagenOriginal.getIconHeight());

        contentPanel.add(contenedorImgOriginal);
        contentPanel.add(contenedorImgProcesada);


        //BOTONES
       crearBotones();

        setVisible(true);
    }
    private void inicializacionMenu(){
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
    }
    private void crearBotones(){
        btnNegativo = new JButton("Negativo");
        btnNegativo.setBounds(50, 600, 100, 20);
        btnNegativo.addActionListener(this);
        contentPanel.add(btnNegativo);

        btnConvolucion = new JButton("Convolucion");
        btnConvolucion.setBounds(50, 630, 100, 20);
        btnConvolucion.addActionListener(this);
        contentPanel.add(btnConvolucion);

        btnGris = new JButton("Gris");
        btnGris.setBounds(170, 600, 100, 20);
        btnGris.addActionListener(this);
        contentPanel.add(btnGris);

        btnBrillo = new JButton("Brillo");
        btnBrillo.setBounds(290, 600, 100, 20);
        btnBrillo.addActionListener(this);
        contentPanel.add(btnBrillo);

        btnBinarizacion = new JButton("Binarizacion");
        btnBinarizacion.setBounds(410, 600, 100, 20);
        btnBinarizacion.addActionListener(this);
        contentPanel.add(btnBinarizacion);

        btnUmbral = new JButton("Umbral");
        btnUmbral.setBounds(530, 600, 100, 20);
        btnUmbral.addActionListener(this);
        contentPanel.add(btnUmbral);

        btnComposicion = new JButton("Composicion");
        btnComposicion.setBounds(650, 600, 100, 20);
        btnComposicion.addActionListener(this);
        contentPanel.add(btnComposicion);

        btnSuma = new JButton("Suma");
        btnSuma.setBounds(770, 600, 100, 20);
        btnSuma.addActionListener(this);
        contentPanel.add(btnSuma);

        btnResta = new JButton("Resta");
        btnResta.setBounds(900, 600, 100, 20);
        btnResta.addActionListener(this);
        contentPanel.add(btnResta);

        btnBordes = new JButton("Bordes");
        btnBordes.setBounds(1020, 600, 100, 20);
        btnBordes.addActionListener(this);
        contentPanel.add(btnBordes);

        btnAtras = new JButton("Atras");
        btnAtras.setBounds(1140, 600, 100, 20);
        btnAtras.addActionListener(this);
        contentPanel.add(btnAtras);

        btnReiniciar = new JButton("Reiniciar");
        btnReiniciar.setBounds(1280, 600, 100, 20);
        btnReiniciar.addActionListener(this);
        contentPanel.add(btnReiniciar);
    }


    public void actionPerformed(ActionEvent e) {
        System.out.println(e.paramString());
//        contenedorImgProcesada.setIcon(imagenOriginal);
        BufferedImage imgN;
        try {
            if(contador == 0){
                imgN = null;
                System.out.println("entro");
                imgN = ImageIO.read(new File(PATH));
                contador++;
            }else{
                System.out.println("contador+1");
                contenedorImgOriginal.setIcon(imagenProcesada);
                Image imagen1 = imagenProcesada.getImage();
                BufferedImage imagenProcesar = (BufferedImage) imagen1;
                Histograma histograma = new Histograma(imagenProcesar);
                contentPanel.add(histograma).setBounds(50, 450, imagenOriginal.getIconWidth(), 100);
                // Obtener la imagen del ImageIcon
                java.awt.Image image = imagenProcesada.getImage();

                // Crear un BufferedImage con las dimensiones de la imagen
                BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

                // Obtener el contexto gráfico del BufferedImage
                Graphics2D g2d = bufferedImage.createGraphics();

                // Dibujar la imagen en el contexto gráfico
                g2d.drawImage(image, 0, 0, null);

                // Liberar los recursos del contexto gráfico
                g2d.dispose();

                imgN = bufferedImage;
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        ancho = imgN.getWidth();
        alto = imgN.getHeight();

        m = new double[ancho][alto];
        mr = new int[ancho][alto];
        mg = new int[ancho][alto];
        mb = new int[ancho][alto];

        for (int i = 0; i < ancho; i++) {
            for (int j = 0; j < alto; j++) {
                m[i][j] = imgN.getRGB(i, j); // RGB = (R*65536)+(G*256)+B , (when R is RED, G is GREEN and B is BLUE)
                mr[i][j] = ((int) m[i][j] >> 16) & 0x000000FF;
                mg[i][j] = ((int) m[i][j] >> 8) & 0x000000FF;
                mb[i][j] = ((int) m[i][j]) & 0x000000FF;
            }
        }
        back = m;

        if (e.paramString().indexOf("Negativo") != -1) {
            filtroNegativo(imgN);
        } else if (e.paramString().indexOf("Gris") != -1) {
            filtroGris(imgN);
        } else if (e.paramString().indexOf("Brillo") != -1) {
            filtroBrillo(imgN);
        } else if (e.paramString().indexOf("Binarizacion") != -1) {
            //Se obtienen los valores de RGB, se hace una suma ponderada de estos valores y si es mayor a 128 el pixel se establece en blanco, sino se establece en negro
           filtroBinzarizacion(imgN);
        } else if (e.paramString().indexOf("Umbral") != -1) {
            // Segmentación por Umbrales
            filtroUmbral(imgN);
        } else if (e.paramString().indexOf("Composicion") != -1) {
            filtroComposicion(imgN);
        } else if (e.paramString().indexOf("Suma") != -1) {
            filtroSuma(imgN);
        } else if (e.paramString().indexOf("Resta") != -1) {
            filtroResta(imgN);

        }else if (e.paramString().indexOf("Bordes") != -1) {
            bordes(imgN);
        } else if (e.paramString().indexOf("Atras") != -1) {

        } else if (e.paramString().indexOf("Reiniciar") != -1) {

        } else if (e.paramString().indexOf("Convolucion") != -1) {
            try {
                filtroConvolucion(imgN);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    //negativo, sumar, desenfoque, bordes, mediana, restar, binzarizacion, binerizacion comp
    //segmentacion, contraste, grises

    private void filtroConvolucion(BufferedImage imgN) throws IOException {
        BufferedImage conv = imgN;

        float[] kernelData = {
                -1, -1, -1,
                -1,  8, -1,
                -1, -1, -1
        };
        Kernel kernel = new Kernel(3, 3, kernelData);

        ConvolveOp convolveOp = new ConvolveOp(kernel);

        BufferedImage prueba = convolveOp.filter(conv, null);

        imagenProcesada = new ImageIcon(prueba);
        contenedorImgProcesada.setIcon(imagenProcesada);

//        contenedorImgProcesada.setIcon(new ImageIcon(prueba));

        llamadaHistograma(imagenProcesada);
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

    private void llamadaHistograma(ImageIcon imagen){
        Image imagen1 = imagen.getImage();
        BufferedImage imagenProcesar = (BufferedImage) imagen1;
        Histograma histograma = new Histograma(imagenProcesar);
        contentPanel.add(histograma).setBounds(700, 450, imagenOriginal.getIconWidth(), 100);
    }

    private void filtroGris(BufferedImage imagen){
        System.out.println("Gris");
        double[][] mG = new double[ancho][alto];
        int[][] mrgbG = new int[ancho][alto];

        for (int i = 0; i < ancho; i++) {
            for (int j = 0; j < alto; j++) {
                double rgb = (mr[i][j] + mg[i][j] + mb[i][j]) / 3;
                double gris = (rgb * 65536) + (rgb * 256) + (rgb);
                imagen.setRGB(i, j, (int) gris);
            }
        }

        for (int i = 0; i < ancho; i++) {
            for (int j = 0; j < alto; j++) {
                mG[i][j] = imagen.getRGB(i, j);
                mrgbG[i][j] = ((int) m[i][j]) & 0x000000FF;
            }
        }

//        ImageIcon prueba = new ImageIcon(imagen);
        imagenProcesada = new ImageIcon(imagen);
        contenedorImgProcesada.setIcon(imagenProcesada);

        llamadaHistograma(imagenProcesada);
    }

    private void filtroNegativo(BufferedImage imagen){
        for (int i = 0; i < imagen.getWidth(); i++) {
            for (int j = 0; j < imagen.getHeight(); j++) {
                double r = 255 - mr[i][j];
                double g = 255 - mg[i][j];
                double b = 255 - mb[i][j];
                double neg = (r * 65536) + (g * 256) + (b);
                imagen.setRGB(i, j, (int) neg);
            }
        }

        System.out.println("Imagen -> Negativo");

//        ImageIcon prueba = new ImageIcon(imagen);
        imagenProcesada = new ImageIcon(imagen);
        llamadaHistograma(imagenProcesada);



        contenedorImgProcesada.setIcon(imagenProcesada);
    }

    private void filtroBrillo(BufferedImage imagen){
        System.out.println("Brillo");

        BufferedImage finalImgB = imagen;
        btnBrillo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent l) {
                if (l.getKeyChar() == '-') {
                    n++;
                }

                if (l.getKeyChar() == '+') {
                    n--;
                }
                if (n > 0) {
                    for (int i = 0; i < ancho; i++) {
                        for (int j = 0; j < alto; j++) {
                            double r = Math.round((Math.pow(((mr[i][j]) / 255.0), n)) * 255.0);
                            double g = Math.round((Math.pow(((mg[i][j]) / 255.0), n)) * 255.0);
                            double b = Math.round((Math.pow(((mb[i][j]) / 255.0), n)) * 255.0);
                            double brillop = (r * 65536) + (g * 256) + (b);
                            ;
                            finalImgB.setRGB(i, j, (int) brillop);
                        }
                    }
                }
                if (n < 0) {
                    for (int i = 0; i < ancho; i++) {
                        for (int j = 0; j < alto; j++) {
                            double r = Math.round((Math.pow(((mr[i][j]) / 255.0), 1 / (Math.abs(n)))) * 255.0);
                            double g = Math.round((Math.pow(((mg[i][j]) / 255.0), 1 / (Math.abs(n)))) * 255.0);
                            double b = Math.round((Math.pow(((mb[i][j]) / 255.0), 1 / (Math.abs(n)))) * 255.0);
                            double brillop = (r * 65536) + (g * 256) + (b);
                            finalImgB.setRGB(i, j, (int) brillop);
                        }
                    }
                }
                if (n == 0) {
                    for (int i = 0; i < ancho; i++) {
                        for (int j = 0; j < alto; j++) {
                            finalImgB.setRGB(i, j, (int) back[i][j]);
                        }
                    }
                }
                filtroBrilloHistograma(finalImgB);
//                contenedorImgProcesada.repaint();
                System.out.println("Factor de brillo: " + n);
            }
        });
        filtroBrilloHistograma(finalImgB);
    }

    private void filtroBrilloHistograma(BufferedImage imagen){
        imagenProcesada = new ImageIcon(imagen);
        contenedorImgProcesada.setIcon(imagenProcesada);
        llamadaHistograma(imagenProcesada);
    }

    private void filtroBinzarizacion(BufferedImage imagen){
        System.out.println("Binarización");

        BufferedImage bin = new BufferedImage(ancho, alto, BufferedImage.TYPE_BYTE_BINARY);

        for (int i = 0; i < ancho; i++) {
            for (int j = 0; j < alto; j++) {
                Color c = new Color(imagen.getRGB(i, j));
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
        imagenProcesada = new ImageIcon(bin);

        contenedorImgProcesada.setIcon(imagenProcesada);
        llamadaHistograma(imagenProcesada);
    }
    private void filtroUmbral(BufferedImage imagen){
        System.out.println("Umbral");

        BufferedImage umb = new BufferedImage(ancho, alto, BufferedImage.TYPE_BYTE_BINARY);

        int umbral1 = 120; // Umbral 1
        int umbral2 = 180; // Umbral 2
        for (int i = 0; i < ancho; i++) {
            for (int j = 0; j < alto; j++) {
                Color c = new Color(imagen.getRGB(i, j));
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
        imagenProcesada = new ImageIcon(umb);

        contenedorImgProcesada.setIcon(imagenProcesada);
        llamadaHistograma(imagenProcesada);
    }

    private void filtroComposicion(BufferedImage imagen){
        System.out.println("Composición");

        BufferedImage imgComp = imagen;

        Rectangle rect = contenedorImgProcesada.getBounds();
        int x = rect.x + 58;
        int y = rect.y + 84;
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

        imagenProcesada = new ImageIcon(imgResultado);

        contenedorImgProcesada.setIcon(imagenProcesada);
        llamadaHistograma(imagenProcesada);
    }

    private void filtroResta(BufferedImage imagen){
        System.out.println("Resta");

        BufferedImage imgRes = imagen;

        Rectangle rect = contenedorImgProcesada.getBounds();
        int x = rect.x + 58;
        int y = rect.y + 84;
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

        for (int i = 0; i < ancho; i++) {
            for (int j = 0; j < alto; j++) {

                Color color1 = new Color(imgRes.getRGB(i, j));
                Color color2 = new Color(imgProcesada.getRGB(i, j));

                int r = Math.max(color1.getRed() - color2.getRed(), 0);
                int g = Math.max(color1.getGreen() - color2.getGreen(), 0);
                int b = Math.max(color1.getBlue() - color2.getBlue(), 0);
                Color nuevoColor = new Color(r, g, b);

                imgResultado.setRGB(i, j, nuevoColor.getRGB());
            }
        }
        imagenProcesada = new ImageIcon(imgResultado);

        contenedorImgProcesada.setIcon(imagenProcesada);
        llamadaHistograma(imagenProcesada);
    }

    private void filtroSuma(BufferedImage imagen){
        System.out.println("Suma");

        BufferedImage imgSum = imagen;

        Rectangle rect = contenedorImgProcesada.getBounds();
        int x = rect.x + 58;
        int y = rect.y + 84;
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

        for (int i = 0; i < ancho; i++) {
            for (int j = 0; j < alto; j++) {
                int valorPixel1 = imgSum.getRGB(i, j);
                int valorPixel2 = imgProcesada.getRGB(i, j);
                int valorResultado = sumarValoresPixel(valorPixel1, valorPixel2);

                imgResultado.setRGB(i, j, valorResultado);
            }
        }
        imagenProcesada = new ImageIcon(imgResultado);

        contenedorImgProcesada.setIcon(imagenProcesada);
        llamadaHistograma(imagenProcesada);
    }

    private void bordes(BufferedImage imagen){
        int width = imagen.getWidth();
        int height = imagen.getHeight();
        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        int[][] sobelX = {
                {-1, 0, 1},
                {-2, 0, 2},
                {-1, 0, 1}
        };

        int[][] sobelY = {
                {-1, -2, -1},
                {0, 0, 0},
                {1, 2, 1}
        };

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int pixelX = (
                        sobelX[0][0] * getGrayLevel(imagen.getRGB(x - 1, y - 1))) +
                        (sobelX[0][1] * getGrayLevel(imagen.getRGB(x, y - 1))) +
                        (sobelX[0][2] * getGrayLevel(imagen.getRGB(x + 1, y - 1))) +
                        (sobelX[1][0] * getGrayLevel(imagen.getRGB(x - 1, y))) +
                        (sobelX[1][1] * getGrayLevel(imagen.getRGB(x, y))) +
                        (sobelX[1][2] * getGrayLevel(imagen.getRGB(x + 1, y))) +
                        (sobelX[2][0] * getGrayLevel(imagen.getRGB(x - 1, y + 1))) +
                        (sobelX[2][1] * getGrayLevel(imagen.getRGB(x, y + 1))) +
                        (sobelX[2][2] * getGrayLevel(imagen.getRGB(x + 1, y + 1)));

                int pixelY = (
                        sobelY[0][0] * getGrayLevel(imagen.getRGB(x - 1, y - 1))) +
                        (sobelY[0][1] * getGrayLevel(imagen.getRGB(x, y - 1))) +
                        (sobelY[0][2] * getGrayLevel(imagen.getRGB(x + 1, y - 1))) +
                        (sobelY[1][0] * getGrayLevel(imagen.getRGB(x - 1, y))) +
                        (sobelY[1][1] * getGrayLevel(imagen.getRGB(x, y))) +
                        (sobelY[1][2] * getGrayLevel(imagen.getRGB(x + 1, y))) +
                        (sobelY[2][0] * getGrayLevel(imagen.getRGB(x - 1, y + 1))) +
                        (sobelY[2][1] * getGrayLevel(imagen.getRGB(x, y + 1))) +
                        (sobelY[2][2] * getGrayLevel(imagen.getRGB(x + 1, y + 1)));

                int magnitude = (int) Math.sqrt((pixelX * pixelX) + (pixelY * pixelY));
                magnitude = Math.min(255, Math.max(0, magnitude)); // Asegurar que el valor esté en el rango 0-255
                resultImage.setRGB(x, y, new Color(magnitude, magnitude, magnitude).getRGB());
            }
        }

        imagenProcesada = new ImageIcon(resultImage);

        contenedorImgProcesada.setIcon(imagenProcesada);
        llamadaHistograma(imagenProcesada);
    }

    public static int getGrayLevel(int rgb) {
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        return (red + green + blue) / 3;
    }

//    Reduccion de ruido
}