package com.arshan.whatsappfrineds;

/**
 * Created by Arshan on 04-Nov-2016.
 */

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;


public class CustomProgressDialog extends Dialog {

    /**
     * Show loading dialog
     *
     * @param context
     *            Context
     */


    public static  CustomProgressDialog show(Context context) {
        CustomProgressDialog dialog = new  CustomProgressDialog(context);
        dialog.setTitle("Finding WhatsApp friends");
        dialog.setCancelable(false);
        ProgressBar progressBar = new ProgressBar(context);
        //progressBar.setProgressDrawable(R.drawable.progress_style);
        progressBar.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(android.R.color.holo_red_light), android.graphics.PorterDuff.Mode.MULTIPLY);

        dialog.addContentView(progressBar, new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        dialog.show();

        return dialog;
    }

    public static  CustomProgressDialog init(Context context) {
        CustomProgressDialog dialog = new  CustomProgressDialog(context);
        dialog.setTitle("");
        dialog.setCancelable(false);
        ProgressBar progressBar = new ProgressBar(context);
        //progressBar.setProgressDrawable(R.drawable.progress_style);
        progressBar.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(android.R.color.holo_red_light), android.graphics.PorterDuff.Mode.MULTIPLY);

        dialog.addContentView(progressBar, new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));


        return dialog;
    }




    public CustomProgressDialog(Context context) {
        super(context, R.style.newDialog);
    }
}