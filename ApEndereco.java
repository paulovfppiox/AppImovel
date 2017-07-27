
public class ApEndereco 		{
	
	private boolean ruaIsEmpty;
	private String rua;
	private String bairro;
	private int numImovel;
	
	public ApEndereco()	{
	}
	
	public boolean isRuaIsEmpty() {
		return ruaIsEmpty;
	}
	public void setRuaIsEmpty(boolean ruaIsEmpty) {
		this.ruaIsEmpty = ruaIsEmpty;
	}
	public String getRua() {
		return rua;
	}
	public void setRua(String rua) {
		this.rua = rua;
	}
	public String getBairro() 				{
		return bairro;
	}
	public void setBairro(String bairro) 		{
		this.bairro = bairro;
	}
	public int getNumImovel() 			 		{
		return numImovel;
	}
	public void setNumImovel(int numImovel) 	{
		this.numImovel = numImovel;
	}
	public ApEndereco(boolean ruaIsEmpty, String rua, String bairro, int numImovel) 		{
		super();
		this.ruaIsEmpty = ruaIsEmpty;
		this.rua = rua;
		this.bairro = bairro;
		this.numImovel = numImovel;
	}
	
	@Override
	public String toString() 				{
		return "ApEndereco [ruaIsEmpty=" + ruaIsEmpty + ", rua=" + rua + ", bairro=" + bairro + ", numImovel="
				+ numImovel + "]";
	}
	
}
