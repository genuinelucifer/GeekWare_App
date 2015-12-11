package com.geekware.geekware;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.parse.ParseUser;

/**
 * Use this file to create functions which could be used more than in one file.
 * Always keep only static members here.
 */
public class Utilities {

    /**
     * This functions returns if a valid internet connection is available.
     * Always use this function to determine if to call api requiring internet or not.
     *
     * @param ctx : It should be the handle of the Activity from which you are calling this function
     *              Ex: Utilities.isNetworkAvailable(MainActivity.this);
     */
    public static boolean isNetworkAvailable(Context ctx)
    {
        ConnectivityManager ctvMngr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo aNetInfo = ctvMngr.getActiveNetworkInfo();
        return aNetInfo != null && aNetInfo.isAvailable();
    }

    /**
     * Always try to keep variables as private and provide public getters and setters
     * This variable keeps track of currently logged in user.
     */
    private static ParseUser curUser = null;
    public static boolean checkLoggedInUser()
    {
        curUser = ParseUser.getCurrentUser();
        return curUser != null;
    }
    public static void setCurrentUser(ParseUser usr)
    {
        curUser = usr;
    }
    public static String getUsername()
    {
        return curUser.getUsername();
    }

    public static Context contextLogout;    //Context used by the LogoutTask
    /**
     * Call this AsyncTask to logout a user.
     * Always set contextLogout to the handle of current activity before executing this task.
     * Ex: Utilities.contextLogout = HomeActivity.this;
     *     new Utilities.LogoutTask().get();
     */
    public static class LogoutTask extends AsyncTask<Void,Void, Void> {
        ProgressDialog pd;
        Intent i;

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(contextLogout);
            pd.setMessage("Logging out...\nPlease wait...");
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ParseUser.logOut();
            curUser = null;
            i = new Intent(contextLogout, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            return null;
        }

        @Override
        protected void onPostExecute(Void state) {
            pd.setMessage("");
            pd.dismiss();
            contextLogout.startActivity(i);
        }
    }
}
