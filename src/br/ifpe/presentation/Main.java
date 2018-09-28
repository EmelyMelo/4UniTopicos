package br.ifpe.presentation;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


import br.ifpe.entities.Account;
import br.ifpe.entities.AccountEnum;
import br.ifpe.entities.Client;
import br.ifpe.service.BankService;
import br.ifpe.service.ServiceFactory;

/**
 * OBSERVAÇÕES: 
 * NÃO é permitido o uso de nenhuma estrutura de repetição (for, while, do-while).
 * Tente, ao máximo, evitar o uso das estruturas if, else if, else e switch-case
 * 
 * @author Victor Lira
 *
 */
public class Main {
	
	private static BankService service = ServiceFactory.getService();
	
	public static void main(String[] args) {
		/*OK
		imprimirNomesClientes();		
		imprimirSaldoMedio(8);	
		imprimirClientesComPoupanca();
		
		*/
	

	}
	
	/**
	 * 1. Imprima na tela o nome e e-mail de todos os clientes (sem repetição), usando o seguinte formato:
	 * Victor Lira - vl@cin.ufpe.br
	 */
	public static void imprimirNomesClientes() {		//OK
		service
			.listClients()
			.stream()
			.map(c -> c.getName() + ": " + c.getEmail())
			.distinct()
			.forEach(n -> System.out.println(n));
	}
	
	/**
	 * 2. Imprima na tela o nome do cliente e a média do saldo de suas contas, ex:
	 * Victor Lira - 352
	 */
	public static void imprimirMediaSaldos() {		//OK
		service
			.listClients()
			.stream()
			.map(cliente -> cliente.getName() + " - " + service
															.listAccounts()
															.stream()
															.filter(conta -> conta.getClient().getName().equals(cliente.getName()))
															.mapToDouble(conta -> conta.getBalance())
															.average()
															.getAsDouble())
			.forEach(System.out::println);
	}
	
	/**
	 * 3. Considerando que só existem os países "Brazil" e "United States", 
	 * imprima na tela qual deles possui o cliente mais rico, ou seja,
	 * com o maior saldo somando todas as suas contas.
	 */
	public static void imprimirPaisClienteMaisRico() {		//OK
			double saldoBrazil	= service
					.listAccounts()
					.stream()
					.filter(conta -> conta.getClient().getAddress().getCountry().equals("Brazil"))
					.mapToDouble(conta -> conta.getBalance())
					.sum();

			double saldoUSA = service
					.listAccounts()
					.stream()
					.filter(conta -> conta.getClient().getAddress().getCountry().equals("United States"))
					.mapToDouble(conta -> conta.getBalance())
					.sum();
			
			System.out.println(Double.compare(saldoBrazil, saldoUSA));

		}
	
	/**
	 * 4. Imprime na tela o saldo médio das contas da agência
	 * @param agency
	 */
	public static void imprimirSaldoMedio(int agency) {		//OK
		double saldos = service
			.listAccounts()
			.stream()
			.filter(conta -> conta.getAgency() == agency)
			.mapToDouble(conta -> conta.getBalance())
			.average()
			.getAsDouble();
		System.out.println(saldos);
	}
	
	/**
	 * 5. Imprime na tela o nome de todos os clientes que possuem conta poupança (tipo SAVING)
	 */
	public static void imprimirClientesComPoupanca() {		//OK
		service
			.listAccounts()
			.stream()
			.filter(conta -> conta.getType().equals(AccountEnum.SAVING))
			.forEach(conta -> System.out.println(conta.getClient().getName()));
	}
	
	/**
	 * 6.
	 * @param agency
	 * @return Retorna uma lista de Strings com o "estado" de todos os clientes da agência
	 */
	public static List<String> getEstadoClientes(int agency) {		//OK
		return service
			.listAccounts()
			.stream()
			.filter(conta -> conta.getAgency() == agency)
			.map(conta -> conta.getClient().getAddress().getState())
			.collect(Collectors.toList());
		}
	
	/**
	 * 7.
	 * @param country
	 * @return Retorna uma lista de inteiros com os números das contas daquele país
	 */
	public static int[] getNumerosContas(String country) {		//OK
		return service
				.listAccounts()
				.stream()
				.filter(conta -> conta.getClient().getAddress().getCountry().equals(country))
				.mapToInt(conta -> conta.getNumber())
				.toArray();
	}
	
