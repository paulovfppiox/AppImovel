import java.util.*;
import java.net.*;
import java.nio.charset.Charset;
import java.io.*;
import java.lang.*;

// Parser de um apartamento apenas. Recebe como input a página de um apartamento gerada.
// A partir desta string, e gerado os objetos Apartamento, ApEndereco e ApMainData
public class VivaRealParser	 {
	
	String apCod = "";
	String precoTitle = "";
	String precoDois = "";
	boolean hasSuite = false;
	
	VantagensAp vantagens = new VantagensAp();
	Apartamento ap = new Apartamento();
	ApEndereco  end = new ApEndereco();
	ApMainData  data = new ApMainData();
	
	ConversorCharsets convChars = new ConversorCharsets();
	
	private String rTagIni = ""; // tag de referência inicial para montar a substring de interesse (aquela que contém os dados buscados)
	private String rTagFim = ""; // tag de referência final
	private int pTagIni;
	private int pTagFim;
	
	private String contStr = ""; 
	
	String fone = "";
	public VivaRealParser() { }
	public VivaRealParser(String apPageCodeStr ) 					{	// recebe a string contendo o código da página do Ap 
		
		 this.getTitleInfo( apPageCodeStr );
		 this.getAddress( apPageCodeStr );
		 this.getApCod( apPageCodeStr );
		 this.getListaCaracteristicas( apPageCodeStr );
		 this.getInfosGeraisAp( apPageCodeStr );
		 this.getInfosAdicionais( apPageCodeStr );
		 this.mntApartamento();
	}
	
	/** Pega o código do AP */ 
	public void getApCod( String pageCodeStr ) 						{
		
		// String codDiv = "<span class=\"property-title__code\">COD. ap23291 </span>";
		String tag1 = "<span class=\"property-title__code\">";
		String tag2 = "property-title__location js-titleLocation";
		
		int id1 = pageCodeStr.indexOf(tag1);
		pageCodeStr = pageCodeStr.substring( pageCodeStr.indexOf(tag1), pageCodeStr.indexOf(tag2) );
		// out("id = " + id1 + " -- " + "code = " + pageStr);
		
		pageCodeStr = pageCodeStr.replace(tag1, " ");
		pageCodeStr = pageCodeStr.replace(tag2, " ");
		pageCodeStr = pageCodeStr.replace("COD.", " ");
		pageCodeStr = pageCodeStr.replaceAll("\\s",""); // remove brancos
		pageCodeStr = pageCodeStr.replaceAll("[^0-9.]", ""); // remove não numericos
		apCod = pageCodeStr;
		out("Codigo = " + pageCodeStr);
	}
	
	/** pega o id da rua */
	public static int getRuaId ( String str, String qual )			{
		
		int resp = 0;
		
		if ( str.contains( qual ) )		{
			resp = str.indexOf(qual);
		} 
		return resp;
	}
	
	/** Pega o id da rua, na string de endereço.. com base em um vetor de qualificadores (ex: rua, Rua, av.) */
	private int getRuaIdByQuali( String[] ruaQualis, String addStr )			{ 
		
		int idRua = 0;
		
		for ( int i=0; i < ruaQualis.length ; i++ )			
			if ( ruaQualis[i] != null )
				idRua = getRuaId( addStr, ruaQualis[i] );
		
		return idRua;
	}
	
	public boolean isBairro( String str )						{
		ArrayList<String> bairros = new ArrayList<String>();
		bairros.add("Bancários");
		bairros.add("Altiplano");
		bairros.add("Torre");
		bairros.add("Manaíra");
		
		for( int i=0; i< bairros.size(); i++ )			{
			if ( str.equals( bairros.get(i) ) )		{
				return true;
			}	
		}
		return false;
	}
	
