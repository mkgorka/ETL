import org.apache.commons.lang3.ObjectUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.File;
import java.util.List;


/**
 * Interfejs u¿ytkownika
 * @author Katarzyna Matusiak
 */

public class UsersWindow extends JFrame
{
	private JButton etl, extract, transform, load, clean, expCsv, expTxt;
	private JTextField fieldId;
	private int code;
	private JTextArea opinions;
	private Product product;
	String htmlPath = "C:\\Ceneo/HTML.txt";
		
	private UsersWindow()
	{
		setSize (1366,730);
		setTitle ("Projekt ETL");
			

		// panel z nagówkiem
		JPanel panelHead=new JPanel();
		panelHead.setBounds (0,0,1366,80);
		panelHead.setBackground(new Color(0,70,90));
		panelHead.setLayout(null);
		add(panelHead);
				    

		// etykieta nag³ówka
		JLabel labelHead=new JLabel("Pobieranie opinii z serwisu Ceneo.pl");
		Font fontHead=new Font("Tahoma",Font.BOLD,30);
		labelHead.setFont(fontHead);
		labelHead.setForeground(Color.white);
		labelHead.setBounds(300,5,1000,70);
		panelHead.add(labelHead);


		// panel z guzikami
		JPanel panelButtons=new JPanel();
		panelButtons.setBounds(0,80,400,570);
		panelButtons.setBackground(new Color(240,250,250));
		panelButtons.setLayout(null);
		add(panelButtons);
				    

		// etykieta pola z id przedmiotu
		JLabel labelId=new JLabel("ID przedmiotu: ");
		labelId.setBounds(50, 15, 100, 30);
		panelButtons.add(labelId);
				    

		// pole do wpisania id przedmiotu
		fieldId=new JTextField();
		fieldId.setBounds(150, 15, 150, 30);
		panelButtons.add(fieldId);
				    
				    
		// guzik ETL
		etl=new JButton("ETL");
		etl.setBounds(50,55,250,40);
		etl.setBackground(Color.white);
		panelButtons.add(etl);
		etl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				File input = null;
				try {
					input = new File(htmlPath);
				}catch(NullPointerException err){
					System.out.println(err.getMessage());
				}

				int insertedProd;
				int insertedRev;
				code=getCode();

				print("- Rozpoczêcie procesu Extract \n","set");

				try {
					if (code != 0) {

						UrlReview getURL = new UrlReview(code);
						getURL.getPageCount();
						NextUrlReview getNextPages = new NextUrlReview(getURL);
						TxtFile saveToTXT = new TxtFile(getURL, getNextPages);
						saveToTXT.bindStrings();
						saveToTXT.createFile();
						saveToTXT.writeToFile();

						if (!(input != null && input.exists()))
							print("- Plik HTML nie zosta³ poprawnie zapisany \n\n", "append");
						else {
							print("- Plik z kodem HTML zosta³ poprawnie zapisany (\"C:\\\\Ceneo/HTML.txt\") \n\n", "append");
							print("- Rozpoczêcie procesu Transform \n", "append");

							Product product = Transform.run(code);

							print("- Zakoñczenie procesu Transform \n", "append");

							if (product == null)
								print("- Za³adowanie produktu i opinii z pliku zakoñczone niepowodzeniem \n\n", "append");
							else {
								print("- Za³adowano 1 produkt i " + product.getAllReviews().size() + " opinii z pliku \n\n", "append");

								//Wczytywanie produktu do bazy
								print("- Rozpoczêcie procesu Load \n", "append");

								Load load = new Load();
								load.connect();

								insertedProd = load.loadProduct(product);
								insertedRev = load.loadReviews(product);

								print("- Zakoñczenie procesu Load \n", "append");

								if (insertedProd == 0) {
									print("- Podany produkt znajduje siê ju¿ w bazie danych \n", "append");

									if (insertedRev == 0)
										print("- Wszystkie opinie dla produktu znajduj¹ siê ju¿ w bazie danych \n", "append");
									else
										print("- Za³adowano " + insertedRev + " brakuj¹ce opinie dla produktu \n", "append");
								} else {
									print("- Za³adowano 1 produkt i " + insertedRev + " opinie\n", "append");
								}

								deleteFile();
								printDatabase();
							}
						}
					}

				}catch(NullPointerException err){
					err.printStackTrace();
				}

				JOptionPane.showMessageDialog(opinions, "Proces ETL zakoñczony.");
			}
		});
					

		// guzik Extract
		extract=new JButton("Extract");
		extract.setBounds(50,105,250,40);
		extract.setBackground(Color.white);
		panelButtons.add(extract);
		extract.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				code = getCode();
				String pathName = "C:\\Ceneo/HTML.txt";
				File input = null;
				try {
					input = new File(pathName);
				}catch(NullPointerException err){
					System.out.println(err.getMessage());
				}

				if (code != 0) {
					print("- Rozpoczêcie procesu Extract \n", "set");

					UrlReview getURL = new UrlReview(code);
					getURL.getPageCount();
					NextUrlReview getNextPages = new NextUrlReview(getURL);
					TxtFile saveToTXT = new TxtFile(getURL, getNextPages);
					saveToTXT.bindStrings();
					saveToTXT.createFile();
					saveToTXT.writeToFile();

					print("- Zakoñczenie procesu Extract \n", "append");

					if (input != null && !input.exists())
						print("- Plik HTML nie zosta³ poprawnie zapisany \n\n", "append");
					else {
						print("- Plik z kodem HTML zosta³ poprawnie zapisany (\"C:\\\\Ceneo/HTML.txt\") \n\n", "append");
					}
					JOptionPane.showMessageDialog(opinions, "Proces Extract zakoñczony.");
				}
			}
		});
					

		// guzik Transform
		transform=new JButton("Transform");
		transform.setBounds(50,155,250,40);
		transform.setBackground(Color.white);
		panelButtons.add(transform);
		transform.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				code = getCode();
				String pathName = "C:\\Ceneo/HTML.txt";
				File input = null;

				try {
					input = new File(pathName);
				}catch(NullPointerException err){
					System.out.println(err.getMessage());
				}

				if (code != 0 && input != null && input.exists()) {
					print("- Rozpoczêcie procesu Transform \n", "append");

					product = Transform.run(code);

					print("- Zakoñczenie procesu Transform \n", "append");

					if (product == null)
						print("- Za³adowanie produktu i opinii z pliku zakoñczone niepowodzeniem \n\n", "append");
					else
						print("- Z pliku za³adowano 1 produkt i " + product.getAllReviews().size() + " opinie \n\n", "append");

				}else{
					print("- Plik z kodem HTML nie istnieje. Proces Extract nie zosta³ wczeœniej przeprowadzony. Transform zakoñczony niepowodzeniem\n", "append");
				}

				JOptionPane.showMessageDialog(opinions, "Proces Transform zakoñczony.");
			}
		});

					
		// guzik Load
		load=new JButton("Load");
		load.setBounds(50,205,250,40);
		load.setBackground(Color.white);
		panelButtons.add(load);
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int insertedProd;
				int insertedRev;

				if (product != null) {

					//Wczytywanie produktu do bazy
					print("- Rozpoczêcie procesu Load \n", "append");
					Load load = new Load();
					load.connect();

					insertedProd = load.loadProduct(product);
					insertedRev = load.loadReviews(product);

					print("- Zakoñczenie procesu Load \n", "append");

					if(insertedProd == 0){
						print("- Podany produkt znajduje siê ju¿ w bazie danych \n", "append");

						if(insertedRev == 0)
							print("- Wszystkie opinie dla produktu znajduj¹ siê ju¿ w bazie danych \n", "append");
						else
							print("- Za³adowano " + insertedRev + " brakuj¹ce opinie dla produktu \n", "append");
					}else{
						print("- Za³adowano 1 produkt i " + insertedRev + " opinie\n", "append");
					}

					deleteFile();
					printDatabase();

				} else{
					print("- Produkt nie jest poprawnie za³adowany z pliku z kodem HTML. Proszê przeprowadziæ proces Transform \n\n", "append");
					JOptionPane.showMessageDialog(opinions, "Proces Load nie zosta³ przeprowadzony.");
				}
			}
		});
					
		// guzik Wyczyœæ bazê
		clean=new JButton("Wyczyœæ bazê");
		clean.setBounds(50,255,250,40);
		clean.setBackground(Color.white);
		panelButtons.add(clean);
		clean.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				Load load = new Load();
				load.connect();
				int deletedRows = load.cleanDatabase();
				JOptionPane.showMessageDialog(panelHead, "Czyszczenie bazy zakoñczone. Liczba usuniêtych produktów wraz z opiniami: "  + deletedRows );
			}
		});

		// guzik Eksport do .csv
		expCsv=new JButton("Eksport do .csv");
		expCsv.setBounds(50,305,250,40);
		expCsv.setBackground(Color.white);
		panelButtons.add(expCsv);
		expCsv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				Load load = new Load();
				load.connect();
				List<Product> products = load.selectProducts();
				boolean operationResult;
				int savedProd = 0;
				int savedRev = 0;

				if(products.size() == 0){
					print("- Brak produktów w bazie \n", "append");
				}
				else{
					for(Product p : products){
						//Zapisanie produktu i jego opinii do .csv
						operationResult = p.saveToCsvFile();

						if(operationResult){
							savedProd++;
							savedRev += p.getAllReviews().size();
						}
					}
				}

				print("Do plików .csv zapisano " + savedProd + " produkt/produkty oraz " + savedRev + " opinii \n\n", "append");
				JOptionPane.showMessageDialog(opinions, "Dane zosta³y wyeksportowane do pliku .csv");
			}
		});

					
		// guzik Eksport do pliku tekstowego
		expTxt=new JButton("Eksport do pliku tekstowego");
		expTxt.setBounds(50, 355, 250, 40);
		expTxt.setBackground(Color.white);
		panelButtons.add(expTxt);
		expTxt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Load load = new Load();
				load.connect();
				List<Product> products = load.selectProducts();
				boolean isSuccessful;
				int savedTxtFiles = 0;

				if (products.isEmpty()) {
					JOptionPane.showMessageDialog(panelHead, "Brak produktów w bazie.");
				} else {
					for (Product p : products) {
						//Zapisanie wszystkich opinii produktu do osobnych plikow .txt
						isSuccessful = p.saveReviewsToTxt();

						if (isSuccessful)
							savedTxtFiles += p.getAllReviews().size();
					}

					print("- Zapisano " + savedTxtFiles + " opinii do plików .txt \n\n", "append");

					JOptionPane.showMessageDialog(opinions, "Dane zosta³y wyeksportowane do pliku tekstowego.");
				}
			}
		});
					
		// panel z informacjami o opiniach
		JPanel panelOpinions=new JPanel();
		panelOpinions.setBounds (400,80,966,570);
		panelOpinions.setBackground(new Color(240,250,250));
		panelOpinions.setLayout(null);
		add(panelOpinions);

			
		// etykieta na panelu z opiniami
		JLabel labelOpinions=new JLabel("Opinie o wczytanych produktach");
		Font fontOpinions=new Font("Tahoma",Font.BOLD,20);
		labelOpinions.setFont(fontOpinions);
		labelOpinions.setBounds(270,20,500,30);
		panelOpinions.add(labelOpinions);
				    
		
		// tabela do wyœwietlenia opinii
		opinions=new JTextArea();
		JScrollPane scroll=new JScrollPane(opinions);
		scroll.setBounds(20, 80, 900, 450);
		panelOpinions.add(scroll);


		// panel ze stopk¹
		JPanel panelFooter=new JPanel();
		panelFooter.setBounds (0,500,1366,80);
		panelFooter.setBackground(new Color(240,250,250));
		add(panelFooter);


		// tekst w stopce
		JLabel labelFooter=new JLabel("<html>Program jest projektem zaliczeniowym z przedmiotu Hurtownie Danych na Uniwersytecie Ekonomicznym w Krakowie.<br>Program zosta³ stworzony przez: Katarzyna Matusiak, Zuzanna Gil, Anna Munk, Magdalena Górka");
		Font fontFooter=new Font("Tahoma",Font.PLAIN,10);
		labelFooter.setFont(fontFooter);
		panelFooter.add(labelFooter);
	}
		
	public static void main(String[] args) {
			
		UsersWindow frame = new UsersWindow();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setLayout(null);
		frame.setBackground(Color.getHSBColor(220, 20, 60));
	}

	private int getCode()
	{
		int code=0;

		String codeStr=fieldId.getText();

		if(codeStr.equals(""))
			JOptionPane.showMessageDialog(this, "Nie wprowadzono kodu produktu");
		else
			code=Integer.parseInt(codeStr);

		return code;
	}

	private void print(String str, String mode)
	{
		if(mode.equals("set"))
			opinions.setText(str);
		else if(mode.equals("append"))
				opinions.append(str);
	}

	private void printDatabase()
	{
		print("--------------------------------------\n", "append");
		print("PRODUKTY I OPINIE W BAZIE \n\n", "append");

		//Odczytywanie wszystkich zapisanych produktów z bazy
		Load load = new Load();
		load.connect();
		List<Product> products = load.selectProducts();

		//Wyœwietlanie opinii na ekranie
		for (Product p : products) {
			print(p.toString(), "append");
		}
	}

	private void deleteFile()
	{
		try {
			File input = new File(htmlPath);

			if (input.delete()) {
				print("- Plik " + input.getName() + " zosta³ usuniêty\n", "append");
			} else {
				print("- Plik nie zosta³ usuniêty\n", "append");
			}
		}catch (NullPointerException err){
			System.out.println(err.getMessage());
		}
	}
}

