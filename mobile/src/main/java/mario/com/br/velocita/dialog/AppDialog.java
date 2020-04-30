package mario.com.br.velocita.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class AppDialog {

    public static void showDialog(Context context, String title, String message, String positiveButton, String negativeButton, DialogInterface.OnClickListener positiveCallback, DialogInterface.OnClickListener negativeCallback) {

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButton, positiveCallback)
                .setNegativeButton(negativeButton, negativeCallback)
                .setCancelable(false)
                .show();
    }

}
