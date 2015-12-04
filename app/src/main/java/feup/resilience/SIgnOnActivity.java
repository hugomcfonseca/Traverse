package feup.resilience;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignOnActivity extends AppCompatActivity {

    private Button btn_nextRegister;
    private EditText et_Username, et_Password, et_Password2, et_dateOfBirth, et_Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_on);

        et_Username = (EditText)findViewById(R.id.et_username);
        et_Password = (EditText)findViewById(R.id.et_password);
        et_Password2 = (EditText)findViewById(R.id.et_conf_pass);
        et_Email = (EditText)findViewById(R.id.et_email);
        //et_dateOfBirth = (EditText)findViewById(R.id.et);
        btn_nextRegister = (Button)findViewById(R.id.btn_register);

        et_Email.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                String email = et_Email.getText().toString();

                if (!isEmailValid(email))
                       et_Email.setError("Invalid email address.");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            public boolean isEmailValid(String email) {
                boolean isValid = false;

                String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
                CharSequence inputStr = email;

                Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(inputStr);
                if (matcher.matches()) {
                    isValid = true;
                }
                return isValid;
            }

        });


        et_Password2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String strPass1 = et_Password.getText().toString();
                String strPass2 = et_Password2.getText().toString();
                if (strPass1.equals(strPass2));
                else {
                    et_Password2.setError("Differents Passwords.");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btn_nextRegister.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

            }
        });

        //Data();//Função para mostrar o calendário
    }

    /*public  void Data (){
        EditText txtDate = (EditText)findViewById(R.id.et_birth);
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialog dialog=new DateDialog(view);
                FragmentTransaction ft =getFragmentManager().beginTransaction();
                dialog.show(ft, "DatePicker");
            }
        });
    }*/

}
