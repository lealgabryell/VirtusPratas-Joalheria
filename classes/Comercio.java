package classes;

import java.io.File;
import java.util.ArrayList;

public class Comercio {
	private ArrayList<Item> produtos = new ArrayList<>();
	private ArrayList<GerenciadorArq> relatorios = new ArrayList<GerenciadorArq>();
	private File arquivo = new File("relatorio.txt");
	private File saldo = new File("saldo.txt");
	private double saldoCarteira = 5000.50;
	private ArrayList<GerenciadorArq> transacoesRecentes = new ArrayList<>();

	public void menu(int esc) {
		Uteis.mostrarLinha();
		switch (esc) {
			case 1:
				if (produtos.isEmpty()) {
					System.out.println("Nenhum item foi cadastrado na loja");
					break;
				} else {
					int tiposDeListagem = Uteis.leInt(
							"Opções de listagem:\n1) Listar todos os itens\n2) Listar itens de categória específica\nSua escolha: ");
					switch (tiposDeListagem) {
						case 1:
							Uteis.mostrarLinha();
							listar();
							break;
						case 2:
							int categoriasDeListagem = Uteis.leInt("Opções de listagem:\n"
									+ "1) Corrente\n"
									+ "2) Pulseira\n"
									+ "3) Brinco\n"
									+ "4) Pingente\n"
									+ "Sua escolha: ");
							Uteis.mostrarLinha();
							switch (categoriasDeListagem) {
								case 1:
									listar("Corrente");
									break;
								case 2:
									listar("Pulseira");
									break;
								case 3:
									listar("Brinco");
									break;
								case 4:
									listar("Pingente");
									break;
								default:
									System.out.println("Opção inválida.");
									break;
							}
							break;
						default:
							System.out.println("Opção inválida.");
					}
				}
				break;
			case 2:
				if (!relatorios.isEmpty()) {
					System.out.println("Seu saldo é: " + GerenciadorArq.getArqSaldo(saldo));
					saldoCarteira = GerenciadorArq.getArqSaldo(saldo);
				} else
					System.out.println("Seu saldo é: " + saldoCarteira);

				String nomeDoProduto = Uteis.leString("Digite o nome do acessório: ");
				Item selecionado = null;
				for (Item item : produtos) {
					if (item.getNome() == nomeDoProduto) {
						selecionado = item;
					}
				}
				if (selecionado != null) {
					System.out.println("Já temos um produto com esse nome. Cadastre um novo, ou adicione estoque!");
					break;
				}
				double valorDeCompra = Uteis.leDouble("Valor de compra: ");
				double valorDeVenda = Uteis.leDouble("Valor de venda: ");
				int categoria = Uteis.leInt(
						"Selecione a catégoria que se enquadre\n1) Corrente\n2) Pulseira\n3) Brinco\n4) Pingente\nDigite sua escolha: ");
				Uteis.mostrarLinha();
				Item novo = null;
				switch (categoria) {
					case 1:
						int tamanho = Uteis.leInt("Tamanho da corrente em cm: ");
						cadastrar(novo = new Corrente(nomeDoProduto, valorDeCompra, valorDeVenda, tamanho));
						break;
					case 2:
						tamanho = Uteis.leInt("Tamanho da pulseira em cm: ");
						cadastrar(novo = new Pulseira(nomeDoProduto, valorDeCompra, valorDeVenda, tamanho));
						break;
					case 3:
						String formato = Uteis.leString("Descreva o formato do brinco: ");
						cadastrar(novo = new Brinco(nomeDoProduto, valorDeCompra, valorDeVenda, formato));
						break;
					case 4:
						String tamanhoPingente = Uteis
								.leString("Digite o tamanho do pingente(Pequeno, Medio, Grande): ");
						cadastrar(novo = new Pingente(nomeDoProduto, valorDeCompra, valorDeVenda,
								tamanhoPingente.charAt(0)));
						break;
					default:
						System.out.println("Opção inválida.");
						break;
				}
				if (categoria > 4 || categoria < 0)
					break;
				Uteis.mostrarLinha();
				String escolha = Uteis.leString("Deseja adicionar estoque ao novo item? ");
				if (escolha.toLowerCase().startsWith("s")) {
					int cod = novo.getCodigo();
					int qtd = Uteis.leInt("Digite quantas unidades você deseja adicionar: ");
					adicionar(cod, qtd);
				} else
					System.out.println("Você está sendo redirecionado ao menu...");
				break;
			case 3:
				if (produtos.isEmpty()) {
					System.out.println("Nenhum item foi cadastrado na loja");
				} else {
					Item i = findCod(Uteis.leInt("Digite o código de acesso do produto: "));
					if (i == null) {
						System.out.println("Código inválido.\nNão temos nenhum item com esse acesso.");
					} else {
						int unidadesAModificar = Uteis.leInt("Digite quantas unidades você deseja adicionar: ");
						adicionar(i.getCodigo(), unidadesAModificar);
					}
				}
				break;
			case 4:
				if (produtos.isEmpty()) {
					System.out.println("Nenhum item foi cadastrado na loja");
				} else {
					while (true) {
						Item aRemover = findCod(Uteis.leInt("Digite o código do item: "));
						if (aRemover == null) {
							System.out.println("Código inválido.");
							break;
						} else {
							if (aRemover.getEstoque() > 0) {
								String confirmacao = Uteis
										.leString(aRemover.getNome() + " tem " + aRemover.getEstoque() +
												" unidades em estoque.\nDeseja remover mesmo assim? ");
								if ((confirmacao.toLowerCase()).startsWith("s")) {
									remover(aRemover);
									break;
								} else {
									System.out.println("Remoção cancelada. Você será redirecionado ao menu...");
									break;
								}
							} else {
								remover(aRemover);
								break;
							}
						}
					}
				}
				break;
			case 5:
				if (produtos.isEmpty())
					System.out.println("Nenhum item foi cadastrado na loja");
				else {
					Item i = findCod(Uteis.leInt("Digite o código de acesso do item: "));
					if (i == null) {
						System.out.println("Código inválido.\nNão temos nenhum item com esse acesso.");
					} else {
						int qtd = Uteis.leInt("Digite quantas unidades você deseja vender: ");
						if (qtd > i.getEstoque()) {
							System.out.println("Impossível vender " + qtd + " unidades de " + i.getNome() + ". "
									+ i.getEstoque() + " unidades disponíveis.");
						} else {
							System.out.printf("%d unidades de %s foram vendidas! Isso resultou em R$%.2f\n",
									qtd, i.getNome(), i.getValorVenda() * qtd);
							saldoCarteira += i.getValorVenda() * qtd;
							GerenciadorArq transacaoRecente = new GerenciadorArq("Venda", qtd, i.getNome(),
									qtd * i.getValorVenda(),
									saldoCarteira);
							transacoesRecentes.add(transacaoRecente);
							relatorios.add(transacaoRecente);
							GerenciadorArq.addAoRelatorio(arquivo, relatorios);
							GerenciadorArq.addNovoSaldo(saldo, relatorios);
							i.venderEstoque(qtd);
						}
					}
				}
				break;
			case 6:
				relatorios = GerenciadorArq.getDoRelatorio(arquivo);
				if (!relatorios.isEmpty()) {
					System.out.println("Aqui esta o historico geral das transacoes:");
					for (GerenciadorArq r : relatorios) {
						System.out.printf(r.getTipo() +
								" de " + r.getQuantidade() + " unidades de " + r.getNome() +
								". Dinheiro investido: R$%.2f\n", r.getDinheiroEnvolvido());
					}
					System.out.println("\nAqui esta o historico das transacoes recentes:");
					if (!transacoesRecentes.isEmpty()) {
						for (GerenciadorArq r : transacoesRecentes) {
							System.out.printf(r.getTipo() +
									" de " + r.getQuantidade() + " unidades de " + r.getNome() +
									". Dinheiro investido: R$%.2f\n", r.getDinheiroEnvolvido());
						}
					} else {
						System.out.println(
								"Hoje ainda nao foi feita nenhuma transacao!\nSe quiser fazer uma transacao, cadastre uma compra ou venda!");
					}
				} else {
					System.out.println("Nenhuma transação foi feita.");
				}
				System.out.printf("Seu saldo eh de: R$%.2f\n", relatorios.get(relatorios.size() - 1).getSaldoAtual());
				break;
			case 7:
				System.out.println("Saindo do sistema...");
				break;
			default:
				System.out.println("Opção inválida...");
		}
	}

