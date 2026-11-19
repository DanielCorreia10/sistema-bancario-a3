package contabancaria;


public class ContaCorrente extends ContaBancaria {

    private double cheque_especial;
    private double valorChequeEspecialUsado;

    public ContaCorrente(String name, String conta, String agencia,
            double saldo, double deposito, double saque, double boleto,
            double cheque_especial, double valorChequeEspecialUsado) {

        super(name, conta, agencia, saldo, deposito, saque, boleto);

        this.cheque_especial = cheque_especial;
        this.valorChequeEspecialUsado = valorChequeEspecialUsado;
    }

    public double getCheque_especial() {
        return cheque_especial;
    }

    public void setCheque_especial(double cheque_especial) {
        this.cheque_especial = cheque_especial;
    }

    public double getValorChequeEspecialUsado() {
        return valorChequeEspecialUsado;
    }

    public void setValorChequeEspecialUsado(double valorChequeEspecialUsado) {
        this.valorChequeEspecialUsado = valorChequeEspecialUsado;
    }


    @Override 
    public void configurarConta(double primeiroDeposito) {

        setSaldo(primeiroDeposito);

        if (primeiroDeposito <= 500) {
            this.cheque_especial = 50;
        } else {
            this.cheque_especial = primeiroDeposito / 2.0;
        }
    }

    @Override
    public boolean depositando() {
        boolean depositoRealizado = super.depositando();

        if (!depositoRealizado) {
            return false;
        }
        
        double taxaChequeEspecial = this.valorChequeEspecialUsado * 0.20;
        double valorParaCobrir = this.valorChequeEspecialUsado + taxaChequeEspecial;
        double valorDepositado = getDeposito();

        if (this.valorChequeEspecialUsado > 0) {
            System.out.println("Você utilizou R$" + String.format("%.2f", this.valorChequeEspecialUsado) + " do seu cheque especial.");
            System.out.println("Será cobrada uma taxa de 20%: R$" + String.format("%.2f", taxaChequeEspecial));

            // Aplica a taxa e zera o valor usado do cheque especial, se o depósito cobrir
            if (valorDepositado >= valorParaCobrir) {
                setSaldo(getSaldo() + (valorDepositado - valorParaCobrir));
                this.valorChequeEspecialUsado = 0; // Zera o uso do cheque especial
                System.out.println("Cheque especial e taxa pagos com sucesso!");
            } else {
                // Se o depósito não cobrir tudo (cheque especial + taxa), ele só diminui a dívida
                this.valorChequeEspecialUsado = valorParaCobrir - valorDepositado; // Diminui a dívida
                setSaldo(0); // O saldo vai para zero, pois o depósito foi para cobrir a dívida
                System.out.println("Depósito utilizado para abater dívida do cheque especial. Você ainda deve R$" + String.format("%.2f", this.valorChequeEspecialUsado) + " do cheque especial.");
            }
        } else {
            setSaldo(getSaldo() + valorDepositado);
            System.out.println("Depósito realizado com sucesso! Saldo atual: R$" + String.format("%.2f", getSaldo()));
            
        }
        return true;
       
    }


    @Override
    public boolean sacando() {

        boolean saqueRealizado = super.sacando();

        if (saqueRealizado) {
            return true;
        }

        if(getSaque() <= 0){
            return false;
        }

        System.out.println("Saldo insuficiente, vamos tentar aplicar o limite do seu cheque especial");

        double restanteParaSacar = getSaque() - getSaldo();

        // Verifica se existe limite disponível
        if (restanteParaSacar <= (this.cheque_especial - this.valorChequeEspecialUsado)) {

            setSaldo(0);
            this.valorChequeEspecialUsado += restanteParaSacar;

            System.out.println("Saque realizado com sucesso!");
            System.out.println("Valor utilizado do cheque especial: R$"
                    + String.format("%.2f", this.valorChequeEspecialUsado));

            return true;

        } else {

            System.out.println("Saldo + limite insuficiente, não podemos concluir a transação!");

            return false;
        }
    }

    @Override
    public boolean pagandoBoleto() {

        boolean boletoPago = super.pagandoBoleto();

        if (boletoPago) {
            return true;
        }

         if (getBoleto() <= 0) {
            return false;
        }

        System.out.println("Saldo insuficiente, vamos tentar aplicar o limite do seu cheque especial");

        double valorParaPagarComCheque = getBoleto() - getSaldo();
        if (valorParaPagarComCheque <= (this.cheque_especial - this.valorChequeEspecialUsado) ){
            this.valorChequeEspecialUsado += valorParaPagarComCheque; // Registra o uso do cheque especial
            setSaldo(0); // Zera o saldo da conta

            System.out.println("Transação concluída com sucesso sua conta agora encontra-se usando o cheque especial ");
            System.out.println("Saldo atual: R$"+ String.format("%.2f", getSaldo()));
            return true;
        }else{
            System.out.println("Saldo + limite insuficiente, não podemos concluir a transação!");
            return false;
        }

    }
        
}