	public void getAddress( String pageCodeStr )						{
		
		// <strong class="property-title__location js-titleLocation"> Avenida Cajazeiras, 359,  Manaíra, João Pessoa, PB </strong></h1></section>
		//System.out.print("Endereco" + pageStr);
		String tag1 = "property-title__location js-titleLocation\">";					 // tag anterior ao conteúdo buscado
		String tag2 = "site-main__property-gallery property-gallery js-propertyGallery"; // tag q determina o fim do conteúdo buscado
		
		if ( pageCodeStr.contains(tag1) && pageCodeStr.contains(tag2)  )		{
			
			int x = pageCodeStr.indexOf(tag1);
			int y = pageCodeStr.indexOf(tag2);
			
			String addStr = pageCodeStr.substring( x, y );	// String contendo o endereço
			int size = addStr.length();
			
			// System.out.println( "addStr == " + addStr + ", size ==> " + size ); // 159
			
			// Remove o nome da cidade e atualiza size
			//ex: <strong class="property-title__location js-titleLocation"> Avenida Cajazeiras, 359,  Manaíra
			int jpId = addStr.indexOf("JoÃ£o Pessoa");
			// System.out.println("\njpId ==> " + jpId ); 
			
			String rest = addStr.substring(jpId,  size-1);
			addStr = addStr.replace(rest, "");
			// addStr = addStr.replaceAll("\\s","");
			size = addStr.length();
			
			/*System.out.println( "rest == " + rest + ", size ==> " + size );
			System.out.println( "addStr == " + addStr );*/
			addStr = addStr.replace(tag1, "");

			addStr = convertAcentos(addStr);
			// addStr = addStr.replaceAll("[^0-9\\p{L}\\s]", "");

			// Identifica o início do endereço
			String ruaQualis[] = { "rua", "Rua", "Avenida", "Av.", "r.", "R." };
			int idRua = -1;
			// idRua = getRuaIdByQuali(ruaQualis, addStr);
			
			// out(" addStr >> " + addStr );
			String endData[] = addStr.split(",");
			String aux = "";
			
			for( int j=0; j<endData.length; j++ )				{
				
				aux = endData[j].replaceAll("\\s","");	// remove brancos
				// out("data" + endData[j] + isBairro(aux) + "\n");
				
				if ( isBairro(aux) )			    { // trata bairro
					end.setBairro( aux );
					continue;
				}
				if ( containsOnlyNumbers(aux) ) 	{ // trata n° do Ap
					int n = Integer.parseInt(aux);
					end.setNumImovel(n);
					continue;
				}
				
				String rua;
				for ( int i=0; i < ruaQualis.length ; i++ )	{ // Trata ruas			
					if ( ruaQualis[i] != null )		{
						if ( endData[j].contains(ruaQualis[i]) )	{
							rua = endData[j].replace(ruaQualis[i], "");
							end.setRua( rua );
							continue;
						}
					}
				}
			}
			out(end + "\n\n");
		}
	}
	
	public void getTitleInfo ( String pageCodeStr ) 			{
		// exemplos: INPUTS: 
		// <title> Apartamento na Rua João Galiza de Andrade, 94, Bancários em João Pessoa, por R$ 190.000 - VivaReal</title>
		// <title> Apartamento na Rua João Galiza de Andrade, 190.000 - VivaReal </title>
		
		int initId = pageCodeStr.indexOf("<title>");
		int fimId = pageCodeStr.indexOf("</title>");
		String str = pageCodeStr.substring( initId, fimId );
		
		int tam = str.length();
		String precoStr = "";
		
		if ( str.contains("<title>") )			{
			 //out(str + "\n\n"); 
			 
			 if ( str.contains("R$") )	{
				  int id = str.indexOf("R$");
			 	  // out(str + " " + id + "\n\n");
			 	  precoStr = str.substring(id, tam);
			 } 
			 apMainData.precoTitle = precoStr.replaceAll("[^0-9.]", ""); // remove todos caracteres não numéricos
			 out("preco = " + apMainData.precoTitle );
		}
	}
	
