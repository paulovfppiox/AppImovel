import java.util.*;
import java.lang.*;
import java.net.*;
import java.io.*;
import java.nio.charset.Charset;

public class Acento			{

	public static void main( String [] args ) 	{

		byte[] bytes = { (byte)0xc3, (byte)0xa2, 0x61, 0x62, 0x63, 0x64 };
	    Charset utf = Charset.forName("UTF-8");
	    Charset iso = Charset.forName("ISO-8859-1");
	
	    String str = "Bancários";
	    String s = new String(str.getBytes(), utf );
	    System.out.println( s );
	    
	    
	    
	    String string = new String ( bytes, utf );
	    System.out.println(string);
	
	    // "When I do a getbytes(encoding) and "
	    byte[] isoBytes = string.getBytes(iso);
	
	    for ( byte b : isoBytes )
	        System.out.printf("%02x ", b);
	
	    System.out.println();
	
	    // "then create a new string with the bytes in ISO-8859-1 encoding"
	    String string2 = new String ( isoBytes, iso );
	
	    // "I get a two different chars"
	    System.out.println(string2);
	}
	    
}
