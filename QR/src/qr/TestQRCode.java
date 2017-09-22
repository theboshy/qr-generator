package qr;

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

public class TestQRCode {

    //ruta de almacenamiento
    private static final String PATH = System.getProperty("user.home") + "\\Desktop\\";
    //nombre archiv
    
    private static final String FILE_NAME = "QRc";
    //tipo archivo
    private static final String FILE_EXT = ".png";
    //tamaño en eje Y
    private static final int H = 300;
    //tamaño en eje X
    private static final int W = 300;

    public static void main(String[] args) {

        TestQRCode qr = new TestQRCode();
        File f = new File(PATH + FILE_NAME + FILE_EXT);
        String text = "qrcodexa";

        try {

            qr.createQrCode(f, text, H, W);
            System.out.println("QRCode Generated: " + f.getAbsolutePath());

            String qrString = qr.decoder(f);
            System.out.println("Text QRCode: " + qrString);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public File createQrCode(File file, String text, int y, int x) throws Exception {

        //if (!file.exists()) {
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

        ImageIO.write(image, FILE_EXT.replace(".", ""), file);
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
}
