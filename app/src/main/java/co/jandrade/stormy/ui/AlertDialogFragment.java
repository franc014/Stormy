package co.jandrade.stormy.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import co.jandrade.stormy.R;

/**
 * Created by Juan Francisco Andrade on 4/22/15.
 */
public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //parent activity
        Context context = getActivity();
        //factory method pattern: alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                                          .setTitle(context.getString(R.string.weather_api_error_title))
                                          .setMessage(context.getString(R.string.weather_api_error_message))
                                          .setPositiveButton(context.getString(R.string.weather_api_error_positive_button), null);
        // alert creation
        AlertDialog dialog = builder.create();
        //has to return dialog
        return dialog;

    }
}