	public boolean findPhone( String str )				{
		
		String phoneStr = "";
		String foneCod = "";
		
		if ( str.contains("(83)") )		foneCod = "(83)";
		else if ( str.contains("083") )	foneCod = "083";
		else if ( str.contains("83") )	foneCod = "083";

		if ( !foneCod.isEmpty() )		{
			int codOp =  str.indexOf(foneCod);
			
			int tam = str.length();
			int tamAux = codOp + 15; 
			
			if ( tamAux <= tam )
				 tam = tamAux;
				
			phoneStr = str.substring( codOp ,  tam );
			fone = phoneStr;
			fone.replaceAll("\\s","");		// remove brancos
			fone.replaceAll("[^0-9.]", ""); // remove não-numéricos
			
			String newStr = str.replace(phoneStr, ""); 
			// System.out.println("New str = " + newStr);
			
			while(findPhone(newStr));
			return true;
		}
		return false;
	}
	
	private void regrasDeBuscaVantagens( String[] lista  )					{
		
		for(int i=0; i<lista.length; i++)		{
			if ( lista[i].contains("Perto de Shopping") && ( vantagens.getPertoDeShopping() == -1 ) )
				vantagens.setDeposito( 1 );
			
			if ( lista[i].contains("DepÃ³sito") || lista[i].contains("DEPÃ³SITO") || lista[i].contains("deposito") && ( vantagens.getDeposito() == -1 ) )
				vantagens.setDeposito(1);
				
			if ( lista[i].contains("CondomÃ­nio") || lista[i].contains("CONDOMÃ­NIO") && ( vantagens.getCondFechado() == -1 )  )
				vantagens.setCondFechado(1);
				
			if ( lista[i].contains("Sacada") || lista[i].contains("sacada") || lista[i].contains("SACADA") && ( vantagens.getVaranda() == -1 ) )
				vantagens.setVaranda(1);
			
			if ( lista[i].contains("Elevador") || lista[i].contains("elevador") && ( vantagens.getElevador() == -1 ) )
				vantagens.setElevador(1); 
			
			 if ( lista[i].contains("lazer") || lista[i].contains("Lazer") ||  lista[i].contains("LAZER") && ( vantagens.getAreaLazer() == -1 )  )			{ // Lazer
				 vantagens.setAreaLazer(1);
			 }
			 if ( lista[i].contains("Varanda") || lista[i].contains("varanda") ||  lista[i].contains("VARANDA") && ( vantagens.getVaranda() == -1 )  )		{ // Varanda
				 vantagens.setVaranda(1);
			 }
			 if ( lista[i].contains("Playground") || lista[i].contains("playground") ||  lista[i].contains("PLAYGROUND") && ( vantagens.getPlayground() == -1 )  )	{ // Playground
				 vantagens.setPlayground(1);
			 }
			 if ( lista[i].contains("Esporte") || lista[i].contains("esporte") ||  lista[i].contains("ESPORTE") || lista[i].contains("Quadra") || lista[i].contains("quadra") )		{ // Poliesportiva
				 // setQuadraEsporte(1);
			 }
			 if ( lista[i].contains("festa") || lista[i].contains("Festa") ||  lista[i].contains("FESTA") && ( vantagens.getSalaoFesta() == -1 ) )	{ // Salão de festa
				 vantagens.setSalaoFesta(1);
			 }
			 if ( lista[i].contains("Academia") || lista[i].contains("academia") ||  lista[i].contains("ACADEMIA") && ( vantagens.getAcademia() == -1 ) )	{ // Salão de festa
				 vantagens.setAcademia(1);
			 }
			 if ( lista[i].contains("Churrasqueira") || lista[i].contains("churrasqueira") ||  lista[i].contains("CHURRASQUEIRA") && ( vantagens.getChurrasco() == -1 )   )	{ // Churrasqueira
				 vantagens.setChurrasco(1);
			 }
			 if ( lista[i].contains("Piscina") || lista[i].contains("piscina") ||  lista[i].contains("PISCINA") && ( vantagens.getPiscina() == -1 )  )	{ // Piscina
				 vantagens.setPiscina(1);
			 }
			 if ( lista[i].contains("WC SOCIAL") || lista[i].contains("WC Social") ||  lista[i].contains("Wc social")  && ( vantagens.getWcSocial() == -1 ) )	{ // Piscina
				 vantagens.setWcSocial(1);
			 }
			 if ( lista[i].contains("Elevador") || lista[i].contains("elevador") && ( vantagens.getElevador() == -1 ) )	{
				 vantagens.setElevador(1); 
			 }
		}
		
		
	}
	
