package pdvend.payment;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.text.style.TypefaceSpan;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Created by max on 02/11/16.
 */

public class MoneyTextWatcher implements TextWatcher {
    static EditText typePaymentValue;
    private boolean isUpdating = false;
    private NumberFormat nf = NumberFormat.getCurrencyInstance();



    public static MoneyTextWatcher newInstance(EditText editText)
    {
        MoneyTextWatcher moneyTextWatcher = new MoneyTextWatcher();
        typePaymentValue = editText;
        return moneyTextWatcher;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        // Evita que o método seja executado varias vezes.
        // Se tirar ele entre em loop
        if (isUpdating) {
            isUpdating = false;
            return;
        }
        isUpdating = true;
        String str = charSequence.toString();
            // Verifica se já existe a máscara no texto.
            boolean hasMask = ((str.indexOf("R$") > -1 || str.indexOf("$") > -1) &&
                    (str.indexOf(".") > -1 || str.indexOf(",") > -1));
            // Verificamos se existe máscara
            if (hasMask) {
                // Retiramos a máscara.
                str = str.replaceAll("[R$]", "").replaceAll("[,]", "")
                        .replaceAll("[.]", "");
            }

            try {
                // Transformamos o número que está escrito no EditText em
                // monetário.
                str = nf.format(Double.parseDouble(str) / 100);
                double number = nf.parse(str).doubleValue();
                System.out.println(str);
                System.out.println(number);
                typePaymentValue.setText(str);
                typePaymentValue.setSelection(typePaymentValue.getText().length());
            } catch (NumberFormatException e) {
                charSequence = "";
            } catch (ParseException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public static double convertToDoubleFormat(String currencyFormat){
        double doubleFormat = 0;
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        try {
            doubleFormat = numberFormat.parse(currencyFormat).doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return doubleFormat;
    }

    static String formatNegative(double value){
        NumberFormat numberFormat = new DecimalFormat("'R$'0,00");
        return numberFormat.format(value);
    }

   /*
    @Override
    public void afterTextChanged(Editable editable) {
        if (!editable.toString().matches("^\\$(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?$")) {
            String userInput = "" + editable.toString().replaceAll("[^\\d]", "");
            StringBuilder cashAmountBuilder = new StringBuilder(userInput);

            while (cashAmountBuilder.length() > 3 && cashAmountBuilder.charAt(0) == '0') {
                cashAmountBuilder.deleteCharAt(0);
            }
            while (cashAmountBuilder.length() < 3) {
                cashAmountBuilder.insert(0, '0');
            }
            cashAmountBuilder.insert(cashAmountBuilder.length() - 2, '.');

            typePaymentValue.removeTextChangedListener(this);
            typePaymentValue.setText(cashAmountBuilder.toString());

            typePaymentValue.setTextKeepState("R$" + cashAmountBuilder.toString());
            Selection.setSelection(typePaymentValue.getText(), cashAmountBuilder.toString().length() + 1);

            typePaymentValue.addTextChangedListener(this);
        }

    }*/
}
