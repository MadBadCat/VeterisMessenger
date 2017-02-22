package com.example.aazmund.veterismessenger;

import android.content.Intent;
import android.os.*;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<Messenger> adapter;
    RelativeLayout activity_main;
    FloatingActionButton fab;

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.menu_sign_out){
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(activity_main, "Вы были отключены.",Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(activity_main,"Успешный вход. Добро пожаловать!", Snackbar.LENGTH_SHORT).show();
                displayMessage();
            }
            else {
                Snackbar.make(activity_main,"Не удалось войти. Пожалуйста повторите позже.", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity_main = (RelativeLayout) findViewById(R.id.activity_main);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                EditText input = (EditText)findViewById(R.id.input);
                FirebaseDatabase.getInstance().getReference().push().setValue(new Messenger(input.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                input.setText("");

            }
        });

        //если не зареган то вывести страницу
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_REQUEST_CODE);
        } else {
            Snackbar.make(activity_main, "Добро пожаловать" + FirebaseAuth.getInstance().getCurrentUser().getEmail(), Snackbar.LENGTH_SHORT).show();
        }
        //загрузка контента
        displayMessage();
    }

    private void displayMessage() {

        ListView ListOfMessege = (ListView)findViewById(R.id.list_of_messege);
        adapter = new FirebaseListAdapter<Messenger>(this,Messenger.class,R.layout.list_item,FirebaseDatabase.getInstance().getReference() ) {
            @Override
            protected void populateView(View v, Messenger model, int position) {
                // получение референсов для показа лист_итем
                TextView messageText,messageUser,messegeTime;
                messageText = (TextView) v.findViewById(R.id.message_text);
                messageUser = (TextView) v.findViewById(R.id.message_user);
                messegeTime = (TextView) v.findViewById(R.id.message_time);

                messageText.setText(model.getMessengerText());
                messageUser.setText(model.getMessengerUser());
                messegeTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessegetime()));
            }
        };
        ListOfMessege.setAdapter(adapter);

    }

}