	/**
	 * 8.
	 * Retorna o somatório dos saldos das contas do cliente em questão 
	 * @param clientEmail
	 * @return
	 */
	public static double getMaiorSaldo(String clientEmail) {		//OK
		return service
				.listAccounts()
				.stream()
				.filter(conta -> conta.getClient().getEmail().equals(clientEmail))
				.mapToDouble(conta -> conta.getBalance())
				.sum();
				
	}
	
	/**
	 * 9.
	 * Realiza uma operação de saque na conta de acordo com os parâmetros recebidos
	 * @param agency
	 * @param number
	 * @param value
	 */
	public static void sacar(int agency, int number, double value) {    //OK
		service
			.listAccounts()
			.stream()
			.filter(conta -> conta.getAgency() == agency && conta.getNumber() == number)
			.forEach(conta -> conta.setBalance(conta.getBalance() - value));			
	}
	
	/**
	 * 10. Realiza um deposito para todos os clientes do país em questão	
	 * @param country
	 * @param value
	 */
	public static void depositar(String country, double value) {		//OK
		service
			.listAccounts()
			.stream()
			.filter(conta -> conta.getClient().getAddress().getCountry().equals(country))
			.forEach(cliente -> cliente.setBalance(cliente.getBalance() + value));
	}
	
	/**
	 * 11. Realiza uma transferência entre duas contas de uma agência.
	 * @param agency - agência das duas contas
	 * @param numberSource - conta a ser debitado o dinheiro
	 * @param numberTarget - conta a ser creditado o dinheiro
	 * @param value - valor da transferência
	 */
	public static void transferir(int agency, int numberSource, int numberTarget, double value) {		//OK
		service
			.listAccounts()
			.stream()
			.filter(conta -> conta.getAgency() == agency && conta.getNumber() == numberSource)
			.forEach(conta -> conta.setBalance(conta.getBalance() - value));
		service
			.listAccounts()
			.stream()
			.filter(conta -> conta.getAgency() == agency && conta.getNumber() == numberTarget)
			.forEach(conta -> conta.setBalance(conta.getBalance() + value));
	}
	
	/**
	 * 12.
	 * @param clients
	 * @return Retorna uma lista com todas as contas conjuntas (JOINT) dos clientes
	 */
	public static List<Account> getContasConjuntas(List<Client> clients) {		//OK
		List<Account> contasConjuntas = new ArrayList<Account>();
		service
			.listAccounts()
			.stream()
			.filter(conta -> conta.getClient().equals(clients))
			.map(conta -> conta.getType().equals(AccountEnum.JOINT))
			.collect(Collectors.toList());
		
		return contasConjuntas;
	}
	
	/**
	 * 13.
	 * @param state
	 * @return Retorna uma lista com o somatório dos saldos de todas as contas do estado 
	 */
	public static double getSomaContasEstado(String state) {		//OK
		DoubleSummaryStatistics soma = service
				.listAccounts()
				.stream()
				.filter(conta -> conta.getClient().getAddress().getState().equals(state))
				.collect(Collectors.summarizingDouble(Account::getBalance));
		
		return soma.getSum();
		
	}
	
	/**
	 * 14.
	 * @return Retorna um array com os e-mails de todos os clientes que possuem contas conjuntas
	 */
	public static String[] getEmailsClientesContasConjuntas() {		//OK
		return service
			.listAccounts()
			.stream()
			.filter(conta -> conta.getType().equals(AccountEnum.JOINT))
			.map(conta -> conta.getClient().getEmail())
			.toArray(String[]::new);
	}
	
	/**
	 * 15.
	 * @param number
	 * @return Retorna se o número é primo ou não
	 */
	public static boolean isPrimo(int number) {		//OK	
		return IntStream.rangeClosed(2, (number/2)).noneMatch(x -> number % x == 0);
	}
	
	/**
	 * 16.
	 * @param number
	 * @return Retorna o fatorial do número
	 */
	public static int getFatorial(int number) {		//OK	
		return IntStream.range(1, number).reduce(1, (n1,n2) -> (n1*n2));
	}
}
