package com.mttnow.fluttr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mttnow.fluttr.managers.ProfileManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String DESTINATION = "destination";
    private static final String DEPART = "depart";
    private static final String RETURN = "return";
    private static final String NUM_TRAVELLERS = "numTravellers";

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button search = (Button) findViewById(R.id.search_btn);
        search.setOnClickListener(this);

        Button quick = (Button) findViewById(R.id.quick_nav);
        quick.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_btn:
                loginUser();
                break;
            case R.id.quick_nav:
                Intent intent = new Intent();
                intent.putExtra(DESTINATION, "Paris");
                intent.putExtra(DEPART, "");
                intent.putExtra(RETURN, "");
                intent.putExtra(NUM_TRAVELLERS, 1);
                intent.setClass(this, HotelStreamActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void loginUser() {
        String username;
        String password;
        EditText userInput = (EditText) findViewById(R.id.username_input);
        username = userInput.getText().toString();
        EditText passwordInput = (EditText) findViewById(R.id.password_input);
        password = passwordInput.getText().toString();
        if(!username.isEmpty() && !password.isEmpty()) {
            userSignIn(username, password);
        }
        else {
            if (username.isEmpty()) {
                userInput.setError("Please enter a valid username");
            }
            if (password.isEmpty()) {
                passwordInput.setError("Please enter a valid password");
            }
        }
    }

    private void userSignIn(String email, String password) {
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Authentication failed. Please check your email and password.",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("FLUTTR", "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (firebaseAuth.getCurrentUser() != null) {
                            startActivity(new Intent(MainActivity.this, SearchActivity.class));
                        }

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("FLUTTR", "signInWithEmail:failed", task.getException());
//                            Toast.makeText(MainActivity.this, "Authentication failed. Please check your email and password.",
//                                    Toast.LENGTH_SHORT).show();
                        }

                        findViewById(R.id.progressBar).setVisibility(View.GONE);
                    }
                });
    }
}
