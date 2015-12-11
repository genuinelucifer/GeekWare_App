package com.geekware.geekware;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * This class has to be edited a lot after gui changes. It is here currently just for reference.
 */
public class SignUpActivity extends ActionBarActivity {
    EditText etId,etPass,etRePass;                      //Make 3 editTexts for this
    Button btnSignUp;
    CheckBox cbTerms;                                   //The checkBox to agree to conditions

    String username, password, repass, email;
    boolean isCbChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etId       = (EditText) findViewById(R.id.etUserIdSignUp);
        etPass     = (EditText) findViewById(R.id.etPassSignUp);
        etRePass   = (EditText) findViewById(R.id.etRePassSignUp);
        btnSignUp  = (Button)   findViewById(R.id.btnSignUp);
        cbTerms    = (CheckBox) findViewById(R.id.cbTerms);

        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                username = etId.getText().toString();
                password = etPass.getText().toString();
                repass = etRePass.getText().toString();
                email = username + "@iiti.ac.in";
                isCbChecked = cbTerms.isChecked();

                new SignUpTask().execute();
            }
        });
    }

    /**
     * All the possible states the SignUp Task can return with.
     */
    private enum SignUpTaskState
    {
        SUCCESS,
        NO_INTERNET,
        EMPTY_SIGNUP,
        PASS_NOMATCH,
        TERMS_UNAGREED,
        EXCEPTION_THROWN
    }

    class SignUpTask extends AsyncTask<Void,Boolean, SignUpTaskState> {
        ProgressDialog pd;
        String errMsg;

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(SignUpActivity.this);
            pd.setMessage("Please wait while signing up...");
            pd.show();
        }

        @Override
        protected SignUpTaskState doInBackground(Void... params) {
            publishProgress(false);
            pd.setMessage("Signing up...");

            if (!Utilities.isNetworkAvailable(SignUpActivity.this)) {
                return SignUpTaskState.NO_INTERNET;
            }

            username = username.trim();
            password = password.trim();
            repass = repass.trim();
            email = email.trim();

            if (username.isEmpty() || password.isEmpty() || email.isEmpty())
            {
                return SignUpTaskState.EMPTY_SIGNUP;
            }
            else if (!password.equals(repass))
            {
                return SignUpTaskState.PASS_NOMATCH;
            }
            else if (!isCbChecked)
            {
                return SignUpTaskState.TERMS_UNAGREED;
            }
            else {
                try {
                    ParseUser newUser = new ParseUser();
                    newUser.setUsername(username);
                    newUser.setPassword(password);
                    newUser.setEmail(email);
                    newUser.signUp();
                } catch (ParseException e) {
                    e.printStackTrace();
                    errMsg = e.getMessage();
                    return SignUpTaskState.EXCEPTION_THROWN;
                }
            }
            return SignUpTaskState.SUCCESS;
        }

        @Override
        protected void onProgressUpdate(Boolean... isEnabled) {
            btnSignUp.setEnabled(isEnabled[0]);
            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
        }


        @Override
        protected void onPostExecute(SignUpTaskState state) {
            pd.setMessage("");
            pd.dismiss();

            // refresh UI
            if (state == SignUpTaskState.SUCCESS) {
                // Success!
                Toast.makeText(SignUpActivity.this, "Sign Up Successful!\nYou can now login with your new account.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            else if (state == SignUpTaskState.NO_INTERNET) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setMessage(R.string.no_internet_msg)
                        .setTitle(R.string.no_internet_title)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else if (state == SignUpTaskState.EMPTY_SIGNUP) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setMessage(R.string.signup_error_message)
                        .setTitle(R.string.signup_error_title)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else if(state == SignUpTaskState.PASS_NOMATCH)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setMessage(R.string.pass_no_match_error_msg)
                        .setTitle(R.string.signup_error_title)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else if (state == SignUpTaskState.TERMS_UNAGREED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setMessage("Please read and agree to our terms and conditions before signing up.")
                        .setTitle("Agree to terms...")
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else if (state == SignUpTaskState.EXCEPTION_THROWN) {
                // Fail
                String errorMsg = errMsg.replace("parameters", "Email ID or password");
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setMessage(errorMsg)
                        .setTitle(R.string.signup_error_title)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            btnSignUp.setEnabled(true);
        }
    }
}
