import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import jxl.read.biff.BiffException;

import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import java.util.*;

public class GeraPlanilha
{
	
   private String sheetName = "Planilha";	
   private WritableWorkbook wworkbook;
   private WritableSheet wsheet;
   private String outFile; // = "C:/Users/Bruno Paiva/workspace/AppImovel/apps3.xls";
	
   public GeraPlanilha() {}
   public GeraPlanilha( String outFile )		{
	   System.out.println( "Criando planilha ..." );
	   this.outFile = outFile;
	   this.createXLS();
   }
   private void createXLS()		{
	   	  try		  {	
	   		  wworkbook = Workbook.createWorkbook( new File(outFile) );
	   		  wsheet = wworkbook.createSheet(sheetName, 0);
	   	  } catch ( FileNotFoundException exp )	{ 
	   		System.out.println( "O arquivo " + outFile + " não foi encontrado, é inexistente, ou está aberto por outro processo." );
	   	  } catch ( Exception exp )	{
			   System.out.println( "Deu erro ao criar planilha!! " );
			   exp.printStackTrace();
		  }
   }
   
   public void write()			{
	   try		  {	
		   wworkbook.write();
		   wworkbook.close();
	   } catch ( Exception exp )	{
		   System.out.println( "Erro ao gravar planilha!!" );
	   }
   }
   
   public void addNumCell( int x, int y, double val )		{
	   try		  {	
		   Number number = new Number( x, y, val );
		   wsheet.addCell(number);
	   } catch ( Exception exp )	{
		   System.out.println( "Erro ao add numcell" );
	   }
   }
   
   public void addTxtCell( int x, int y, String val )		{
	   try		  {
		   wsheet.addCell( new Label( x, y, val ) );
	   } catch ( Exception exp )	{
		   System.out.println( "Erro ao add txtcell" );
	   }
   }
   
   public void addColsLabels( ArrayList<String> labels, int fstColId )		{
	   
	   try {
		   
		   int colId;
		   Label l;
		   for ( colId = fstColId; colId <labels.size()-1; colId++ )	{
			   l = new Label(colId, 0, labels.get(colId));
			   wsheet.addCell(l);
		   }
		   
	   } catch (Exception exp)	{
		   System.out.println( "Erro em labels" );
		   exp.printStackTrace();
	  }
	   
   }
   
   /* Para testar
    * public static void main(String[] args)			{
   
		   GeraPlanilha planilha = new GeraPlanilha("Teste Aba");
		   ArrayList<String> labels = new  ArrayList<String>();
		   labels.add("URL");
		   labels.add("Preço");
		   labels.add("Condom.");
		   labels.add("IPTU"); 	      
		   labels.add("Banheiros"); 
		   labels.add("Quartos");
		   labels.add("Área");
		   labels.add("Elev.");
		   labels.add("Pisc.");
		   labels.add("Festas");
		   labels.add("Academ.");
		   labels.add("Lazer");
		   labels.add("Varanda");
		   labels.add("Posicao");
		   planilha.addColsLabels(labels);
		   planilha.write();

	     // Number number = new Number(3, 4, 3.1459);
	      // wsheet.addCell(number);
	   
	
	      /*
	       * Workbook workbook = Workbook.getWorkbook(new File(out));
	      Sheet sheet = workbook.getSheet(0);
	      Cell cell1 = sheet.getCell(0, 2);
	      System.out.println(cell1.getContents());
	      Cell cell2 = sheet.getCell(3, 4);
	      System.out.println(cell2.getContents());
	      workbook.close();
	      
   }*/
   
}