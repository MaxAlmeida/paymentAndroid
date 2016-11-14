# PaymentFragment

Esse biblioteca implementa uma tela de pagamentos que tem como finalidade proporcionar que um pagamento seja efetuado de duas maneiras : cartão e dinheiro.

## Uso

Para uso é necessário que a biblioteca seja importada. Dessa forma, o código a seguir é um exemplo de como deve ser chamado essa tela de pagamentos.
```java
 public void btnShowDialog(View view){
        Double totalValue = MoneyTextWatcher.convertToDoubleFormat(value.getText().toString());
        FragmentManager fragmentManager = getSupportFragmentManager();
        PaymentFragments payfragment =  PaymentFragments.newInstance(totalValue);
        payfragment.show(fragmentManager,"message!");
        payfragment.setCancelable(true);
    }
```
Essa biblioteca também possibilita a conversão de um formato moeda para o formato double assim como é feito na primeira linha do método btnShowDialog(View view). Caso o valor do pagamento ja venha em formato double basta passa-lo para PayFragments.newInstance() diretamente. `

