import java.nio.charset.Charset;

public class ConversorCharsets 	{
	
	private final Charset UTF8_CHARSET = Charset.forName("UTF-8");
	public ConversorCharsets() {
	}
	
	String decodeUTF8(byte[] bytes) {
	    return new String(bytes, UTF8_CHARSET);
	}
	
	byte[] encodeUTF8(String string) {
	    return string.getBytes(UTF8_CHARSET);
	}
	
	public static void main( String [] args ) 	{
		ConversorCharsets p = new ConversorCharsets();
		// String s = s"Banc√°rios", "UTF-8"
		byte[] x = p.encodeUTF8("Banc·rios");
		System.out.println("1 == " + x[0] + x[1] + x[2] + x[3] + x[4] + x[5] + x[6] + x[7]); 
		System.out.println("2 == " + p.decodeUTF8(x));
	}
}