package feup.resilience;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * Created by Filipe on 16/12/2015.
 */
public class SignOnFragment  extends Fragment {

    DataBaseAdapter connector;
    Communicator comm;
    private Button btn_nextRegister;
    private EditText et_Username, et_Password, et_Password2, et_dateOfBirth, et_Email;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_singon,container,false);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initicontrol();
    }


    private void initicontrol(){
        et_Username = (EditText) getActivity().findViewById(R.id.et_username);
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
                    et_dateOfBirth.setError("You haven't the minimun age to access to this app.");
                else
                    et_dateOfBirth.setError(null);
            }
        });


        et_dateOfBirth.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    DateDialog dialog = new DateDialog(v);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                    return true;
                }

                return false;
            }
        });

        btn_nextRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = et_Username.getText().toString();
                String email = et_Email.getText().toString();
                String password = et_Password.getText().toString();
                String password2 = et_Password2.getText().toString();
                String date = et_dateOfBirth.getText().toString();

                if (username.matches("") || password.matches("") || password2.matches("") ||
                        date.matches("") || email.matches("")) {
                    Toast.makeText(getActivity() , "Please, fill all fields.", Toast.LENGTH_SHORT).show();
                }

                if (connector.verifyUsernameAndEmail(username, email))
                    et_Username.setError("Username or email already in use, insert another please.");

                // TODO: insert condition to validate date
                if (!DateValidate(date))
                    Toast.makeText(getActivity(), "You haven't the minimun age to access to this app.", Toast.LENGTH_SHORT).show();
                //et_dateOfBirth.setError("You haven't the minimun age to access to this app.");

                if (verifyEqualsPasswords(password, password2) &&
                        DateValidate(date) &&
                        !connector.verifyUsernameAndEmail(username, email)) {
                    connector.createUser(username, email, date, password);
                    Toast.makeText(getActivity(), "Account created successfully!", Toast.LENGTH_SHORT).show();
                    comm.respond(1);//1 quer dizer que foi positivo

                }

            }
        });
    }


    private boolean DateValidate(String date) {
        String toParse = "01-01-2003";
        String format = "dd-MM-yyy";
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try{
            Date dateref = formater.parse(toParse);
            Date datesend = formater.parse(date);
            if (datesend.compareTo(dateref)>0){
                return false;
            }
            else
                return true;
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
        return true;

    }



    //Date Fragment
    @SuppressLint("ValidFragment")
    public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        public DateDialog(View view){
            et_dateOfBirth =(EditText)view;
        }

        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current date as the default date in the dialog
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            //show to the selected date in the text box
            String date=day+"-"+(month+1)+"-"+year;
            et_dateOfBirth .setText(date);
        }

    }

    private boolean verifyEqualsPasswords(String password, String password2){
        if (!password.matches(password2)){
            et_Password2.setError("Passwords doesn't match.");
            return false;
        } else
            return true;
    }


    public void closeThisActivity(){
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Close The Database
        connector.close();
    }
}
