
package contabancaria;

import java.util.Scanner;
import geradornumeros.GeradorNumerosFormatados;

public abstract class ContaBancaria {
    final static Scanner scanner = new Scanner(System.in);
    private String name;
    private  String conta;
    private  String agencia;
    private double saldo;
    private double deposito;
    private double saque;
    private double boleto;


    // Construtor
    public ContaBancaria(String name, String conta, String agencia, double saldo, double deposito, double saque, double boleto) {
        this.name = name;
        this.conta = conta;
        this.agencia = agencia;
        this.saldo = saldo;
        this.deposito = deposito;
        this.saque = saque;
        this.boleto = boleto;
    }

    public void criarConta(){
        double primeiroDeposito;

        System.out.println("Seja bem-vindo");
        System.out.println("==================");
        System.out.println("Insira seu nome :");

        this.name = scanner.nextLine().trim();

        this.conta=GeradorNumerosFormatados.gerarNumeroConta();
        this.agencia = GeradorNumerosFormatados.gerarNumeroAgencia();

        System.out.println("Sua conta foi criada com sucesso, veja seus dados abaixo:");
        System.out.println("Nome: " + this.name);
        System.out.println("Conta: " + this.conta);
        System.out.println("Agência: " + this.agencia);
        System.out.println("=========================");

        System.out.println("Realize seu primeiro depósito para ter acesso a sua conta");
        System.out.println("Digite o valor do depósito");
        
        primeiroDeposito = scanner.nextDouble();
        scanner.nextLine();
        configurarConta(primeiroDeposito);
    }

    public String getName() {
        return name;
    }

    public String getConta() {
        return conta;
    }
    public String getAgencia() {
        return agencia;
    }
    public double getSaldo() {
        return saldo;
    }

    public double getDeposito() {
        return deposito;
    }

    public double getSaque() {
        return saque;
    }

    public double getBoleto() {
        return boleto;
    }

    
    
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public void setBoleto(double boleto) {
        this.boleto = boleto;
    }

    public void setName(String name) {
        this.name = name;
    }


    public abstract void configurarConta(double primeiroDeposito);

    public boolean autorizarAcesso(String nome, String numero, String agencia) {
        if (this.name.equalsIgnoreCase(nome) && this.conta.equalsIgnoreCase(numero) && this.agencia.equalsIgnoreCase(agencia)) {
            System.out.println("Acesso autorizado!");
            return true;
        } else {
            System.out.println("Informações inválidas. Acesso negado.");
            return false;
        }
    }

    public boolean depositando() {
        System.out.println("Informe o valor do depósito:");
        this.deposito = scanner.nextDouble();

        if (this.deposito <= 0) {
            System.out.println("Valor de depósito inválido!");
            return false;
        }

        return true;
    }
    public boolean sacando(){
        System.out.println("Informe o valor do saque:");
        this.saque = scanner.nextDouble();

        if (this.saque <= 0) {
            System.out.println("Valor de saque inválido!");
            return false;
        }
        
        if (this.saque > this.saldo) {
            System.out.println("Saldo insuficiente!");
            return false;
            
        } else {
            this.saldo -= this.saque;
            System.out.println("Saque realizado com sucesso!");
            System.out.println("Saldo atual: R$"+ String.format("%.2f", this.saldo));
            return true;
        }

    }
    public boolean pagandoBoleto(){
        System.out.println("Informe o valor do boleto:");
        this.boleto = scanner.nextDouble();

        if(this.boleto <=0){
            System.out.println("Valor de boleto inválido!");
            return false;
        }
        if (this.boleto<=this.saldo){
            this.saldo -= this.boleto;
            System.out.println("Boleto pago com sucesso!");
            return true;
        }
        System.out.println("Saldo insuficiente!");
        return false;
    }    


}
