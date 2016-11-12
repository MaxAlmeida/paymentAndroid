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
        //Prevents the method from executing multiple times.
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
}
