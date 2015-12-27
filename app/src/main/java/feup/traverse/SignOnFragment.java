package feup.traverse;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Filipe
 * @date 16/12/2015.
 */

public class SignOnFragment  extends Fragment {

    DataBaseAdapter connector;
    Communicator comm;
    private Button btn_nextRegister;
    private EditText et_Username,et_Name, et_Password, et_Password2, et_dateOfBirth, et_Email;
    private Session session;//global variable

    private Pattern pattern;
    private Matcher matcher;

    private static final String DATE_PATTERN =
            "(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[012])-((19|20)\\d\\d)";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_sign_on_form,container,false);

        session = new Session(getActivity().getBaseContext()); //in oncreate
        pattern = Pattern.compile(DATE_PATTERN);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initicontrol();
    }

    private void initicontrol(){

        et_Username = (EditText) getActivity().findViewById(R.id.et_username);
        et_Name = (EditText) getActivity().findViewById(R.id.et_name);
        et_Password = (EditText) getActivity().findViewById(R.id.et_password);
        et_Password2 = (EditText) getActivity().findViewById(R.id.et_conf_pass);
        et_Email = (EditText) getActivity().findViewById(R.id.et_email);
        et_dateOfBirth = (EditText) getActivity().findViewById(R.id.et_dateofbirth);
        btn_nextRegister = (Button) getActivity().findViewById(R.id.btn_register);

        // create a instance of SQLite Database
        connector = new DataBaseAdapter(getActivity());
        connector.open();

        comm = (Communicator) getActivity();

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

        et_dateOfBirth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!DateValidate(et_dateOfBirth.getText().toString()))
                    et_dateOfBirth.setError("It's doesn't appear with expected date format.");
                else
                    et_dateOfBirth.setError(null);
            }
        });

        btn_nextRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkError();
            }
        });
    }

    private void checkError (){
        boolean flag_minimalError = false;

        String username = et_Username.getText().toString();
        String name = et_Name.getText().toString();
        String email = et_Email.getText().toString();
        String password = et_Password.getText().toString();
        String password2 = et_Password2.getText().toString();
        String date = et_dateOfBirth.getText().toString();

        if (username.trim().length() < 1 || name.trim().length() < 1 || password.trim().length() < 1 ||
                date.trim().length() < 1 || email.trim().length() < 1) {
            Toast.makeText(getActivity() , "Please, fill all fields in.", Toast.LENGTH_SHORT).show();
            flag_minimalError = false;
        } else if (connector.verifyUsernameAndEmail(username, email)) {
            et_Username.setError("Username or email already in use, insert another please.");
            flag_minimalError = true;
        } else if (!DateValidate(date)) {
            et_dateOfBirth.setError("Please, fill in the date in this format: dd-mm-yyyy.");
            flag_minimalError = true;
        } else if (!verifyEqualsPasswords(password,password2)) {
            et_Password.setError("Passwords doesn't match.");
            flag_minimalError = true;
        } else {

            session.putdata(username, name, email, date,password);
            //Toast.makeText(getActivity(), "Account created successfully!", Toast.LENGTH_SHORT).show();
            comm.respond(0, 0);//1 quer dizer que foi positivo
        }

        if (flag_minimalError)
            Toast.makeText(getActivity() , "Please, correct assigned errors.", Toast.LENGTH_SHORT).show();
    }

    public boolean DateValidate(final String date){

        matcher = pattern.matcher(date);

        if(matcher.matches()){

            matcher.reset();

            if(matcher.find()){

                String day = matcher.group(1);
                String month = matcher.group(2);
                int year = Integer.parseInt(matcher.group(3));

                if (day.equals("31") &&
                        (month.equals("4") || month .equals("6") || month.equals("9") ||
                                month.equals("11") || month.equals("04") || month .equals("06") ||
                                month.equals("09"))) {
                    return false; // only 1,3,5,7,8,10,12 has 31 days
                } else if (month.equals("2") || month.equals("02")) {
                    //leap year
                    if(year % 4==0){
                        if(day.equals("30") || day.equals("31")){
                            return false;
                        }else{
                            return true;
                        }
                    }else{
                        if(day.equals("29")||day.equals("30")||day.equals("31")){
                            return false;
                        }else{
                            return true;
                        }
                    }
                }else{
                    return true;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    private boolean verifyEqualsPasswords(String password, String password2){
        if (!password.matches(password2)){
            et_Password2.setError("Passwords doesn't match.");
            return false;
        } else
            return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Close The Database
        connector.close();
    }
}
