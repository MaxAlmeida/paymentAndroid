package pdvend.payment;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.NumberFormat;
import android.support.test.espresso.Espresso;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import static android.support.test.espresso.Espresso.getIdlingResources;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withInputType;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.util.EnumSet.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class PaymentFragmentTest {
    private Double paymentValue;
    private String moneyFormat;
    private Double moneyPaymentValue;
    private Double cardPaymentValue;
    private Double invalidCardPaymentValue;
    NumberFormat numberFormat;
    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        numberFormat = NumberFormat.getCurrencyInstance();
        paymentValue = (double)2000;
        moneyPaymentValue = (double)1500;
        cardPaymentValue = (double)500;
        invalidCardPaymentValue = (double)3000;
        moneyFormat = numberFormat.format(paymentValue/10).toString();

    }

    /*
        Test if value passed from mainActivity matches with TextView Elements
     */
    @Test
    public void testMatchVTotalPaymentValueWithFragement(){
        onView(withId(R.id.getValue)).perform(typeText(paymentValue.toString()));
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.remaining_value)).check(matches(withText(moneyFormat.toString().replace("R$",""))));
        onView(withId(R.id.total_value)).check(matches(withText(moneyFormat.toString().replace("R$",""))));
        onView(withId(R.id.type_payment_value)).check(matches(withText(moneyFormat.toString())));
    }

    /*

        Test payment realized with partial payments with diferent types (money and card)
     */
    @Test
    public void testMatchValuesWithPartialPayment(){
        double remainValue = paymentValue;
        String moneyFormat;
        onView(withId(R.id.getValue)).perform(typeText(paymentValue.toString()));
        onView(withId(R.id.button)).perform(click());

        //Realizes money payment and check remain value
        onView(withId(R.id.type_payment_value)).perform(clearText(),typeText(moneyPaymentValue.toString()));
        onView(withId(R.id.money_payment_button)).perform(click());
        remainValue = (remainValue - moneyPaymentValue);
        moneyFormat = numberFormat.format(remainValue/10).toString();
        onView(withId(R.id.remaining_value)).check(matches(withText(moneyFormat.replace("R$",""))));

        //Realizes card payment and check remain value
        onView(withId(R.id.type_payment_value)).perform(clearText(),typeText(cardPaymentValue.toString()));
        onView(withId(R.id.card_payment_button)).perform(click());
        remainValue = (remainValue - cardPaymentValue);
        moneyFormat = numberFormat.format(remainValue/10).toString();
        onView(withId(R.id.remaining_value)).check(matches(withText(moneyFormat.replace("R$",""))));


        //Test if money payment realized exists in list
        onData(anything())
                .inAdapterView(withId(R.id.event_list))
                .atPosition(0)
                .check(matches(hasDescendant(Matchers.allOf(withId(R.id.payment_type), withText("Dinheiro")))));

        //Test if card payment realized exists in list
        onData(anything())
                .inAdapterView(withId(R.id.event_list))
                .atPosition(1)
                .check(matches(hasDescendant(Matchers.allOf(withId(R.id.payment_type), withText("Cartão")))));

        onView(withText("OK")).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());

    }

    /*
        Test card payment with value bigger than the total value
     */
    @Test
    public void testInvalidPaymentWithCard()
    {
        onView(withId(R.id.getValue)).perform(typeText(paymentValue.toString()));
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.type_payment_value)).perform(clearText(),typeText(invalidCardPaymentValue.toString()));
        onView(withId(R.id.card_payment_button)).perform(click());
        onView(withText(R.string.invalid_payment_card))
                .check(matches(isDisplayed()));
        onView(withText("Ok")).perform(click());

    }

    /*
        Test payment when full amount has already been paid
     */
    @Test
    public void testInvalidPayment()
    {
        Double moneyPayment = (double)2000;
        onView(withId(R.id.getValue)).perform(typeText(paymentValue.toString()));
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.type_payment_value)).perform(clearText(),typeText(moneyPayment.toString()));
        onView(withId(R.id.card_payment_button)).perform(click());
        onView(withId(R.id.type_payment_value)).perform(clearText(),typeText(moneyPayment.toString()));
        onView(withId(R.id.money_payment_button)).perform(click());
        onView(withText(R.string.invalid_payment_message))
                .check(matches(isDisplayed()));
        onView(withText("Ok")).perform(click());


    }

    @Test
    public void testShowChange()
    {
        Double biggerMoneyPayment = (double)3000;
        String changeValue =  numberFormat.format((biggerMoneyPayment-paymentValue)/10);
        onView(withId(R.id.getValue)).perform(typeText(paymentValue.toString()));
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.type_payment_value)).perform(clearText(),typeText(biggerMoneyPayment.toString()));
        onView(withId(R.id.money_payment_button)).perform(click());
        onView(withId(R.id.remaining_text)).check(matches(withText(R.string.change_text)));
        onView(withId(R.id.remaining_value)).check(matches(withText(changeValue.toString().replace("R$",""))));
    }

}
