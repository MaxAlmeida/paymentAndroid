package pdvend.payment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by max on 03/11/16.
 */

public class PaymanetArrayAdapter extends ArrayAdapter<Payment> {
    private final List<Payment> payments;
    private final Context context;

    public PaymanetArrayAdapter(Context context, int resource, List<Payment> payments) {
        super(context, resource, payments);
        this.context = context;
        this.payments = payments;
    }

    static class ViewHolder{
        protected TextView paymentType;
        protected TextView paymentValue;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Payment payment = getItem(position);
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        final ViewHolder viewHolder;
        View view = null;
        if (convertView == null){
            LayoutInflater inflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.adapter_layout,null);
            viewHolder = new ViewHolder();
            viewHolder.paymentType = (TextView)convertView.findViewById(R.id.payment_type);
            viewHolder.paymentValue = (TextView)convertView.findViewById(R.id.payment_value);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        double paymentValue = payment.getValue();
        String paymentValueParse = numberFormat.format(paymentValue);
        viewHolder.paymentValue.setText(paymentValueParse);
        viewHolder.paymentType.setText(payment.getType());
        return convertView;
    }
}
