import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/* Pega uma lista de apartamentos com base em uma única URL de busca */
public class URLParser 					{
	
	public ArrayList<URL> urls = new ArrayList<URL>();   // Listagem de URLs de cada apartamento
	public ArrayList<URL> raizes = new ArrayList<URL>(); // As raízes, são a listagem de Aps que correspondem ao filtro de busca inicial
	
	public URLParser()				{
		try	{
			URL url = new URL("https://www.vivareal.com.br/venda/paraiba/joao-pessoa/bairros/bancarios/apartamento_residencial/#area-ate=200&area-desde=50&banheiros=2&onde=BR>Paraiba>NULL>Joao_Pessoa>Barrios>Bancarios,BR>Paraiba>NULL>Joao_Pessoa>Barrios>Cidade_Verde,BR>Paraiba>NULL>Joao_Pessoa>Barrios>Miramar,BR>Paraiba>NULL>Joao_Pessoa>Barrios>Altiplano_Cabo_Branco&preco-ate=300000&preco-desde=70000&quartos=2&tipo-usado=apartamento");
			raizes.add(url);
		} catch ( Exception e ) {
			System.out.println(e.getMessage());
		}
	}
	
	public void displayURLs()		{
	
		int i = 0;
		for(URL elem : urls)		{
			System.out.println("N°-"+i++ + ") "+ elem );
		}
		
	}
	
	// Execução para vários Aps
	public void runMultiple()		{
		
		try 	{
			URL url = new URL("https://www.vivareal.com.br/venda/paraiba/joao-pessoa/bairros/bancarios/apartamento_residencial/#area-ate=200&area-desde=50&banheiros=2&onde=BR>Paraiba>NULL>Joao_Pessoa>Barrios>Bancarios,BR>Paraiba>NULL>Joao_Pessoa>Barrios>Cidade_Verde,BR>Paraiba>NULL>Joao_Pessoa>Barrios>Miramar,BR>Paraiba>NULL>Joao_Pessoa>Barrios>Altiplano_Cabo_Branco&preco-ate=300000&preco-desde=70000&quartos=2&tipo-usado=apartamento");
			raizes.add(url);
			url = new URL("https://www.vivareal.com.br/venda/paraiba/joao-pessoa/bairros/bancarios/apartamento_residencial/?pagina=2#area-ate=200&area-desde=50&banheiros=2&onde=BR>Paraiba>NULL>Joao_Pessoa>Barrios>Bancarios,BR>Paraiba>NULL>Joao_Pessoa>Barrios>Cidade_Verde,BR>Paraiba>NULL>Joao_Pessoa>Barrios>Miramar,BR>Paraiba>NULL>Joao_Pessoa>Barrios>Altiplano_Cabo_Branco&preco-ate=300000&preco-desde=70000&quartos=2&tipo-usado=apartamento");
			raizes.add(url);
			/*url = new URL("https://www.vivareal.com.br/venda/paraiba/joao-pessoa/bairros/bancarios/apartamento_residencial/?pagina=3#area-ate=200&area-desde=50&banheiros=2&onde=BR>Paraiba>NULL>Joao_Pessoa>Barrios>Bancarios,BR>Paraiba>NULL>Joao_Pessoa>Barrios>Cidade_Verde,BR>Paraiba>NULL>Joao_Pessoa>Barrios>Miramar,BR>Paraiba>NULL>Joao_Pessoa>Barrios>Altiplano_Cabo_Branco&preco-ate=300000&preco-desde=70000&quartos=2&tipo-usado=apartamento");
			raizes.add(url);
			url = new URL("https://www.vivareal.com.br/venda/paraiba/joao-pessoa/bairros/bancarios/apartamento_residencial/?pagina=4#area-ate=200&area-desde=50&banheiros=2&onde=BR>Paraiba>NULL>Joao_Pessoa>Barrios>Bancarios,BR>Paraiba>NULL>Joao_Pessoa>Barrios>Cidade_Verde,BR>Paraiba>NULL>Joao_Pessoa>Barrios>Miramar,BR>Paraiba>NULL>Joao_Pessoa>Barrios>Altiplano_Cabo_Branco&preco-ate=300000&preco-desde=70000&quartos=2&tipo-usado=apartamento");
			raizes.add(url);
			url = new URL("https://www.vivareal.com.br/venda/paraiba/joao-pessoa/bairros/bancarios/apartamento_residencial/?pagina=5#area-ate=200&area-desde=50&banheiros=2&onde=BR>Paraiba>NULL>Joao_Pessoa>Barrios>Bancarios,BR>Paraiba>NULL>Joao_Pessoa>Barrios>Cidade_Verde,BR>Paraiba>NULL>Joao_Pessoa>Barrios>Miramar,BR>Paraiba>NULL>Joao_Pessoa>Barrios>Altiplano_Cabo_Branco&preco-ate=300000&preco-desde=70000&quartos=2&tipo-usado=apartamento");
			raizes.add(url);
			url = new URL("https://www.vivareal.com.br/venda/paraiba/joao-pessoa/bairros/bancarios/apartamento_residencial/?pagina=6#area-ate=200&area-desde=50&banheiros=2&onde=BR>Paraiba>NULL>Joao_Pessoa>Barrios>Bancarios,BR>Paraiba>NULL>Joao_Pessoa>Barrios>Cidade_Verde,BR>Paraiba>NULL>Joao_Pessoa>Barrios>Miramar,BR>Paraiba>NULL>Joao_Pessoa>Barrios>Altiplano_Cabo_Branco&preco-ate=300000&preco-desde=70000&quartos=2&tipo-usado=apartamento");
			raizes.add(url);*/
			
			for ( int i = 0; i<raizes.size(); i++ )
				  this.run(i);
			
		} catch ( Exception e ) {
			System.out.println(e.getMessage());
		}
	}
	