	// Toda página, contém também, após as descrições (em parágrafo), uma lista de características
	public void getListaCaracteristicas( String pageCodeStr )								{
		String caracTag = "<ul class=\"property-description__features\"><li>";
		String tagFim = "</li></ul> <p><a class=\"property-description__see-more"; // Não é legal ser este, pois pode ser que tenha em outro
		
		String cont = "";
		
		if ( pageCodeStr.contains( caracTag ) )				{
				
				// System.out.println("Tem uma lista!!!!!");
				int posIni = pageCodeStr.indexOf( caracTag );
				int posFim = pageCodeStr.indexOf( tagFim );
				
				cont = pageCodeStr.substring( posIni, posFim );
				
				String lista[] = cont.split("</li><li>");
				
				lista[0] = lista[0].replace(caracTag, "");
				
				regrasDeBuscaVantagens( lista );
				// System.out.println( lista[0] + ", " + lista[1] + ", " + lista[2] );
		}			
	}
	
	// h3 class="property-description__title property-description__additional">Características</h3>
	// <ul class="property-description__features"><li>
	/** Pega infos da construtora, etc... parte adicional, vantagens  */
	public void getInfosAdicionais( String pageCodeStr )							{
		
		int size = pageCodeStr.length();
		
		String descrDiv = "property-description__detail";
		String vantagensDiv = "<ul class=\"property-description__features\"><li>";
		
		if ( pageCodeStr.contains( descrDiv ) && pageCodeStr.contains(vantagensDiv) )				     {
			// Separa o texto de descrições
			
			String descrStr = pageCodeStr.substring( pageCodeStr.indexOf( descrDiv ), pageCodeStr.indexOf(vantagensDiv)+500 );
			descrStr = descrStr.replace(descrDiv, "");
			
			String descrData[] = {""};
			
			if ( descrStr.contains(",")	)			// Esta errado	
				descrData = descrStr.split(",");
			else 
				descrData = descrStr.split(" ");
			
		    // System.out.println(" --------- Descrições --------- ");
			// System.out.println("-- Str: " + descrStr + "\n");
			
			int idSuite = -1;
			
			for( int i=0; i<descrData.length; i++ ) 								{
				 // System.out.println( "** Descricao-" + i + ")" + descrData[i] );

				 // Verificando Suítes				
				if ( vantagens.getNumSuites() == -1 )			{	
					
					 String numSuitesKeys[] = {  "1", "2", "3", "4", "uma", "duas", "três", "tres", "quatro" };
					 String suiteKeys[] = { "suÃ­te", "suite", "SUÃ­TE", "Suite", "SUITE", "SuÃ­te", "SUÃ?TE", "SuÃ?te", "suÃ?te" };
					 
					 // Numero de suítes
					for(int k=0; k<numSuitesKeys.length; k++)		{	// varia numSuites
						 for(int j=0; j<suiteKeys.length; j++)	 {
							 
							 if ( descrData[i].contains(numSuitesKeys[k] + " " + suiteKeys[j]) ||	// Com espaço 
								  descrData[i].contains(numSuitesKeys[k] + suiteKeys[j]) )	{		// Sem espaço
								 
								 // System.out.print("Este ap tem: " + numSuitesKeys[j] + " " + suiteKeys[k] + "\n");
								 
								 if ( (numSuitesKeys[k] == "uma") ||  (numSuitesKeys[k] == "UMA")  || (numSuitesKeys[k] == "1") )	{	
								 	vantagens.setNumSuites(1);
								 	if ( data.getSuitesNum() == -1 )
								 		 data.setSuitesNum(1);
								 }
								 if ( (numSuitesKeys[k] == "duas") || (numSuitesKeys[k] == "DUAS") || (numSuitesKeys[k] == "2") )	{
								 	 vantagens.setNumSuites(2);
								 	if ( data.getSuitesNum() == -1 )
								 		data.setSuitesNum(2);
								 }
								 if ( (numSuitesKeys[k] == "três") || (numSuitesKeys[k] == "TRÊS") || (numSuitesKeys[k] == "tres") || (numSuitesKeys[k] == "TRES") || (numSuitesKeys[k] == "3") ){	
								 	vantagens.setNumSuites(3);
								 	if ( data.getSuitesNum() == -1 )
								 		data.setSuitesNum(3);
								 }
								 if ( (numSuitesKeys[k] == "quatro") || (numSuitesKeys[k] == "QUATRO") || (numSuitesKeys[k] == "4") )	{	
								 	vantagens.setNumSuites(4);
								 	if ( data.getSuitesNum() == -1 )
								 		 data.setSuitesNum(4);
								 }
							 }
						}
					}
				}
				 
				 // Área de serviço		
				 if ( vantagens.getAreaServ() == -1 )			{
					 if ( descrData[i].contains("Ã¡rea de serviÃ§o") ||
						  descrData[i].contains("rea de servi") || 
						  descrData[i].contains("REA DE SERVI") ||
						  descrData[i].contains("Ã¡REA DE SERVIÃ§O") && 
						  ( vantagens.getWcSocial() == -1 )
							)		{
						  vantagens.setAreaServ(1);
					 }
				 }
				 
				 
			// Posição
			 if ( vantagens.getPosicao() == null )						{
				 
				 String strAux;
				 int idPos, idPos2, tam;
				 
				 tam = descrData[i].length();
				 
				 String keysPos[] = { "POSIÃ§Ã£O", "osiÃ§Ã£o" };
				 
				 for(int keyId=0; keyId<2; keyId++) 		{	
				 
					 if ( descrData[i].contains(keysPos[keyId]) ) 			{
						 idPos = descrData[i].indexOf( keysPos[keyId] );
						 idPos2 = idPos + 100;
						 if ( idPos2 >= tam )
							 idPos2 = tam;
						 strAux = descrData[i].substring( idPos, idPos2	);
						 
						 // Tratamento para diminuição da String, caso contenha ponto final
						 
						 // System.out.print( "-->> strAux: " +  strAux );
						 if ( strAux.contains("ascente") || strAux.contains("ASCENTE") )	{
							 strAux = "nascente";
							 vantagens.setPosicao("nascente");
						 }
						 if ( strAux.contains("SUL") || strAux.contains("sul") || strAux.contains("Sul") )	{
							 strAux = "sul";
							 vantagens.setPosicao("sul");
						 }
						 // System.out.print( "-->> Posição AP: " +  strAux );
						
					 }
				}
			 }
			 
			 regrasDeBuscaVantagens( descrData );
				 
				/* if ( descrData[i].contains("lazer") || descrData[i].contains("Lazer") ||  descrData[i].contains("LAZER") && ( vantagens.getAreaLazer() == -1 )  )			{ // Lazer
					 vantagens.setAreaLazer(1);
				 }
				 if ( descrData[i].contains("Varanda") || descrData[i].contains("varanda") ||  descrData[i].contains("VARANDA") && ( vantagens.getVaranda() == -1 )  )		{ // Varanda
					 vantagens.setVaranda(1);
				 }
				 if ( descrData[i].contains("Playground") || descrData[i].contains("playground") ||  descrData[i].contains("PLAYGROUND") && ( vantagens.getPlayground() == -1 )  )	{ // Playground
					 vantagens.setPlayground(1);
				 }
				 if ( descrData[i].contains("Esporte") || descrData[i].contains("esporte") ||  descrData[i].contains("ESPORTE") )		{ // Poliesportiva
					 // vantagens.setQuadraEsporte(1);
				 }
				 if ( descrData[i].contains("festa") || descrData[i].contains("Festa") ||  descrData[i].contains("FESTA") && ( vantagens.getSalaoFesta() == -1 ) )	{ // Salão de festa
					 vantagens.setSalaoFesta(1);
				 }
				 if ( descrData[i].contains("Churrasqueira") || descrData[i].contains("churrasqueira") ||  descrData[i].contains("CHURRASQUEIRA") && ( vantagens.getChurrasco() == -1 )   )	{ // Churrasqueira
					 vantagens.setChurrasco(1);
				 }
				 if ( descrData[i].contains("Piscina") || descrData[i].contains("piscina") ||  descrData[i].contains("PISCINA") && ( vantagens.getPiscina() == -1 )  )	{ // Piscina
					 vantagens.setPiscina(1);
				 }
				 if ( descrData[i].contains("WC SOCIAL") || descrData[i].contains("WC Social") ||  descrData[i].contains("Wc social")  && ( vantagens.getWcSocial() == -1 ) )	{ // Piscina
					 vantagens.setWcSocial(1);
				 }
				 if ( descrData[i].contains("Elevador") || descrData[i].contains("elevador") && ( vantagens.getElevador() == -1 ) )	{
						vantagens.setElevador(1); 
				 }*/
				 // if ( findPhone( descrData[i] ) )			{
				 //  System.out.println( "Tel:.= " + fone );
				 //}
				 
				 // Caso informações principais do APMainData esteja aqui...
				 if ( data.getQuartosNum() == -1 )	{	
				  String numKeys[] = { "1", "2", "3", "4", "um", "UM", "dois", "DOIS", "três", "TRÊS", "tres", "quatro", "QUATRO" };
					  String quartosKeys[] = { "QUARTO", "Quarto", "quarto" };
					  
					  for (int k=0; k<numKeys.length; k++)			{
						  for (int j=0; j<quartosKeys.length; j++)		{
							  
							  if ( descrData[i].contains( numKeys[k] + " " + quartosKeys[j] ) ) 	{
								  
								  if ( ( numKeys[k] == "1" ) || ( numKeys[k] == "UM" ) || ( numKeys[k] == "um" ) )
									  data.setQuartosNum(1);
								  if ( ( numKeys[k] == "2" ) || ( numKeys[k] == "DOIS" ) || ( numKeys[k] == "dois" ) )
									  data.setQuartosNum(2);
								  if ( ( numKeys[k] == "3" ) || ( numKeys[k] == "TRÊS" ) || ( numKeys[k] == "TRES" ) || ( numKeys[k] == "Três" ) || ( numKeys[k] == "Tres" ) )
									  data.setQuartosNum(3);
							  }
							  
						  }
					  }
				 }
				 
				 if ( data.getBanheirosNum() == -1 )	{	
					  String numKeys[] = { "1", "2", "3", "4", "um", "UM", "dois", "DOIS", "três", "TRÊS", "tres", "quatro", "QUATRO" };
						  String quartosKeys[] = { "banheiro", "BANHEIRO", "Banheiro" };
						  // towpper, //tolower						  
						  
						  for (int k=0; k<numKeys.length; k++)			{
							  for (int j=0; j<quartosKeys.length; j++)		{
								  
								  if ( descrData[i].contains( numKeys[k] + " " + quartosKeys[j] ) ) 	{
									  
									  if ( ( numKeys[k] == "1" ) || ( numKeys[k] == "UM" ) || ( numKeys[k] == "um" ) )
										  data.setBanheirosNum(1);
									  if ( ( numKeys[k] == "2" ) || ( numKeys[k] == "DOIS" ) || ( numKeys[k] == "dois" ) )
										  data.setBanheirosNum(2);
									  if ( ( numKeys[k] == "3" ) || ( numKeys[k] == "TRÊS" ) || ( numKeys[k] == "TRES" ) || ( numKeys[k] == "Três" ) || ( numKeys[k] == "Tres" ) )
										  data.setBanheirosNum(3);
								  }
								  
							  }
						  }
					 }
				 
			}
			ap.setVantagens(vantagens);
			//System.out.println( vantagens );
		}	
		//else	{
			// Cria um vantagens nulos
		//}
		String endDiv = "<section class=\"site-main__map-location";
	}
	
