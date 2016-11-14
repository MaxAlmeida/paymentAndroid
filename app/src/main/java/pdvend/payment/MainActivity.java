package pdvend.payment;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        value = (EditText) findViewById(R.id.getValue);
        value.addTextChangedListener(MoneyTextWatcher.newInstance(value));
    }

    /*


     */
    public void btnShowDialog(View view){
        Double totalValue = MoneyTextWatcher.convertToDoubleFormat(value.getText().toString());
        FragmentManager fragmentManager = getSupportFragmentManager();
        PaymentFragments payfragment =  PaymentFragments.newInstance(totalValue);
        payfragment.show(fragmentManager,"message!");
        payfragment.setCancelable(true);
    }
}
