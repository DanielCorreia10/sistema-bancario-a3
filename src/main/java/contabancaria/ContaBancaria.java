
package contabancaria;

import app.Main;
import java.util.Scanner;
import geradornumeros.GeradorNumerosFormatados;

public abstract class ContaBancaria {
    private final static Scanner scanner = new Scanner(System.in);
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

        System.out.println("Sua conta foi criada com sucesso,veja seus dados abaixo:");
        System.out.println(this.name + ":");
        this.conta=GeradorNumerosFormatados.gerarNumeroConta();
        this.agencia = GeradorNumerosFormatados.gerarNumeroAgencia();
        System.out.println("=========================");

        System.out.println("Realize seu primeiro depósito para ter acesso a sua conta");
        System.out.println("Digite o valor do depósito");
        
        primeiroDeposito = scanner.nextDouble();
        configurarConta(primeiroDeposito);

        scanner.nextLine();

        acessarConta();
    }

    public abstract void configurarConta(double primeiroDeposito);

    public void acessarConta(){
        boolean verificador = false;
        do {
            System.out.println("Login");
            System.out.println("============");

            System.out.println("Informe seu nome");
            String nameToVerify = scanner.nextLine().trim();

            System.out.println("Digite o número da sua conta");
            String contaToVerify = scanner.nextLine().trim();

            System.out.println("Digite o número da sua agência");
            String agenciaToVerify = scanner.nextLine().trim();

            if (nameToVerify.equalsIgnoreCase(this.name) && contaToVerify.equals(this.conta) && agenciaToVerify.equals(this.agencia)){
                verificador = true;

            }else{
                System.out.println("Informações inválidas tente novamete");
            }
        }while(!verificador);

        Main.menu();
    }

    // Getters

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

    // Setters
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public void setBoleto(double boleto) {
        this.boleto = boleto;
    }


    // Depósito
    public boolean depositando() {
        System.out.println("Informe o valor do depósito:");
        this.deposito = scanner.nextDouble();

        if (this.deposito <= 0) {
            System.out.println("Valor de depósito inválido!");
            return false;
        }

        return true;
    }

    // Saque
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
    // Pagamento de boleto
    public boolean pagandoBoleto(){

        System.out.println("Informe o valor do boleto:");
        this.boleto = scanner.nextDouble();

        if(this.boleto <=0) {
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