	// Método que realiza a captura de uma lista de imóveis com base em uma URL de Raíz, ou várias.... 
	public void run(int i)		{
	
		try	{
		
			// Busca sem filtragem
			// URL raiz = new URL("https://www.vivareal.com.br/venda/paraiba/joao-pessoa/apartamento_residencial/");
			URLConnection urlConnection = raizes.get(i).openConnection();
			HttpURLConnection connection = null;
		  
		    if ( urlConnection instanceof HttpURLConnection ) 	{
		         connection = (HttpURLConnection) urlConnection;
		    } 		else 		{
		         System.out.println("Please enter an HTTP URL.");
		         return;
		    }
	      
            // Objeto para leitura do stream de texto da página html
		    BufferedReader in = new BufferedReader( new InputStreamReader(connection.getInputStream()) );
		    String current;
		    String pageStr = "";
		    String key = "\"url\": \"https://www.vivareal.com.br/imovel/apartamento";	//descritor de início no codigo
		    
	        while((current = in.readLine()) != null) 		{
	         	 // pageStr += current + "\n";
	         	 
	         	 if ( current.contains(key)	)			{
	         		int pIni = current.indexOf( key );
	         		int pFim = current.lastIndexOf("/");
	    	        String str = current.substring( pIni, pFim );
	    	        
	    	        str = str.replace("url\": ", "" );
	    	        str = str.replaceAll("\"", "" );
	    	        // System.out.println("url = " + str );
	    	        URL urlAux = new URL(str);
		        	
	    	        if ( !urls.contains( urlAux ) )	// apenas adiciona se não contiver
	    	        	urls.add(urlAux);
	         	 }
	        }
 
	        /* while ( pageStr.contains(key) )		{
	        	
	        	System.out.println("URLs ==>> " + str);
	        	pageStr.replace(str, "xxxxxxxxxxxxxxx");

	        }*/
	        
	        
		} catch ( Exception exp ) 	{
		       System.out.println("Erro = " + exp.getMessage() );
			
		}
	}
	
}
