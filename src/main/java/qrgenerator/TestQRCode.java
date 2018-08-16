package qrgenerator;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.FileAlreadyExistsException;

public class TestQRCode {

    private static final String PATH = System.getProperty("user.home") + "\\Desktop\\";
    private static final String FILE_NAME = "qrgenerator_default.png";

    public static void main(String[] args) {
        File f = new File(PATH + FILE_NAME);
        String text = "Nunca confies en un ordenador que no puedas lanzar por una ventana";
            /*TODO:fallando en deploy con gradle (reparar)
        //try {
            /*if (args == null || args.length == 0) {
            printHelpAndExit(true);
        } else {
            int index = 0;
            for (String flag : args) {
                index++;
                switch (flag) {
                    case "-c": {
                        text = args[index];
                        break;
                    }
                    case "--code": {
                        text = args[index];
                        break;
                    }
                    case "-o": {
                        f = new File(args[index].replaceAll(" ",""));
                        break;
                    }
                    case "--object": {
                        f = new File(args[index].replaceAll(" ",""));
                        break;
                    }
                    default: {
                        //printHelpAndExit(false);
                        break;
                    }

                }
            }
        }*/
      //  } catch (ArrayIndexOutOfBoundsException e) {
            //System.out.println(e.getMessage());
        //}


        TestQRCode qr = new TestQRCode();

        try {
            qr.createQrCode(f, text, 300, 300);
            System.out.println("qrcode generated : " + f.getAbsolutePath());
            String qrString = qr.decoder(f);
            System.out.println("text qr : " + qrString);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public File createQrCode(File file, String text, int y, int x) throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(text, com.google.zxing.BarcodeFormat.QR_CODE, x, y);
        BufferedImage image = new BufferedImage(matrix.getWidth(), matrix.getHeight(), BufferedImage.TYPE_INT_RGB);
        image.createGraphics();
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        //fondo
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrix.getWidth(), matrix.getHeight());
        //codigo
        graphics.setColor(Color.BLACK);
        for (int runnerX = 0; runnerX < matrix.getWidth(); runnerX++) {
            for (int runnerY = 0; runnerY < matrix.getHeight(); runnerY++) {
                if (matrix.get(runnerX, runnerY)) {
                    graphics.fillRect(runnerX, runnerY, 1, 1);
                }
            }
        }
        ImageIO.write(image, "png", file);
        return file;
    }

    public String decoder(File file) throws Exception {
        FileInputStream inputStream = new FileInputStream(file);
        BufferedImage image = ImageIO.read(inputStream);
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        Result result = reader.decode(bitmap);
        return result.getText();
    }

    private static void printHelpAndExit(boolean valid) {
        String jarPath = TestQRCode.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        String jarName = jarPath.substring(jarPath.lastIndexOf('/') + 1);

        StringBuilder sb = new StringBuilder("Usage: java -jar ");
        sb.append(jarName).append(" [option]\n");

        sb.append("Options:\n");
        sb.append("\t-h, --help\tmuestra este mensaje de ayuda\n");
        sb.append("\t-c, --code\tmensaje para codificar a qr\n");
        sb.append("\t-o, --object\tdestino de la codificacion\n");

        sb.append("Examples:\n");
        sb.append("\tjava -jar ").append(jarName).append(" -c necesito algo de cafe\n");
        sb.append("\tjava -jar ").append(jarName).append(" -o <root>/Desktop/qr_generate.png (va a escribir el archivo en formato png sin importar la especificacion)");

        System.out.println(sb.toString());
        if (valid) {
            System.exit(0);
        }

    }
}
