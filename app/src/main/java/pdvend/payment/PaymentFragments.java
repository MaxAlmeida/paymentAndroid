package pdvend.payment;

import android.app.ActionBar;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.Layout;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;


public class PaymentFragments extends DialogFragment {

    public PaymentFragments(){

    }

    public static PaymentFragments newInstance (String totalValue){
        PaymentFragments paymentFragments = new PaymentFragments();
        Bundle args = new Bundle();
        args.putString("totalValue",totalValue);
        paymentFragments.setArguments(args);

        return paymentFragments;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        String totalValueObtaneid = getArguments().getString("totalValue");
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.payment_fragments);

        TextView remainingValue = (TextView) dialog.findViewById(R.id.remaining_value);
        remainingValue.setText(totalValueObtaneid);

        TextView totalValue = (TextView) dialog.findViewById(R.id.total_value);
        totalValue.setText(totalValueObtaneid);
       // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText typePaymentValue = (EditText) dialog.findViewById(R.id.type_payment_value);
        typePaymentValue.setText(totalValueObtaneid);
        typePaymentValue.addTextChangedListener( MoneyTextWatcher.newInstance(typePaymentValue));

        return dialog;
    }


}