	public void mntApartamento()			{
		ap.setEndereco(end);
		ap.setDadosGerais(data);
		ap.setVantagens(vantagens);
		//System.out.println( ap );
	}
	
	/** Converte strings acentuadas */
	private static String convertAcentos(String str)			{
		 
		Charset utf = Charset.forName("UTF-8");
		Charset iso = Charset.forName("ISO-8859-1");
		return new String(str.getBytes(), utf );
		
	}	
	
	public static class apMainData			{
		public static String preco = "";
		public static String precoTitle = "";
		public static int condominio = 0;
		public static int iptu = 0;
		public static int banheirosNum = 0;
		public static int quartosNum = 0;
		public static int area = 0;
		public static int suitesNum = 0;
	}
	
	
	private void processaSubPreco( String pageCodeStr )			{
	
		rTagIni = "property-information__item property-information--prices property-information__sub-price";
		
		// ------- Subpreço -------
		if ( pageCodeStr.contains( rTagIni )) 			{
			
			pTagIni = pageCodeStr.indexOf( rTagIni );
			pTagFim = pTagIni + 600;
			
			contStr = pageCodeStr.substring( pTagIni, pTagFim );	// texto que contem todos os precos e sub-precos
			// out("Subpreco == \n" + contStr + "** \n");
			
			// Busca existência de taxas (IPTU, etc)
			String[] keys = { "CondomÃ­nio", "condomÃ­nio" , "IPTU", "iptu" }; // chaves para verificação da existência do IPTU 
			String strAux = "";
			
			for ( int i=0; i<keys.length; i++ )					{	// para cada Chave, 
				
				if ( contStr.contains( keys[i] ) )			{			// Verifica se a substring contém tal chave
					int idAux = contStr.indexOf(keys[i]);
					strAux = contStr.substring( idAux, idAux+150 );
					strAux = strAux.replaceAll("\\s","");	   // remove brancos
					strAux = strAux.replaceAll("[^0-9.]", ""); // remove não-numéricos
					strAux = strAux.replace(".", "");
					
					if ( i == 0 || i == 1 )		{
						data.setCondominio(  Integer.parseInt( strAux )  );
						contStr = contStr.replace( strAux, " " );	// remove o condominio, pq é o primeiro da string
					}	else	{
						data.setIptu( Integer.parseInt( strAux ) );
					}
				}
			}
		// out("condominio = " + apMainData.condominio);
		// out("iptu = " + apMainData.iptu);
		}
		
	}	
	