	private void listar() {
		for (int i = 0; i < produtos.size(); i++) {
			System.out.printf("%d) %s", i + 1, produtos.get(i).toString());
		}
	}

	private void listar(String categoria) {
		int cont = 0;
		for (int i = 0; i < produtos.size(); i++) {
			if (produtos.get(i).getCategoria().equalsIgnoreCase(categoria)) {
				System.out.printf("%d) %s", i + 1, produtos.get(i).atributesString());
				cont = 1;
			}
		}
		if (cont == 0)
			System.out.println("Nenhum item da categoria '" + categoria + "' cadastrado no catálogo.");
	}

	private void cadastrar(Item novoProduto) {
		produtos.add(novoProduto);
		System.out.printf("%s cadastrado com sucesso! Código: %d, Estoque: %d\n",
				novoProduto.getNome(), novoProduto.getCodigo(), novoProduto.getEstoque());
	}

	private void adicionar(int cod, int qtd) {
		Item aReceberEstoque = findCod(cod);
		double valorDoCarrinho = qtd * aReceberEstoque.getCustoCompra();
		if (valorDoCarrinho > saldoCarteira) {
			System.out.println(
					"Saldo insuficiente.\nSaldo disponível: R$" + saldoCarteira + " Custo: R$" + valorDoCarrinho);
		} else {
			System.out.println("Estoque adicionado com sucesso!");
			saldoCarteira -= valorDoCarrinho;
			GerenciadorArq transacaoRecente = new GerenciadorArq("Compra", qtd, aReceberEstoque.getNome(),
					valorDoCarrinho, saldoCarteira);
			transacoesRecentes.add(transacaoRecente);
			relatorios.add(transacaoRecente);
			GerenciadorArq.addAoRelatorio(arquivo, relatorios);
			GerenciadorArq.addNovoSaldo(saldo, relatorios);
			aReceberEstoque.addEstoque(qtd);
		}
	}

	private void remover(Item item) {
		System.out.printf("%s e todo o seu estoque foi removido.\n", item.getNome());
		produtos.remove(item);
	}

	private Item findCod(int cod) {
		Item selecionado = null;
		for (Item item : produtos) {
			if (item.getCodigo() == cod) {
				selecionado = item;
			}
		}
		return selecionado;
	}

	public static void main(String[] args) {
		System.out.println("Seja bem-vindo à Virtus Pratas");
		Comercio c = new Comercio();
		int esc = 0;
		while (esc != 7) {
			System.out.printf(
					"------------------------%s------------------------\n1) Listar itens disponíveis.\n" +
							"2) Cadastrar um novo item.\n" + "3) Adicionar estoque.\n" +
							"4) Remover um item.\n" + "5) Vender estoque.\n" +
							"6) Relatório\n" +
							"7) Sair",
					"Menu");
			esc = Uteis.leInt("\nSua escolha: ");
			c.menu(esc);
		}
	}
}