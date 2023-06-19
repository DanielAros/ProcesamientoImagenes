import java.io.IOException;

public class Main {

    static String PATH = "C:\\Users\\daros\\OneDrive\\Escritorio\\imagen7.jpg";
//    static String PATH = "C:\\Users\\bmtz5\\OneDrive\\Imágenes\\fotoPersona.jpeg";
    public static void main(String[] args) throws IOException {
        System.out.println("Ejecutando proyecto");

        Ventana ventana = new Ventana(PATH);
    }
}