	private void processaPreco( String pageCodeStr )						{
	
		rTagIni = "property-information__item-description property-information--price"; 
		pTagIni = pageCodeStr.indexOf(rTagIni); 				// Acha a posição da div
		pTagFim = pTagIni + 150;
		
		// out( pageCodeStr );
		contStr = pageCodeStr.substring( pTagIni, pTagFim ); 	// Pega a substring

		// 2- Busca caracter de referência 
		if ( contStr.contains("R$") )					 	{
			contStr = contStr.replace(rTagIni, " ");
			contStr = contStr.replace("</span>", " ");
			contStr = contStr.replaceAll("\\s","");	   // remove brancos
			contStr = contStr.replaceAll("[^0-9.]", ""); // remove não-numéricos
			data.setPrecoS( contStr );
			// apMainData.preco = precoStr;
			// out("2)) " + precoStr + "\n");
		}
		
	}
	
	private void processaArea( String pageCodeStr )			{
	    // Area
		rTagIni = "property-information__item icon-area";
		rTagFim = "<span>m";
		
		if ( pageCodeStr.contains( rTagIni ) )							{
			
			pTagIni = pageCodeStr.indexOf( rTagIni );
			pTagFim = pageCodeStr.indexOf( rTagFim );
			
			contStr = pageCodeStr.substring( pTagIni, pTagFim );
			contStr = contStr.replace("</span>", " ");
			contStr = contStr.replaceAll("\\s","");		// remove brancos
			contStr = contStr.replaceAll("[^0-9.]", ""); // remove não-numéricos
			
			// apMainData.area = Integer.parseInt( areaStr );
			data.setArea( Integer.parseInt( contStr ) );
			//System.out.println("\narea = " + contStr);
		}
		
	}
	
