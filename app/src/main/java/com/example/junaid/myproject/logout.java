package com.example.junaid.myproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by junaid on 1/20/2018.
 */
public class logout extends AppCompatActivity implements View.OnClickListener,ListView.OnItemClickListener, SearchView.OnQueryTextListener, ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener {
    TextView logout;
    TextView inbox;
    TextView sent;
    TextView profileName;
    TextView heading;
    private DrawerLayout drawerLayout;
    private ImageButton btnLeftDrawer;
    private ImageButton btnRightDrawer;
    private SharedPreferences spUser;
    private SQLiteDatabase dbUser;
    ListView listView;
    SearchView search;
    ArrayAdapter<String> nameAdapter;
    ExpandableListView mainView;
    ArrayList<ArrayList<message>> messages;
    MyExpandableListAdapter mainadapter;
    ArrayList<user> uname;
    ArrayList uList;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logged);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.loginSuccesMessage)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        search = (SearchView) findViewById(R.id.searchView);
        uList = new ArrayList();
        uname = new ArrayList<user>();
        messages = new ArrayList<ArrayList<message>>();
        listView = (ListView) findViewById(R.id.lvRight);
        profileName = (TextView) findViewById(R.id.textName);
        inbox = (TextView) findViewById(R.id.textInbox);
        sent = (TextView) findViewById(R.id.textSent);
        spUser = getSharedPreferences("user_sp", MODE_PRIVATE);
        dbUser = openOrCreateDatabase("user__db", MODE_PRIVATE, null);
        logout = (TextView) findViewById(R.id.logout);
        heading = (TextView) findViewById(R.id.textView8);
        btnLeftDrawer = (ImageButton) findViewById(R.id.btnLeftDrawer);
        btnRightDrawer = (ImageButton) findViewById(R.id.btnRightDrawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        logout.setOnClickListener(this);
        btnLeftDrawer.setOnClickListener(this);
        btnRightDrawer.setOnClickListener(this);
        dbUser.execSQL("CREATE TABLE IF NOT EXISTS users" +
                "( userMail TEXT PRIMARY KEY ,name TEXT, pass TEXT,enableLog TEXT, gender TEXT,Date TEXT);");
        dbUser.execSQL("CREATE TABLE IF NOT EXISTS msg" +
                "( message TEXT ,sender TEXT, receiver TEXT,time TEXT,Date TEXT);");
        Cursor c = dbUser.rawQuery("SELECT name from users where userMail='" + spUser.getString(getResources().getString(R.string.userMail), "example@something.com") + "'", null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            profileName.setText(c.getString(c.getColumnIndex("name")));
        }
        c = dbUser.rawQuery("SELECT name from users", null);
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            uList.add(c.getString(c.getColumnIndex("name")));
            c.moveToNext();
        }
        nameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, uList);
        loadInbox();
         mainadapter = new MyExpandableListAdapter(this, uname, messages);
        mainView = (ExpandableListView) findViewById(R.id.expandableListView);
        mainView.setAdapter(mainadapter);
        mainView.setOnChildClickListener(this);
        mainView.setOnGroupClickListener(this);
        listView.setAdapter(nameAdapter);
        listView.setOnItemClickListener(this);
        search.setOnQueryTextListener(this);
        inbox.setOnClickListener(this);
        sent.setOnClickListener(this);
    }

    void loadInbox()
    {
        uname.clear();
        messages.clear();
        heading.setText("Inbox");
        Cursor e = dbUser.rawQuery("SELECT DISTINCT sender from msg where receiver='"+profileName.getText()+"'", null);
        e.moveToFirst();
        for(int i=0;i<e.getCount();i++) {
            user d1 = new user();
            d1.uname = e.getString(e.getColumnIndex("sender"));
            uname.add(d1);
            e.moveToNext();
        }

        for (int i = 1; i <= uname.size(); i++) {
            ArrayList<message> mes = new ArrayList<message>();
            Cursor g = dbUser.rawQuery("SELECT message from msg where receiver='"+profileName.getText()+"' AND sender='"+uname.get(i-1).uname+"'", null);
            g.moveToFirst();
            for (int j = 1; j <= g.getCount(); j++) {
                message s = new message();
                s.msg = g.getString(g.getColumnIndex("message"))+j;
                g.moveToNext();
                mes.add(s);
            }
            messages.add(mes);
        }

    }
    void loadOutbox()
    {
        uname.clear();
        messages.clear();
        heading.setText("Sent Items");
        Cursor e = dbUser.rawQuery("SELECT DISTINCT receiver from msg where sender='"+profileName.getText()+"'", null);
        e.moveToFirst();
        for(int i=0;i<e.getCount();i++) {
            user d1 = new user();
            d1.uname = e.getString(e.getColumnIndex("receiver"));
            uname.add(d1);
            e.moveToNext();
        }
        e.moveToFirst();


        for (int i = 0; i < e.getCount(); i++) {
            ArrayList<message> mes = new ArrayList<message>();
            Cursor g = dbUser.rawQuery("SELECT message from msg where sender='"+profileName.getText()+"' AND receiver='"+e.getString(e.getColumnIndex("receiver"))+"'", null);
            g.moveToFirst();
            for (int j = 0; j <g.getCount(); j++) {
                message s = new message();
                s.msg = g.getString(g.getColumnIndex("message"));
                mes.add(s);
                g.moveToNext();
            }
            g.close();
            e.moveToNext();
            messages.add(mes);
        }

    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLeftDrawer: {
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            }
            case R.id.btnRightDrawer: {
                if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                } else {
                    drawerLayout.openDrawer(Gravity.RIGHT);
                }
                break;
            }
            case R.id.logout: {
                Intent i = new Intent(this, loginClass.class);
                SharedPreferences.Editor editor = spUser.edit();
                editor.clear();

                editor.commit();
                startActivity(i);

                finish();
                break;
            }
            case R.id.textSent:{
                loadOutbox();
                mainadapter.notifyDataSetChanged();
                break;
            }
            case R.id.textInbox:{
                loadInbox();
                mainadapter.notifyDataSetChanged();
                break;
            }

        }


    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        nameAdapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

        return false;
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        View customView = getLayoutInflater().inflate(R.layout.senddialogue, null);
        final EditText message = (EditText)customView.findViewById(R.id.message);
        final TextView toName = (TextView)customView.findViewById(R.id.toName);
        toName.setText(uList.get(position).toString());

        AlertDialog.Builder custom = new AlertDialog.Builder(this);
        custom.setView(customView);
        custom.setPositiveButton("Send",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dbUser.execSQL("INSERT INTO msg VALUES('" + message.getText().toString() + "','" + profileName.getText().toString() + "','" + toName.getText().toString() + "','" + Calendar.getInstance().getTime().toString() + "','" + Calendar.getInstance().get(Calendar.DATE) + "')");
                Toast.makeText(getBaseContext(), R.string.mesgSent, Toast.LENGTH_SHORT).show();
                loadOutbox();
                loadOutbox();
                mainadapter.notifyDataSetChanged();

            }
        });
        custom.setCancelable(true);
        custom.create();
        custom.show();

    }


}
