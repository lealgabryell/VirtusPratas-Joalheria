package classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class GerenciadorArq {
	private String tipo;
	private String nome;
	private int quantidade;
	private double dinheiroEnvolvido;
	private double saldoAtual;

	public GerenciadorArq(String tipo, int unidades, String nome, double dinheiro, double saldoAtual) {
		this.tipo = tipo;
		this.nome = nome;
		this.quantidade = unidades;
		this.dinheiroEnvolvido = dinheiro;
		this.saldoAtual = saldoAtual;
	}

	// retorna uma lista com todos uma lista de todos os relatorios
	public static ArrayList<GerenciadorArq> getDoRelatorio(File arquivo) {
		ArrayList<GerenciadorArq> relatorios = new ArrayList<>();
		try {
			Scanner sc = new Scanner(arquivo);
			if (sc.hasNext()) {
				while (sc.hasNextLine()) {
					String[] relatorio = sc.nextLine().split(",");
					relatorios.add(new GerenciadorArq(relatorio[0], Integer.parseInt(relatorio[1]),
							relatorio[2], Double.parseDouble(relatorio[3]), Double.parseDouble(relatorio[4])));
				}

				sc.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo não encontrado.");
		}
		return relatorios;
	}

	// adiciona um relatorio novo à lista
	public static void addAoRelatorio(File arquivo, ArrayList<GerenciadorArq> relatorio) {
		try {
			PrintWriter writer = new PrintWriter(arquivo);
			ArrayList<GerenciadorArq> r1 = getDoRelatorio(arquivo);
			for (GerenciadorArq r : r1) {
				relatorio.add(r);
			}
			for (GerenciadorArq novo : relatorio) {
				writer.write(novo.getTipo() + "," + novo.getQuantidade() + "," + novo.getNome() + ","
						+ novo.getDinheiroEnvolvido() + "," + novo.getSaldoAtual() + "\n");
			}
			writer.close();
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo não encontrado.");
		}
	}

	public static double getArqSaldo(File saldo) {
		double ultimoSaldo = 0;
		try {
			Scanner sc = new Scanner(saldo);
			if (sc.hasNext()) {
				while (sc.hasNextLine()) {
					String[] relatorio = sc.nextLine().split(",");
					ultimoSaldo = Double.parseDouble(relatorio[0]);
				}
				sc.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo não encontrado.");
		}
		return ultimoSaldo;
	}

	public static void addNovoSaldo(File saldo, ArrayList<GerenciadorArq> cadastrados) {
		double saldoAtual = cadastrados.get(cadastrados.size() - 1).getSaldoAtual();
		try {
			PrintWriter writer = new PrintWriter(saldo);
			String info = Double.toString(saldoAtual);
			writer.write(info);
			writer.close();
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo não encontrado.");
		}
	}

	public String getTipo() {
		return tipo;
	}

	public String getNome() {
		return nome;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public double getSaldoAtual() {
		return saldoAtual;
	}

	public double getDinheiroEnvolvido() {
		return dinheiroEnvolvido;
	}
}