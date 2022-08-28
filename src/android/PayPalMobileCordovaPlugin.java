package in.credopay.payment.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Random;
public class PayPalMobileCordovaPlugin extends CordovaPlugin {
    private CallbackContext callbackContext;
    private Activity activity = null;
    private static final int REQUEST_SINGLE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;
    private boolean serverStarted = false;
    private String productionClientId;
    private String sandboxClientId;

    @Override
    public boolean execute(String action, JSONArray args,
                           CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        //this.activity = this.cordova.getActivity();
        // this.activity = this.activity.get;
        boolean retValue = true;

        return retValue;
    }

    @Override
    public void onDestroy() {
        if (null != this.activity && serverStarted) {
            //this.activity.stopService(new Intent(this.activity, PaymentActivity.class));
        }
        super.onDestroy();
    }

    // internal implementation
    private void version() {
        //this.callbackContext.success(Version.PRODUCT_VERSION);
    }

    private void init(JSONArray args) throws JSONException {
        JSONObject jObject = args.getJSONObject(0);
        this.productionClientId = jObject.getString("PayPalEnvironmentProduction");
        this.sandboxClientId = jObject.getString("PayPalEnvironmentSandbox");
        this.callbackContext.success();
    }


    private void startService() {

        startPayment(1, 100, false);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            switch (resultCode) {
                case CredopayPaymentConstants.TRANSACTION_COMPLETED:
                    CLog.loge("TRANSACTION_COMPLETED");
                    Bundle bundle = data.getExtras();
                    for (String key : bundle.keySet()) {
                        System.out.println((key + " Key | " + bundle.get(key).toString())); //To Implement
                    }
                    this.callbackContext.success();
                    break;
                case CredopayPaymentConstants.TRANSACTION_CANCELLED:
                    if (data != null) {
                        String error = data.getStringExtra("error");
                        if (error != null) {
                            Toast.makeText(this, error,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    this.callbackContext.error();
                    break;
                case CredopayPaymentConstants.LOGIN_FAILED:
                    if (data != null) {
                        String error = data.getStringExtra("error");
                        if (error != null) {
                            Toast.makeText(this, error,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    this.callbackContext.error();
                    break;
                case CredopayPaymentConstants.CHANGE_PASSWORD:
                    //no data will come from sdk, dont read data from intent
                    startPayment(1, 100, true);

                    break;
                case CredopayPaymentConstants.CHANGE_PASSWORD_SUCCESS:
                    //no data will come from sdk, dont read data from intent
                    startPayment(1, 100, true);
                    this.callbackContext.success();
                    break;
                case CredopayPaymentConstants.CHANGE_PASSWORD_FAILED:
                    if (data != null) {
                        String error = data.getStringExtra("error");
                        if (error != null) {
                            Toast.makeText(this, error,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    this.callbackContext.error();
                    break;
                default:
                    break;
            }
        }
    }

    public void startPayment(int transactionType, int amount, boolean changePassword) {
        Intent intent = new Intent(this.activity, PaymentActivity.class);
        intent.putExtra("TRANSACTION_TYPE", transactionType);
        intent.putExtra("DEBUG_MODE", true);
        intent.putExtra("PRODUCTION", false);
        intent.putExtra("AMOUNT", amount);
        intent.putExtra("FORCE_LOGIN", true);
        intent.putExtra("PRINT_RECEIPT", false);
        intent.putExtra("LOGIN_ID", "2000001637");//152


        Random r = new Random(System.currentTimeMillis());
        intent.putExtra("CRN_U", "AA" + String.valueOf(10000 + r.nextInt(20000)));

        if (changePassword)
            intent.putExtra("LOGIN_PASSWORD", "Aug@2022");
        else
            intent.putExtra("LOGIN_PASSWORD", "Aug@2022");
        intent.putExtra("LOGO", Utils.getVariableImage(ContextCompat.getDrawable(getApplicationContext(), R.drawable.logo)));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, 1);
    }
}
