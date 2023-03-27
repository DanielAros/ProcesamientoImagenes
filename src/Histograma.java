import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Histograma extends JPanel {

    private BufferedImage imagen;

    public Histograma(BufferedImage imagen) {
        this.imagen = imagen;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        int[] histogramaR = new int[256];
        int[] histogramaG = new int[256];
        int[] histogramaB = new int[256];

        for (int i = 0; i < imagen.getWidth(); i++) {
            for (int j = 0; j < imagen.getHeight(); j++) {
                Color color = new Color(imagen.getRGB(i, j));
                histogramaR[color.getRed()]++;
                histogramaG[color.getGreen()]++;
                histogramaB[color.getBlue()]++;
            }
        }

        int maxValor = 0;
        for (int i = 0; i < 256; i++) {
            if (histogramaR[i] > maxValor) {
                maxValor = histogramaR[i];
            }
            if (histogramaG[i] > maxValor) {
                maxValor = histogramaG[i];
            }
            if (histogramaB[i] > maxValor) {
                maxValor = histogramaB[i];
            }
        }

        double escala = (double) height / (double) maxValor;

        g.setColor(Color.BLACK);
        g.drawLine(20, height - 20, width - 20, height - 20);
        g.drawLine(20, height - 20, 20, 20);

        for (int i = 0; i < 256; i++) {
            g.setColor(Color.RED);
            g.drawLine(20 + i, height - 20, 20 + i, (int) (height - 20 - (histogramaR[i] * escala)));
            g.setColor(Color.GREEN);
            g.drawLine(20 + i, height - 20, 20 + i, (int) (height - 20 - (histogramaG[i] * escala)));
            g.setColor(Color.BLUE);
            g.drawLine(20 + i, height - 20, 20 + i, (int) (height - 20 - (histogramaB[i] * escala)));
        }
    }

}