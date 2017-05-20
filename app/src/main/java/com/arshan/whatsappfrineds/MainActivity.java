package com.arshan.whatsappfrineds;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends ActionBarActivity {
    EditText number, count;
    Button save, delete;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private CustomProgressDialog customProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean hasPermission = (ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED);
        boolean hasPermission2 = (ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.READ_CONTACTS"}, REQUEST_CODE_ASK_PERMISSIONS);
        } else if (!hasPermission2) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.WRITE_CONTACTS"}, REQUEST_CODE_ASK_PERMISSIONS);
        }

        number = (EditText) findViewById(R.id.number);
        save = (Button) findViewById(R.id.save);
        count = (EditText) findViewById(R.id.count);
        delete = (Button) findViewById(R.id.delete);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numberStr = number.getText().toString();
                if ((null == count.getText().toString() || count.getText().toString().matches("")) ||
                        ((null == numberStr || numberStr.matches("") || numberStr.length() < 10))) {
                    Toast.makeText(MainActivity.this, "Plz enter required/proper details", Toast.LENGTH_SHORT).show();
                } else {
                    Integer subNumbInt = Integer.valueOf(numberStr.substring(0,5));
                    Integer cnt = Integer.parseInt(count.getText().toString());
                    Log.d("data","count:"+cnt+"  sub:"+subNumbInt);
                    customProgressDialog = CustomProgressDialog.show(MainActivity.this);
                    getRandomNumbers(cnt, subNumbInt);
                    customProgressDialog.cancel();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialog = CustomProgressDialog.show(MainActivity.this);
                deleteContacts("~Stranger");
                customProgressDialog.cancel();
            }
        });
    }
    private void getRandomNumbers(Integer coun, Integer subNumbInt) {
        for (int i=0; i < coun; i++) {
            Random r = new Random();
            int Low = 11111;
            int High = 99999;
            int result = r.nextInt(High-Low) + Low;
            int nameAppend = r.nextInt(999-1) + 1;
            String finalNum = String.valueOf(subNumbInt)+String.valueOf(result);
            Log.d("finalNum",""+finalNum);
            String finalName = "~Stranger "+nameAppend+"~";
            saveContacts(finalName, finalNum);
        }
        showDailog();
    }

    private void saveContacts(String contactName, String contactNumber) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = ops.size();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID,rawContactInsertIndex)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contactName) // Name of the person
                .build());
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(
                        ContactsContract.Data.RAW_CONTACT_ID,   rawContactInsertIndex)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contactNumber) // Number of the person
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build()); // Type of mobile number

        Log.d("ok","ok");
        try {
            ContentProviderResult[] res = getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        }
        catch (RemoteException e) {
            Log.d("RemoteException",""+e.getLocalizedMessage());
        }
        catch (OperationApplicationException e) {
            Log.d("OperationApplication",""+e.getLocalizedMessage());
        }
    }

    private void deleteContacts(String name) {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        try {
            if (cur.moveToFirst()) {
                do {
                    Log.d("Names:",cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                    Log.d("Name:",name);
                    if (cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)).contains(name)) {
                        String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                        cr.delete(uri, null, null);
                    }

                } while (cur.moveToNext());
            }

        } catch (Exception e) {
            Log.d("Exception",""+e.getLocalizedMessage());
        }
    }

    private void showDailog() {
        android.app.FragmentManager manager = getFragmentManager();
        Info dailogFragment = new Info();
        dailogFragment.show(manager,"dailogFrag");
        number.setText("");
        count.setText("");
    }
}
