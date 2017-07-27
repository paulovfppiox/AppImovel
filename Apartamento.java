
public class Apartamento 			{

	public VantagensAp vantagens;
    public ApEndereco endereco;
	public ApMainData dadosGerais;
	
	public Apartamento() {
		super();
	}
	public Apartamento(VantagensAp vantagens, ApEndereco endereco, ApMainData dadosGerais) {
		super();
		this.vantagens = vantagens;
		this.endereco = endereco;
		this.dadosGerais = dadosGerais;
	}
	
	public ApEndereco getEndereco() {
		return endereco;
	}
	public void setEndereco(ApEndereco endereco) {
		this.endereco = endereco;
	}
	public VantagensAp getVantagens() {
		return vantagens;
	}
	public void setVantagens(VantagensAp vantagens) {
		this.vantagens = vantagens;
	}
	public ApMainData getDadosGerais() {
		return dadosGerais;
	}
	public void setDadosGerais(ApMainData dadosGerais) {
		this.dadosGerais = dadosGerais;
	}
	@Override
	public String toString() {
		return "Apartamento [vantagens=" + vantagens + ", endereco=" + endereco + ", dadosGerais=" + dadosGerais + "]";
	}
	
}
