import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;

import base.Cliente;
import base.Entrega;
import controle.CadastroEntregas;
import controle.Clientela;

class mainGeral {
	public static void main(String[] args) {
		new ACMEDelivery().executa();
	}
}
class ACMEDelivery {

	private Scanner entrada;
	private PrintStream saidaPadrao = System.out;
	private Clientela clientela;
	private CadastroEntregas cadastroEntregas;

	public ACMEDelivery() {
		try {
			BufferedReader streamEntrada = new BufferedReader(new FileReader("arquivoentrada.txt"));
			entrada = new Scanner(streamEntrada);
			PrintStream streamSaida = new PrintStream("arquivosaida.txt", StandardCharsets.UTF_8);
			System.setOut(streamSaida);
		} catch (Exception e) {
			System.out.println(e);
		}
		Locale.setDefault(Locale.ENGLISH);

		clientela = new Clientela();
		cadastroEntregas = new CadastroEntregas();
	}

	private void restauraES() {
		System.setOut(saidaPadrao);
		entrada = new Scanner(System.in);
	}

	public void executa() {
		getAll();
		restauraES();
		pontoExtra();
	}

	private void pontoExtra() {
		try {
			menu();
			int opcao = entrada.nextInt();
			while (opcao!=0) {
				switch (opcao) {
					case 1:
						opcao1();
						break;
					case 2:
						opcao2();
						break;
					case 3:
						opcao3();
						break;
					case 4:
						System.out.println(clientela.toString());
						break;
					case 5:
						System.out.println(cadastroEntregas.toString());
						break;
					case 6:
						System.out.println(clientela.toString());
						System.out.println(cadastroEntregas.toString());
						break;
					default:
						break;
				}
				menu();
				opcao = entrada.nextInt();
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		} finally {
		entrada.close();
		}
	}

	private void getAll() {
		cadastraCliente();
		cadastraEntrega();
		quantidadeClientes();
		quantidadeEntregas();
		verCliente();
		verEntrega();
		dadosEntregaCliente();
		entregaMaiorValor();
		enderecoEntrega();
		somatorioCliente();
	}

	private void cadastraCliente() {
		String email, nome, end;

		email = entrada.nextLine();
		while (!email.equals("-1")) {
			nome = entrada.nextLine();
			end = entrada.nextLine();

			if(clientela.cadastraCliente(new Cliente(nome, email, end))) {
				System.out.println(String.format("1; %s; %s; %s", email, nome, end));
			}
			email = entrada.nextLine();
		}
	}

	private void cadastraEntrega() {

		int codigo;
		double preco;
		String desc;
		String email;
		codigo = entrada.nextInt();

		while (codigo != -1) {
			preco = entrada.nextDouble();
			entrada.nextLine();
			desc = entrada.nextLine();
			email = entrada.nextLine();

			if(cadastroEntregas.cadastraEntrega(new Entrega(codigo, preco, desc, clientela.pesquisaCliente(email)))) {
				System.out.println("2;" + codigo + ";" + preco + ";" + desc + ";" + email);
			}

			codigo = entrada.nextInt();
		}
	}

	private void quantidadeClientes() {
		System.out.println("3;" + clientela.getListaCliente().size());
	}

	private void quantidadeEntregas() {
		System.out.println("4;" + cadastroEntregas.getListaEntregas().size());
	}

	private void verCliente() {
		boolean ver = true;
		entrada.nextLine();
		String email = entrada.nextLine();
		for(Cliente x : clientela.getListaCliente()) {
			if(email.equalsIgnoreCase(x.getEmail())) {
				System.out.println("5;" + x.getEmail() + ";" + x.getNome() + ";" + x.getEndereco());
				ver = false;
			}
		}
		if(ver) {
			System.out.println("5;Cliente inexistente");
		}
	}

	private void verEntrega() {
		boolean ver = true;
		int codigo = entrada.nextInt();
		for(Entrega e : cadastroEntregas.getListaEntregas()) {
			if(codigo == e.getCodigo()) {
				System.out.println("6;" + e.getCodigo() + ";" + e.getValor() + ";" + e.getDescricao() + ";" + e.getCliente().getEmail() + ";" + e.getCliente().getNome() + ";" + e.getCliente().getEndereco());
				ver = false;
			}
		}
		if(ver) {
			System.out.println("6;Entrega inexistente");
		}
	}

	private void dadosEntregaCliente() {
		boolean ver = true;
		entrada.nextLine();
		String email = entrada.nextLine();
		for(Cliente c : clientela.getListaCliente()) {
			if(email.equalsIgnoreCase(c.getEmail())) {
				c.retornaDadosEntrega();
				ver = false;
			}
		}
		if(ver) {
			System.out.println("7;Cliente inexistente");
		}
	}

	private void entregaMaiorValor() {
		Entrega a = new Entrega(0,0,"", null);
		double maiorValor = 0;
		if(cadastroEntregas.getListaEntregas().isEmpty()) System.out.println("8;Entrega inexistente");
		for(Entrega e : cadastroEntregas.getListaEntregas()) {
			if(e.getValor()>maiorValor) {
				a=e;
				maiorValor=e.getValor();
			}
		}
		System.out.println("8;" + a.getCodigo() + ";" + a.getValor() + ";" + a.getDescricao());
	}

	private void enderecoEntrega() {
		boolean ver = true;
		double codigo = entrada.nextDouble();
		for(Entrega e : cadastroEntregas.getListaEntregas()) {
			if(codigo == e.getCodigo()) {
				System.out.println("9;" + e.getCodigo() + ";" + e.getValor() + ";" + e.getDescricao() + ";" + e.getCliente().getEndereco());
				ver = false;
			}
		}
		if(ver) {
			System.out.println("9;Entrega inexistente");
		}
	}

	private void somatorioCliente() {
		boolean ver = true;
		entrada.nextLine();
		String email = entrada.nextLine();
			for(Cliente c : clientela.getListaCliente()) {
				if(email.equalsIgnoreCase(c.getEmail())) {
					ver=false;
					if(c.temEntregas()) {
						String x = "10;" + c.getEmail() + ";" + c.getNome() + ";";
						String fim = String.format(x + "%.2f" , c.somatorioEntregas());
						System.out.printf(fim);
					} else {
						System.out.println("10;Entrega Inexistente");
					}
				}
			}
			if(ver) {
				System.out.println("10;Cliente inexistente");
			}

	}
	private void menu() {
		System.out.println("Insira a opcao desejada:");
		System.out.println("[1] Cadastrar um novo cliente. \n" +
				"[2] Cadastrar uma nova entrega. \n" +
				"[3] Cadastrar um novo cliente e uma entrega correspondente. \n" +
				"[4] Mostrar todos os clientes.  \n" +
				"[5] Mostrar todas as entregas. \n" +
				"[6] Mostrar todas informacoes do sistema. \n" +
				"[0] Sair do sistema." );
	}

	private void opcao1() {
		System.out.println("Insira o Nome, Email e Endereco do cliente desejado: ");
		entrada.nextLine();
		String nome = entrada.nextLine();
		String email = entrada.nextLine();
		String ende = entrada.nextLine();
		if(clientela.cadastraCliente(new Cliente(nome, email, ende))) {
			System.out.println("Cliente cadastrado!");
		} else {
			System.out.println("Cliente invalido!");
		}
	}

	private void opcao2() {
		System.out.println("Insira o Codigo, Preco, Descricao e o Email do cliente da entrega");
		int codigo = entrada.nextInt();
		double preco = entrada.nextDouble();
		entrada.nextLine();
		String desc = entrada.nextLine();
		String email = entrada.nextLine();
		Cliente c = clientela.pesquisaCliente(email);
		Entrega e = new Entrega(codigo, preco, desc, c);
		if(cadastroEntregas.cadastraEntrega(e)) {
			c.adicionaEntrega(e);
			System.out.println("Entrega cadastrada!");
		} else {
			System.out.println("Entrega invalida!");
		}
	}

	private void opcao3() {
		System.out.println("Insira o nome, email e endereco em ordem, para o seu cliente:");
		entrada.nextLine();
		String nome = entrada.nextLine();
		String email = entrada.nextLine();
		String endereco = entrada.nextLine();
		Cliente c = new Cliente(nome, email, endereco);
		if(clientela.cadastraCliente(c)) {
			System.out.println("Cliente cadastrado!");
		} else {
			System.out.println("Cliente invalido");
		}
		System.out.println("Insira uma entrega correspondente (codigo, preco, descricao): ");
		int codigo = entrada.nextInt();
		double preco = entrada.nextDouble();
		entrada.nextLine();
		String desc = entrada.nextLine();
		System.out.println(desc);
		Entrega e = new Entrega(codigo, preco, desc, c);
		if(cadastroEntregas.cadastraEntrega(e)) {
			System.out.println("Entrega registrada!");
			c.adicionaEntrega(e);

		} else {
			System.out.println("Entrega invalida!");
		}
	}


}
