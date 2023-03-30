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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class QrcodeApplication {

	static WebDriver driver;
	public static void main(String[] args) {

		String chromeDriverPath = "/home/luiz/Downloads/chromedriver" ;
		SpringApplication.run(QrcodeApplication.class, args);
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200","--ignore-certificate-errors", "--silent");
		driver= new ChromeDriver(options);

		try {

			//qrCodeCreate();
			System.out.println("\nTESTE CUPOM 1:");
			qrCodeRead("cupom.png");
			System.out.println("\nTESTE CUPOM 2:");
			qrCodeRead("cupom2.png");
			System.out.println("\nTESTE CUPOM 3:");
			qrCodeRead("cupom3.png");
			System.out.println("\nTESTE CUPOM 4:");
			qrCodeRead("cupom4.png");

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

	public static void qrCodeRead(String imgFile)
			throws WriterException, IOException,
			NotFoundException
	{
		// Encoding charset
		String charset = "UTF-8";

		Map<EncodeHintType, ErrorCorrectionLevel> hashMap
				= new HashMap<EncodeHintType,
				ErrorCorrectionLevel>();

		hashMap.put(EncodeHintType.ERROR_CORRECTION,
				ErrorCorrectionLevel.H);

		var result = readQR(imgFile, charset, hashMap);
		System.out.println("QRCode output: " + result);

		//scrapPrint(result);
		System.out.println(scrap(result));
	}

	public static void scrapPrint(String nfeUrl) throws IOException {

		driver.get(nfeUrl);

		//Store the web element
		WebElement iframe = driver.findElement(By.className("iframe-danfe-nfce"));

		//Switch to the frame
		driver.switchTo().frame(iframe);

		//var page = driver.findElement(By.id("conteudo"));

		System.out.println("Empresa: ");
		var p1 = driver.findElement(By.className("txtTopo"));
		System.out.println(p1.getText());

		var p3 = driver.findElements(By.className("text"));
		System.out.println("CNPJ: ");
		System.out.println(p3.get(0).getText());
		System.out.println("Endereço: ");
		System.out.println(p3.get(1).getText());

		System.out.println("\ntab: ");
		var p4 = driver.findElement(By.id("tabResult"));
		System.out.println(p4.getText());

		System.out.println("\nProd: ");
		var p7 = driver.findElements(By.className("txtTit"));
		var p8 = driver.findElements(By.className("Rqtd"));
		var p9 = driver.findElements(By.className("RUN"));
		var p10 = driver.findElements(By.className("RvlUnit"));
		var p11 = driver.findElements(By.className("valor"));
		if (p7.size() > 0){
			for (int i = 0; i < p7.size(); i+=2){
				System.out.println(p7.get(i).getText() + " "
						+ p8.get(i/2).getText().replace("Qtde.:", "") +
						" " + p9.get(i/2).getText().replace("UN: ", "") +
						" x " + p10.get(i/2).getText().replace("Vl. Unit.:   ", "R$ ") +
						" Total: " + p11.get(i/2).getText()
				);
			}
		}

		System.out.println("\nTotal: ");
		var p5 = driver.findElements(By.id("linhaTotal"));
		if (p5.size() > 0){
			for (int i = 0; i < p5.size(); i++){
				System.out.println(i + ": " + p5.get(i).findElement(By.tagName("label")).getText());
				System.out.println(i + ": " + p5.get(i).findElement(By.className("totalNumb")).getText());
			}
		}

		System.out.println("\nEmissão: ");
		var p6 = driver.findElement(By.className("ui-li-static"));
		System.out.println(p6.getText().split(" Emissão: ")[1].split("- Via Consumidor")[0]);

	}

	public static CupomEntity scrap(String nfeUrl) throws IOException {

		driver.get(nfeUrl);

		//Store the web element
		WebElement iframe = driver.findElement(By.className("iframe-danfe-nfce"));

		//Switch to the frame
		driver.switchTo().frame(iframe);

		CupomEntity cupom = new CupomEntity();

		cupom.setNomeEmpresa(driver.findElement(By.className("txtTopo")).getText());

		cupom.setCnpj(driver.findElements(By.className("text")).get(0).getText().replace("CNPJ: ", ""));

		cupom.setEndereco(driver.findElements(By.className("text")).get(1).getText());

		List<ItenEntity> itens = new ArrayList<>();
		var nomeItem = driver.findElements(By.className("txtTit"));
		var qtd = driver.findElements(By.className("Rqtd"));
		var unidade = driver.findElements(By.className("RUN"));
		var valorUnit = driver.findElements(By.className("RvlUnit"));
		var valorTotalItem = driver.findElements(By.className("valor"));
		if (nomeItem.size() > 0){
			for (int i = 0; i < nomeItem.size(); i+=2){
				ItenEntity itenEntity = new ItenEntity();

				itenEntity.setNomeItem(nomeItem.get(i).getText());
				itenEntity.setQtd(Integer.parseInt(qtd.get(i/2).getText().replace("Qtde.:", "")));
				itenEntity.setUnidade(unidade.get(i/2).getText().replace("UN: ", ""));
				itenEntity.setValorUnit(Float.parseFloat(valorUnit.get(i/2).getText()
						.replace("Vl. Unit.:   ", "").replace(",",".")));
				itenEntity.setValorTotalItem(Float.parseFloat(valorTotalItem.get(i/2).getText()
						.replace(",",".")));
				itens.add(itenEntity);
			}
		}
		cupom.setItenEntityList(itens);

		var linhaTotal = driver.findElements(By.id("linhaTotal"));
		if (linhaTotal.size() > 0){
			for (int i = 0; i < linhaTotal.size(); i++){
				switch (linhaTotal.get(i).findElement(By.tagName("label")).getText()){
					case "Qtd. total de itens:":
						cupom.setQtdTotalItens(Integer.parseInt(linhaTotal.get(i).findElement(By.className("totalNumb")).getText()));
						break;
					case "Valor a pagar R$:":
						cupom.setValorPagar(Float.parseFloat(linhaTotal.get(i).findElement(By.className("totalNumb")).getText()
								.replace(",",".")));
						break;
					case "Cartão de Crédito":
						cupom.setCartaoCredito(Float.parseFloat(linhaTotal.get(i).findElement(By.className("totalNumb")).getText()
								.replace(",",".")));
						break;
					case "Cartão de Débito":
						cupom.setCartaoDebito(Float.parseFloat(linhaTotal.get(i).findElement(By.className("totalNumb")).getText()
								.replace(",",".")));
						break;
					case "Dinheiro":
						cupom.setDinheiro(Float.parseFloat(linhaTotal.get(i).findElement(By.className("totalNumb")).getText()
								.replace(",",".")));
						break;
					case "Pix":
						cupom.setPix(Float.parseFloat(linhaTotal.get(i).findElement(By.className("totalNumb")).getText()
								.replace(",",".")));
						break;
					case "Troco":
						cupom.setTroco(Float.parseFloat(linhaTotal.get(i).findElement(By.className("totalNumb")).getText()
								.replace(",",".")));
						break;
					case "Informação dos Tributos Totais Incidentes (Lei Federal 12.741/2012) R$":
						cupom.setTributos(Float.parseFloat(linhaTotal.get(i).findElement(By.className("totalNumb")).getText()
								.replace(",",".")));
						break;
				}
			}
		}

		cupom.setEmissao(
				driver.findElement(By.className("ui-li-static")).
				getText().split(" Emissão: ")[1].split("- Via Consumidor")[0]
		);

		return cupom;

	}


}
