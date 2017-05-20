package com.arshan.whatsappfrineds;

/**
 * Created by Arshan on 11-Oct-2016.
 */

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

public class Info extends DialogFragment implements View.OnClickListener{
    Button ok;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_dailog,null);
        ok = (Button) view.findViewById(R.id.btnOk);

        ok.setOnClickListener(this);
        setCancelable(false);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnOk) {
            //Toast.makeText(getActivity(),"Cancelled",Toast.LENGTH_SHORT).show();
            dismiss();

        }
    }
}