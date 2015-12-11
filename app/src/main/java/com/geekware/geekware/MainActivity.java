package com.geekware.geekware;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

/**
 * Always follow the conventions as used in this file.
 *   -- Always make all the GUI variables in the class outside any function then initialise them in
 *      the onCreate function. Follow the naming convention too.
 *   -- Try to keep everything aligned.
 *   -- Always first assign all variables then only start other code as action listeners.
 *   -- Always name activity classes as *Activity
 */

public class MainActivity extends ActionBarActivity {
    Button btnSignUp, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignUp = (Button) findViewById(R.id.btnProceedSignUp);
        btnLogin  = (Button) findViewById(R.id.btnProceedLogin);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        /* If the user didn't logout the previous time then log him in again automatically. */
        if(Utilities.checkLoggedInUser())
        {
            Intent i = new Intent(MainActivity.this, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }

}