	private void processaQuartos( String pageCodeStr )					{
		
		// Quartos
				rTagIni = "property-information__item icon-room";
				rTagFim = "</span> quarto";
				
				if ( pageCodeStr.contains(rTagIni) ) 					{
					
					pTagIni = pageCodeStr.indexOf(rTagIni);
					pTagFim = pageCodeStr.indexOf(rTagFim);
	
					// ---- N° de quartos ----
					contStr = pageCodeStr.substring( pTagIni, pTagFim ); // aqui, tem uma brecha		
					int tam = contStr.length();
					contStr = contStr.substring( tam-10, tam );
					contStr = contStr.replaceAll("\\s","");	   // remove brancos
					contStr = contStr.replaceAll("[^0-9.]", "");  // remove não-numéricos
					// quartosStr = quartosStr.replaceAll("^\\p{IsDigit}]", "");
					
					int i = 0;
					int numQuartos = Integer.parseInt( contStr );
	
					data.setQuartosNum(numQuartos);
					
					// ---- N° de Suite ----
					int idEnd = 0;
					String auxStr = "";
					if ( pageCodeStr.contains("recommendations__title" ) )		{ 
						idEnd = pageCodeStr.indexOf("recommendations__title");			
						auxStr = pageCodeStr.substring( pTagIni, idEnd );
					}
					
					if ( auxStr.contains("suÃ­te") )			{
						int idSuite = auxStr.indexOf("suÃ­te");
						
						String aux2 = auxStr.substring( idSuite-20 , idSuite );
						if ( aux2.contains("sendo") )	{
							aux2 = aux2.replaceAll("\\s","");	    // remove brancos
							aux2 = aux2.replaceAll("[^0-9.]", "");		// remove não numéricos
							
							data.setSuitesNum( Integer.parseInt( aux2 ) );
							
							// apMainData.suitesNum = Integer.parseInt( aux2 );
							// out( "\n suites == " + aux2 );
						}
					}
				}
				
		
	}
	
