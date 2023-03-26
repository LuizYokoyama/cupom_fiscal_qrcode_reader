package com.ly.qrcode;


import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class QrcodeApplication {

	public static void main(String[] args) {

		SpringApplication.run(QrcodeApplication.class, args);


		try {

			//qrCodeCreate();
			qrCodeRead();

		} catch (WriterException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	// Function to create the QR code
	public static void createQR(String data, String path,
								String charset, Map hashMap,
								int height, int width)
			throws WriterException, IOException
	{

		BitMatrix matrix = new MultiFormatWriter().encode(
				new String(data.getBytes(charset), charset),
				BarcodeFormat.QR_CODE, width, height);

		MatrixToImageWriter.writeToFile(
				matrix,
				path.substring(path.lastIndexOf('.') + 1),
				new File(path));
	}

	// Function to read the QR file
	public static String readQR(String path, String charset,
								Map hashMap)
			throws FileNotFoundException, IOException,
			NotFoundException
	{
		BinaryBitmap binaryBitmap
				= new BinaryBitmap(new HybridBinarizer(
				new BufferedImageLuminanceSource(
						ImageIO.read(
								new FileInputStream(path)))));

		Result result
				= new MultiFormatReader().decode(binaryBitmap);

		return result.getText();
	}

	public static void qrCodeCreate()
			throws WriterException, IOException,
			NotFoundException
	{

		// The data that the QR code will contain
		String data = "www.googel.com";

		// The path where the image will get saved
		String path = "demo.png";

		// Encoding charset
		String charset = "UTF-8";

		Map<EncodeHintType, ErrorCorrectionLevel> hashMap
				= new HashMap<EncodeHintType,
								ErrorCorrectionLevel>();

		hashMap.put(EncodeHintType.ERROR_CORRECTION,
				ErrorCorrectionLevel.L);

		// Create the QR code and save
		// in the specified folder
		// as a jpg file
		createQR(data, path, charset, hashMap, 200, 200);
		System.out.println("QR Code Generated!!! ");
	}

	public static void qrCodeRead()
			throws WriterException, IOException,
			NotFoundException
	{

		// Path where the QR code is saved
		String path = "cupom.png";

		// Encoding charset
		String charset = "UTF-8";

		Map<EncodeHintType, ErrorCorrectionLevel> hashMap
				= new HashMap<EncodeHintType,
				ErrorCorrectionLevel>();

		hashMap.put(EncodeHintType.ERROR_CORRECTION,
				ErrorCorrectionLevel.L);

		var result = readQR(path, charset, hashMap);
		System.out.println("QRCode output: " + result);

		scrap(result);
	}

	public static void scrap(String nfeUrl) throws IOException {

		String chromeDriverPath = "/home/luiz/Downloads/chromedriver" ;

		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200","--ignore-certificate-errors", "--silent");
		WebDriver driver = new ChromeDriver(options);

		driver.get(nfeUrl);

		//Store the web element
		WebElement iframe = driver.findElement(By.className("iframe-danfe-nfce"));

		//Switch to the frame
		driver.switchTo().frame(iframe);

		var page = driver.findElement(By.id("conteudo"));


	}


}
