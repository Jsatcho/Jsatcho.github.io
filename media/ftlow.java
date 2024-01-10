//yt videos: 
//https://www.youtube.com/watch?v=6LmGOm8t_Cw , //https://www.youtube.com/watch?v=Yemr-z4ZcYk

//https://jsoup.org/cookbook/input/load-document-from-url
//https://developer.mozilla.org/en-US/docs/Web/API/Document/getElementById
//https://jsoup.org/cookbook/extracting-data/selector-syntax
//https://stackoverflow.com/questions/30408174/jsoup-how-to-get-href
//https://jsoup.org/apidocs/org/jsoup/nodes/Element.html#selectFirst(java.lang.String)
//https://www.geeksforgeeks.org/java-program-to-open-input-url-in-system-default-browser-in-windows/


import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class FTLow extends JFrame implements ActionListener
{
	JTextField inputField = new JTextField(15);
	JButton SearchButton = new JButton("Search");
	public FTLow()
	{
		add(inputField);
		add(SearchButton);

		setLocationRelativeTo(null);
		setVisible(true);
		  
		setTitle("For The Low");
		setSize(300, 100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		
		
	}
	
	public void UserInputJFrameInputField()
	{
		 
		
		SearchButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try {
					Search(inputField.getText().toString());
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
  }
 

    public static void main(String[] args) throws URISyntaxException 
    {
    	FTLow shoe = new FTLow();
    	shoe.UserInputJFrameInputField();
    }
    
    	
    public void Search(String searchVar) throws URISyntaxException
    {
    	 String linkHref = "";
    	 String lowestlink = "";
         
         

         String searchUrl = "https://www.google.com/search?q=" + searchVar;

         try {
        	 //connect to search engine
             Document doc = Jsoup.connect(searchUrl)
                     .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
                     .get();
             
             //get websites
             Element results = doc.getElementById("search");
             
             double minPrice = Double.MAX_VALUE;
             
             ArrayList<String[]> linksAndPrices = new ArrayList<>();
             
             //iterates through results
             for (Element result : results.select("div[class]")) {
            	  
            	 //stores first anchor element w a link in result element
                 Element linkElement = result.selectFirst("a[href]");
                 if (linkElement == null)
                	 continue;
                 //stores link from linkelement
                 linkHref = linkElement.attr("href");
                 System.out.println(linkHref);
                 
                 //if not an image and contains a valid shoe company link
                 if ((!linkHref.startsWith("/imgres"))&&linkHref.contains("https://www.nike.com/") ||
            		     linkHref.contains("https://www.newbalance.com/")||
            		     linkHref.contains("finishline.com"))
                     	 {

                 //assigns webpage content to productpage
                 Document productPage = Jsoup.connect(linkHref).get();
                 
                 //takes price from website contents
                 Element priceElement = productPage.selectFirst("[class*=price]");
                 if (priceElement != null) {
                     String p = priceElement.text();
                     if (p.substring(0,1).equals("$")) {
	                     if (p.length()>4)
	                    	 p = p.substring(1,7);
	                     else
	                    	 p = p.substring(1);
                     }
                     else
                    	 continue;
                     double price = Double.parseDouble(p);
                     System.out.println("Price: " + price);
                     //adds array with link and product price
                     linksAndPrices.add(new String[] {linkHref, Double.toString(price)});
                     //made array of arraylists
                     //now have to iterate through it to get 
                     //lowest price and then go to that site
                 }
                 }
                
             
             }
             //finds lowest price and its link
             System.out.println("test");
             for (String[] s : linksAndPrices) {
            	 if (Double.parseDouble(s[1]) < minPrice)
            		 
                     minPrice = Double.parseDouble(s[1]);
                     lowestlink = s[0];
             		 }
             System.out.println((lowestlink));
            		 
             //searches for lowest priced product
             URI uri = new URI(lowestlink);
		     Desktop.getDesktop().browse(uri);
             
             
         } catch (IOException e) {
             JOptionPane.showMessageDialog(null,"Link was unable to be generated", "Link Error", JOptionPane.INFORMATION_MESSAGE);
         }

     }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}