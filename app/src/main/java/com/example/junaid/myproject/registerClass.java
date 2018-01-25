package com.example.junaid.myproject;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by junaid on 1/20/2018.
 */
public class registerClass extends AppCompatActivity implements View.OnClickListener {
    EditText emailReg;
    EditText passReg;
    EditText nameReg;
    Button reg;
    Switch enableLog;
    Spinner spinner;
    private SharedPreferences spUser;
    private SQLiteDatabase dbUser;
    CheckBox cbox;
    Button btnDatePicker;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        emailReg = (EditText) findViewById(R.id.regEmail);
        passReg = (EditText) findViewById(R.id.regPass);
        nameReg = (EditText) findViewById(R.id.regName);
        reg = (Button) findViewById(R.id.regButton);
        spUser = getSharedPreferences("user_sp", MODE_PRIVATE);
        dbUser = openOrCreateDatabase("user__db", MODE_PRIVATE, null);
        enableLog=(Switch)findViewById(R.id.Enableswitch);
        spinner=(Spinner)findViewById(R.id.genderSpinner);
        cbox=(CheckBox)findViewById(R.id.checkBox);
        btnDatePicker = (Button) findViewById(R.id.btnDatePicker);
        dbUser.execSQL("CREATE TABLE IF NOT EXISTS users" +
                "( userMail TEXT PRIMARY KEY , name TEXT,pass TEXT,enableLog TEXT, gender TEXT,Date TEXT);");
        reg.setOnClickListener(this);
        btnDatePicker.setOnClickListener(this);
        reg.setEnabled(false);
        cbox.setOnClickListener(this);
    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    void showDatePickerDialog() {
        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                btnDatePicker.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
            }


        }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePicker.setTitle(getString(R.string.DatePickerDialogue));
        datePicker.setCanceledOnTouchOutside(true);
        datePicker.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regButton: {
                if (passReg.getText().toString().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.emptyPassError)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else if (emailReg.getText().toString().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.emptyMailError)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();}
                else if (emailValidator(emailReg.getText().toString()) == false) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.invalidEmailError)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else if ((((passReg.getText().toString().length()) < 8) || passReg.getText().toString().length() > 12)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.passLengthError)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else if(btnDatePicker.getText().toString().equals("Pick Date"))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.DOBError)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else
                {

                    Cursor c = dbUser.rawQuery("SELECT userMail from users where userMail='"+emailReg.getText().toString()+"'", null);
                    if(c.getCount()>0)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(R.string.regUserError)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                    else
                    {
                        dbUser.execSQL("INSERT INTO users VALUES('" + emailReg.getText().toString()+ "','" + nameReg.getText().toString() + "','" + passReg.getText().toString() + "','" + enableLog.isChecked() + "','" + spinner.getSelectedItem().toString() + "','"+btnDatePicker.getText().toString()+"');");
                        SharedPreferences.Editor editor = spUser.edit();
                        editor.putString(getResources().getString(R.string.userMail), emailReg.getText().toString().trim());
                        editor.putString(getResources().getString(R.string.Pass), passReg.getText().toString().trim());
                        editor.commit();
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(R.string.RegSuccessMessage)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                    finish();

                }
                break;
            }
            case R.id.checkBox:
            {
                if(cbox.isChecked())
                {
                    reg.setEnabled(true);
                }
                else
                    reg.setEnabled(false);
                break;
            }
            case R.id.btnDatePicker : {
                showDatePickerDialog();
                break;
            }
        }

    }
}

