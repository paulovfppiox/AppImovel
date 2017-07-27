import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class App 				{

	static final int URL = 0;
	static final int PRECO = 1;
	static final int CONDOM = 2;
	static final int IPTU = 3;
	static final int BATHS = 4;
	static final int ROOMS = 5;
	static final int AREA = 6;

	// Colunas de Vantagens
	static final int SHOP = 8;
	static final int POOL = 9;
	static final int ACAD = 10;
	static final int FESTA = 11;
	static final int ELEVA = 12;
	static final int POS = 13;
	
	static public VivaRealParser dadosAp;
	static public VantagensAp vant;
	static public ApMainData data; 
	static public URLParser parser;
	
	static int itAp = 0;		// iterador dos APs
	static GeraPlanilha planilha = new GeraPlanilha("C:/Users/Bruno Paiva/workspace/AppImovel/Apps-Brunaw.xls");
	
	private static void geraPlanilha()					{
		
		int lineId = itAp + 1;
		
		planilha.addTxtCell( URL, lineId, "https://www.vivareal.com.br" + parser.urls.get(itAp).getPath() );
		planilha.addTxtCell( PRECO, lineId, data.getPrecoS() );
	    planilha.addNumCell( CONDOM, lineId, data.condominio );
	    planilha.addNumCell( IPTU, lineId, data.iptu );
	
	    String charUndef = "---";
	    
	    if ( data.getBanheirosNum() == -1 )		   		  
		    planilha.addTxtCell(BATHS, lineId, charUndef );
	    else
		    planilha.addNumCell(BATHS, lineId, data.getBanheirosNum() );
 	  
  	    if ( data.getQuartosNum() == -1 )
		    planilha.addTxtCell(ROOMS, lineId, charUndef );  
	    else
  		    planilha.addNumCell(ROOMS, lineId, data.getQuartosNum() );
	  
	    if ( data.getArea() == -1 )
		    planilha.addTxtCell(AREA, lineId, charUndef );
	    else
		    planilha.addNumCell(AREA, lineId,  data.getArea() );		
	    
	    if ( vant.getPertoDeShopping() == -1 )
		    planilha.addTxtCell(SHOP, lineId, charUndef );
	    else
		    planilha.addNumCell(SHOP, lineId, vant.getPertoDeShopping() ); 
	    
	    if ( vant.getPiscina() == -1 )
		    planilha.addTxtCell(POOL, lineId, charUndef );
	    else
		    planilha.addNumCell(POOL, lineId, vant.getPiscina() );
	    
	    if ( vant.getAcademia() == -1 )
		    planilha.addTxtCell(ACAD, lineId, charUndef );
	    else
		    planilha.addNumCell(ACAD, lineId, vant.getAcademia() );
	    
	    if ( vant.getFestas() == -1 )
		    planilha.addTxtCell(FESTA, lineId, charUndef );
	    else
		    planilha.addNumCell(FESTA, lineId, vant.getFestas() );
	    
	    if ( vant.getElevador() == -1 )
		    planilha.addTxtCell(ELEVA, lineId, charUndef );
	    else
		    planilha.addNumCell(ELEVA, lineId, vant.getElevador() );
	    
	    if ( vant.getPosicao() == null )
		    planilha.addTxtCell(POS, lineId, charUndef );
	    else
		    planilha.addTxtCell(POS, lineId, vant.getPosicao() );
	    
	}
	
	
	public static void main(String[] args) 					{
	   
	   ArrayList<String> labels = new  ArrayList<String>();			// Descrições da tabela a ser gerada
	   
	   labels.add(URL, "URL");
	   labels.add(PRECO, "Preço");
	   labels.add(CONDOM, "Condom.");
	   labels.add(IPTU, "IPTU"); 	      
	   labels.add(BATHS, "Banheiros"); 
	   labels.add(ROOMS, "Quartos");
	   labels.add(AREA, "Área");
	   labels.add("||||");
	   labels.add(SHOP, "Shopping?");
	   labels.add(POOL, "Piscina?");
	   labels.add(ACAD, "Academia?");
	   labels.add(FESTA, "Salão Festas?");
	   labels.add(ELEVA, "Elevador?");
	   labels.add(POS, "Posição?");
	   
	   planilha.addColsLabels( labels, 0 );
	   
	   parser = new URLParser();
	   //parser.run(0); // Roda apenas para uma página inicial de raíz...
	   parser.runMultiple();
	 
	   VivaRealParser dadosAp;
	   String pageStr = "";	   

	   while ( itAp < parser.urls.size()  )				{
		   
		   try 			{	 
			     // Abre conexão com um Apartamento da lista
		         URLConnection urlConnection = parser.urls.get(itAp).openConnection();
		         HttpURLConnection connection = null;
		         
		         if ( urlConnection instanceof HttpURLConnection ) 			{
		            connection = (HttpURLConnection) urlConnection;
		         } 			else 		{
		            System.out.println("Please enter an HTTP URL.");
		            return;
		         }
		         
		         // Objeto para leitura do stream de texto da página html
		         BufferedReader in = new BufferedReader( new InputStreamReader(connection.getInputStream()) );
		         String current;
		         pageStr = "";
		         
		         // Ler string do código em pageStr 
		         while((current = in.readLine()) != null) 		{
		        	 pageStr += current + "\n";
		         }
		         // System.out.println("" + pageStr );
 	             System.out.println("\n ************** Interpretando o apartamento N°-" + itAp + " ************** \n");
       		     //System.out.println( "URL = " + parser.urls.get(itAp) ) ;
		         
       		     // Interpreta os dados para criação dos objetos de dados
		   		  dadosAp = new VivaRealParser(pageStr);
		   		  vant = dadosAp.ap.getVantagens();
		   		  data = dadosAp.ap.getDadosGerais();
		   		
		   		  geraPlanilha();
       		      
		   		  //System.out.println( dadosAp.ap.getDadosGerais() );
		   		  //System.out.println( dadosAp.ap.getVantagens() );
       		     
       		     itAp++;
		         
		      } catch( Exception e ) 		{
		    	  // Imprime URL apenas dos Apartamentos que possuem erro de processamento
		    	  System.out.println( "\nErro ao processar URL ... ");
		    	  e.printStackTrace();
		    	  itAp++;
		    	  continue;
		      }
		   	  // System.out.println("\n ... Criando o parser ... ");
		   
	   }
	   planilha.write();
	}
}
