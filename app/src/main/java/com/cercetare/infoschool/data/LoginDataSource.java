package com.cercetare.infoschool.data;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.cercetare.infoschool.data.model.LoggedInUser;
import com.cercetare.infoschool.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.concurrent.Executor;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public Result<LoggedInUser> login(String username, String password,LoginActivity loginActivity) {


        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            username);
            firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(loginActivity, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.println(Log.ASSERT, "LOGIN", "signInWithEmail:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();

                    } else {
                        // If sign in fails, display a message to the user.
//                        Log.println(Log.ASSERT, "LOGIN + signInWithEmail:failure", task.getException().toString());
                    }
                }
            });
            FirebaseUser user = firebaseAuth.getCurrentUser();
            LoggedInUser newUser = new LoggedInUser(java.util.UUID.randomUUID().toString(), user.getDisplayName());
            return new Result.Success<>(newUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
