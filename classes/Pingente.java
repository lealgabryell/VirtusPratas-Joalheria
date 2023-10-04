package classes;

public class Pingente extends Item{
	private char tamanho;

	public Pingente(String nome, double custoCompra, double valorVenda, char tamanho) {
		super(nome, "Pingente", custoCompra, valorVenda);
		this.tamanho = tamanho;
	}

	public String atributesString() {
		String info = String.format("%20s (cód.: %10d | estoque: %2d | Tamanho: %s | custo de compra: %.2f | valor de venda: %.2f)\n",
				this.getNome(),
				this.getCodigo(),
				this.getEstoque(),
				this.getTamanho(),
				this.getCustoCompra(),
				this.getValorVenda());
		return info;
	}
	
	public char getTamanho(){
		return tamanho;
	}
}
