
public class ApMainData {
	
	public String precoS;
	public String precoTitle;
	public double preco;
	public double condominio;
	public double iptu;
	public int banheirosNum = -1;
	public int quartosNum = -1;
	public int area;
	public int suitesNum = -1;
	
	public ApMainData() {
		super();
	}
	public ApMainData(String precoS, String precoTitle, double preco, double condominio, double iptu, int banheirosNum,
			int quartosNum, int area, int suitesNum) {
		super();
		this.precoS = precoS;
		this.precoTitle = precoTitle;
		this.preco = preco;
		this.condominio = condominio;
		this.iptu = iptu;
		this.banheirosNum = banheirosNum;
		this.quartosNum = quartosNum;
		this.area = area;
		this.suitesNum = suitesNum;
	}
	@Override
	public String toString() {
		return "ApMainData [precoS=" + precoS + ", precoTitle=" + precoTitle + ", preco=" + preco + ", condominio="
				+ condominio + ", iptu=" + iptu + ", banheirosNum=" + banheirosNum + ", quartosNum=" + quartosNum
				+ ", area=" + area + ", suitesNum=" + suitesNum + "]";
	}
	public String getPrecoS() {
		return precoS;
	}
	public void setPrecoS(String precoS) {
		this.precoS = precoS;
		this.preco = Double.parseDouble( precoS );
		
	}
	public String getPrecoTitle() {
		return precoTitle;
	}
	public void setPrecoTitle(String precoTitle) {
		this.precoTitle = precoTitle;
	}
	public double getPreco() {
		return preco;
	}
	public void setPreco(double preco) {
		this.preco = preco;
	}
	public double getCondominio() {
		return condominio;
	}
	public void setCondominio(double condominio) {
		this.condominio = condominio;
	}
	public double getIptu() {
		return iptu;
	}
	public void setIptu(double iptu) {
		this.iptu = iptu;
	}
	public int getBanheirosNum() {
		return banheirosNum;
	}
	public void setBanheirosNum(int banheirosNum) {
		this.banheirosNum = banheirosNum;
	}
	public int getQuartosNum() {
		return quartosNum;
	}
	public void setQuartosNum(int quartosNum) {
		this.quartosNum = quartosNum;
	}
	public int getArea() {
		return area;
	}
	public void setArea(int area) {
		this.area = area;
	}
	public int getSuitesNum() {
		return suitesNum;
	}
	public void setSuitesNum(int suitesNum) {
		this.suitesNum = suitesNum;
	}
	
	
	

}
