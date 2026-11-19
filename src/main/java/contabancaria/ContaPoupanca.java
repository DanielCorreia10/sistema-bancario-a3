package contabancaria;

public class ContaPoupanca extends ContaBancaria {

    private double taxaRendimento = 0.5;

    public ContaPoupanca(String name, String conta, String agencia,
            double saldo, double deposito, double saque, double boleto, double taxaRendimento) {
        super(name, conta, agencia, saldo, deposito, saque, boleto);
        this.taxaRendimento = taxaRendimento;
    }

    public double getTaxaRendimento(){
        return taxaRendimento;
    }

    public void setTaxaRendimento (double taxaRendimento){
        this.taxaRendimento = taxaRendimento;
    }

    public void calcularRendimento(){
        double rendimento = getSaldo() * (taxaRendimento/100);
        setSaldo(getSaldo() + rendimento);
        System.out.printf("Rendimento aplicado: R$ %.2f%n", rendimento);
        System.out.printf("Saldo atualizado: R$ %.2f%n", getSaldo());
    }

    @Override
    public void configurarConta(double primeiroDeposito) {
        setSaldo(primeiroDeposito);
    }

    @Override 
    public boolean depositando() {
        boolean depositoRealizado = super.depositando();

        if (!depositoRealizado) {
            return false;
        }

        setSaldo(getSaldo() + getDeposito());
        return true;
    }
    
}