	private void processaBanheiro( String pageCodeStr )				{
		
		rTagIni = "property-information__item icon-bathroom";
		rTagFim = "</span> banheiro</dd>";
		
		// Banheiro
		if ( pageCodeStr.contains( rTagIni ) ) 	{
			
			pTagIni = pageCodeStr.indexOf( rTagIni );
			
			if ( pageCodeStr.contains( rTagFim ) )	{ 
				pTagFim = pageCodeStr.indexOf(rTagFim );
				contStr = pageCodeStr.substring( pTagIni , pTagFim );
				contStr = contStr.replaceAll("\\s","");
				contStr = contStr.replaceAll("[^0-9.]", "");
				data.setBanheirosNum( Integer.parseInt( contStr ) );
			}
			// System.out.println( "banheiro = " + banheiroStr );
		}
		
	}
	
	// Informações presentes na DIV class ".property-information__collumn", n° de quartos, preço etc.
	public void getInfosGeraisAp( String pageCodeStr )						{
		
		String numsQuartosDiv = "</span> quarto"; // antes de quarto, se contém /span
		String suiteDiv = "suíte";
		String endDiv = "<cite>Você também pode gostar</cite>";	// Após as infos gerais
		
		String rTagIni = ""; // tag de referência inicial para montar a substring de interesse (aquela que contém os dados buscados)
		String rTagFim = ""; // tag de referência final
		int pTagIni;
		int pTagFim;
		
		String contStr = ""; 
		processaPreco( pageCodeStr );
		processaSubPreco( pageCodeStr );
		processaArea( pageCodeStr );
		processaQuartos( pageCodeStr );
		processaBanheiro( pageCodeStr );
		
		// out("====== Dados gerais ======= \n " + data + "\n ================== \n");
	}
	
	private static void out(String str)				{
		// System.out.print(str);
	}
	
	private static boolean containsOnlyNumbers(String str)	 {        

		if ( str == null || str.length() == 0 )
             return false;
		
        for (int i = 0; i < str.length(); i++) 		{
        	if ( !Character.isDigit(str.charAt(i)) )
               return false;
        }
	    return true;
	}	
}



