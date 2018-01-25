package com.example.junaid.myproject;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class loginClass extends AppCompatActivity implements View.OnClickListener {
    EditText pass;
    EditText email;
    Button log;
    Button reg;
    TextView forgotP;
    private SharedPreferences spUser;
    private SQLiteDatabase dbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        email=(EditText)findViewById(R.id.emailText);
        pass=(EditText)findViewById(R.id.passText);
        log=(Button)findViewById(R.id.loginButton);
        reg=(Button)findViewById(R.id.registerButton);
        forgotP=(TextView)findViewById(R.id.forgetP);
        spUser = getSharedPreferences("user_sp", MODE_PRIVATE);
        dbUser = openOrCreateDatabase("user__db", MODE_PRIVATE, null);
        log.setOnClickListener(this);
        forgotP.setOnClickListener(this);
        reg.setOnClickListener(this);
        dbUser.execSQL("CREATE TABLE IF NOT EXISTS users" +
                "( userMail TEXT PRIMARY KEY ,name TEXT, pass TEXT,enableLog TEXT, gender TEXT,Date TEXT);");
        Cursor c = dbUser.rawQuery("SELECT userMail from users where userMail='"+spUser.getString(getResources().getString(R.string.userMail), "example@something.com")+"' and pass='"+spUser.getString(getResources().getString(R.string.Pass), "")+"'and enableLog='true'", null);
        if(c.getCount()>0)
        {
            finish();
            Intent i = new Intent(this,logout.class);
            startActivity(i);

        }


    }
    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:{
                if(pass.getText().toString().equals(""))
                {
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

                }
                else if(email.getText().toString().equals(""))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.emptyMailError)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else if(emailValidator(email.getText().toString())==false)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.invalidEmailError)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();}
                else if ((((pass.getText().toString().length()) < 8) || pass.getText().toString().length() > 12) ) {
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
                else
                {
                    Cursor c = dbUser.rawQuery("SELECT userMail from users where userMail='"+email.getText().toString()+"' and pass='"+pass.getText().toString()+"'", null);
                    if(c.getCount()== 0)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(R.string.wrongCresError)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //do things
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else{
                        SharedPreferences.Editor editor = spUser.edit();
                        editor.putString(getResources().getString(R.string.userMail), email.getText().toString().trim());
                        editor.putString(getResources().getString(R.string.Pass), pass.getText().toString().trim());
                        editor.commit();
                        finish();
                        Intent i = new Intent(this,logout.class);
                        startActivity(i);

                    }
                        }

                break;            }
            case R.id.forgetP:{
                Intent i = new Intent(this,forgotPass.class);
                startActivity(i);
                break;
            }
            case R.id.registerButton:{
                Intent i = new Intent(this,registerClass.class);
                startActivity(i);
                break;
            }
        }
    }
}
