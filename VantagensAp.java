
public class VantagensAp 			{
	
	private int numSuites = -1;
	private int elevador = -1;
	private int gasEncanado = -1;
	private int piscina = -1;
	private int festas = -1;
	private int academia = -1;
	private int wcSocial = -1;
	private int areaServ = -1;
	private int areaLazer = -1;
	private int playground = -1;
	private int salaoFesta = -1;
	private int churrasco = -1;
	private int varanda = -1;
	private int condFechado = -1;
	private int deposito = -1;
	private String posicao = null;
	private int pertoDeShopping = -1; // perto de shopping
	private int quadraEsporte = -1;   // esporte
	
	
	public String getPosicao() 			{
		return posicao;
	}
	public void setPosicao(String str)	{
		this.posicao = str;
	}
	
	public int getNumSuites() 			{
		return numSuites;
	}
	public void setNumSuites(int numSuites) {
		this.numSuites = numSuites;
	}
	public int getElevador() 			{
		return elevador;
	}
	public void setElevador(int elevador) {
		this.elevador = elevador;
	}
	public int getGasEncanado() 		{
		return gasEncanado;
	}
	public void setGasEncanado(int gasEncanado) {
		this.gasEncanado = gasEncanado;
	}
	public int getPiscina()				{
		return piscina;
	}
	public void setPiscina(int piscina) {
		this.piscina = piscina;
	}
	public int getFestas() 		{
		return festas;
	}
	public int getVaranda() 	{
		return this.varanda;
	}
	
	
	// --------------------------------
	public void setVaranda(int v) {
		this.varanda = v;
	}
	
	public void setFestas(int festas) {
		this.festas = festas;
	}
	public int getAcademia() {
		return academia;
	}
	public void setAcademia(int academia) {
		this.academia = academia;
	}
	public int getWcSocial() {
		return wcSocial;
	}
	public void setWcSocial(int wcSocial) {
		this.wcSocial = wcSocial;
	}
	public int getAreaServ() {
		return areaServ;
	}
	public void setAreaServ(int areaServ) {
		this.areaServ = areaServ;
	}
	public int getAreaLazer() {
		return areaLazer;
	}
	public void setAreaLazer(int areaLazer) {
		this.areaLazer = areaLazer;
	}
	public int getPlayground() {
		return playground;
	}
	public void setPlayground(int playground) {
		this.playground = playground;
	}
	public int getSalaoFesta() {
		return salaoFesta;
	}
	public void setSalaoFesta(int salaoFesta) {
		this.salaoFesta = salaoFesta;
	}
	public int getChurrasco() {
		return churrasco;
	}
	public void setChurrasco(int churrasco) {
		this.churrasco = churrasco;
	}
	public int getCondFechado() {
		return condFechado;
	}
	public void setCondFechado(int condFechado) {
		this.condFechado = condFechado;
	}
	public int getDeposito() {
		return deposito;
	}
	public void setDeposito(int deposito) {
		this.deposito = deposito;
	}
	
	public int getPertoDeShopping() {
		return pertoDeShopping;
	}
	public void setPertoDeShopping(int pertoDeShopping) {
		this.pertoDeShopping = pertoDeShopping;
	}
	public int getQuadraEsporte() {
		return quadraEsporte;
	}
	public void setQuadraEsporte(int quadraEsporte) {
		this.quadraEsporte = quadraEsporte;
	}
	
	
	@Override
	public String toString() {
		return "VantagensAp [numSuites=" + numSuites + ", elevador=" + elevador + ", gasEncanado=" + gasEncanado
				+ ", piscina=" + piscina + ", festas=" + festas + ", academia=" + academia + ", wcSocial=" + wcSocial
				+ ", areaServ=" + areaServ + ", areaLazer=" + areaLazer + ", playground=" + playground + ", salaoFesta="
				+ salaoFesta + ", churrasco=" + churrasco + "]";
	}
	
	public VantagensAp(int numSuites, int elevador, int gasEncanado, int piscina, int festas, int academia,
			int wcSocial, int areaServ, int areaLazer, int playground, int salaoFesta, int churrasco, int varanda,
			int condFechado, int deposito) {
		super();
		this.numSuites = numSuites;
		this.elevador = elevador;
		this.gasEncanado = gasEncanado;
		this.piscina = piscina;
		this.festas = festas;
		this.academia = academia;
		this.wcSocial = wcSocial;
		this.areaServ = areaServ;
		this.areaLazer = areaLazer;
		this.playground = playground;
		this.salaoFesta = salaoFesta;
		this.churrasco = churrasco;
		this.varanda = varanda;
		this.condFechado = condFechado;
		this.deposito = deposito;
	}
	public VantagensAp() {
		super();
	}
	
	
	

}