package pdvend.payment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.app.AlertDialog;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;


public class PaymentFragments extends DialogFragment {
    private double totalValueRemain;
    private EditText paymentValue;
    private ListView listView;
    private ArrayList<Payment> payments;
    private PaymanetArrayAdapter paymentAdapter;
    private Button moneyPaymentButton;
    private Button cardPaymentButton;
    private TextView remainingValue;
    private TextView totalValue;
    private NumberFormat numberFormat;
    private TextView remainingText;
    private TextView moneySymbol;
    private Button confirmButton;
    private Button cancelButton;

    public PaymentFragments(){

    }

    public static PaymentFragments newInstance (double totalValue){
        PaymentFragments paymentFragments = new PaymentFragments();
        Bundle args = new Bundle();
        args.putDouble("totalValue", totalValue);
        paymentFragments.setArguments(args);

        return paymentFragments;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        totalValueRemain = getArguments().getDouble("totalValue");
        System.out.println(totalValueRemain);
        numberFormat = NumberFormat.getCurrencyInstance();

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.payment_fragments);

        confirmButton = (Button) dialog.findViewById(R.id.confirm_payment_button);
        cancelButton = (Button) dialog.findViewById(R.id.cancel_button);

        remainingText = (TextView) dialog.findViewById(R.id.remaining_text);
        moneySymbol = (TextView) dialog.findViewById(R.id.money_symbol_remaining);

        remainingValue = (TextView) dialog.findViewById(R.id.remaining_value);
        remainingValue.setText(numberFormat.format(totalValueRemain).toString().replace("R$",""));

        totalValue = (TextView) dialog.findViewById(R.id.total_value);
        totalValue.setText(numberFormat.format(totalValueRemain).toString().replace("R$",""));

        moneyPaymentButton = (Button) dialog.findViewById(R.id.money_payment_button);
        cardPaymentButton = (Button) dialog.findViewById(R.id.card_payment_button);
       // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        paymentValue = (EditText) dialog.findViewById(R.id.type_payment_value);
        paymentValue.setText(numberFormat.format(totalValueRemain));
        paymentValue.addTextChangedListener( MoneyTextWatcher.newInstance(paymentValue));
        updateConfirmButton();

        final ListView listView = (ListView) dialog.findViewById(R.id.event_list);
        payments = new ArrayList<>();
        paymentAdapter = new PaymanetArrayAdapter(getContext(),R.layout.adapter_layout,payments);
        listView.setAdapter(paymentAdapter);

        moneyPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (totalValueRemain > 0) {
                    double paymentMoneyValue = 0;
                    try {
                        paymentMoneyValue = numberFormat.parse(paymentValue.getText().toString()).doubleValue();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String paymentType = getResources().getString(R.string.money);
                    updatePaymentRealized(paymentType,paymentMoneyValue);
                }
                else{
                    showInvalidMessage(R.string.invalid_payment_message);
                }
            }
        });
        cardPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(totalValueRemain > 0) {
                    double paymentCardValue = 0;
                    try {
                        paymentCardValue = numberFormat.parse(paymentValue.getText().toString()).doubleValue();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(paymentCardValue > totalValueRemain)
                        showInvalidMessage(R.string.invalid_payment_card);
                    else {
                        String paymentType = getResources().getString(R.string.card);
                        updatePaymentRealized(paymentType, paymentCardValue);
                    }

                }
                else{
                    showInvalidMessage(R.string.invalid_payment_message);
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        return dialog;
    }


    public void updatePaymentRealized(String paymentType, double paymentValue){
        Payment payment = new Payment(paymentType,paymentValue);
        int compare = Double.compare(paymentValue,totalValueRemain);
        payments.add(payment);
        paymentAdapter.notifyDataSetChanged();
        totalValueRemain = totalValueRemain - paymentValue;
        System.out.println("Valor Restante:");
        System.out.println(totalValueRemain);
        updateConfirmButton();

        //If PaymentValue is greater than totalValueRemain
        if(compare > 0) {
            remainingValue.setText(numberFormat.format(Math.abs(totalValueRemain)).toString().replace("R$", ""));
            remainingValue.setTextColor(getResources().getColor(R.color.color_Change));
            moneySymbol.setTextColor(getResources().getColor(R.color.color_Change));
            remainingText.setText(getResources().getString(R.string.change_text));
        }
        else
            remainingValue.setText(numberFormat.format(totalValueRemain).toString().replace("R$", ""));


    }
    public void showInvalidMessage(int stringId){
        AlertDialog.Builder alertDialog;
        alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle(R.string.invalid_payment);
        alertDialog.setMessage(stringId);
        alertDialog.setPositiveButton("Ok",null);
        alertDialog.show();
    }

    public void updateConfirmButton(){
        if(totalValueRemain > 0)
            confirmButton.setVisibility(View.GONE);
        else
            confirmButton.setVisibility(View.VISIBLE);
    }


}

