package id.ac.polinema.absenguru;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import id.ac.polinema.absenguru.api.ApiClient;
import id.ac.polinema.absenguru.api.ApiInterface;
import id.ac.polinema.absenguru.helper.Session;
import id.ac.polinema.absenguru.model.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import android.os.Bundle;

public class LoginAdmin extends AppCompatActivity {

    private EditText inputUsername, inputPassword;
    private Button loginButton;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);
        session = new Session(getApplicationContext());
        inputUsername = findViewById(R.id.usernameAdmin);
        inputPassword = findViewById(R.id.passwordAdmin);
        loginButton = findViewById(R.id.btnLoginAdmin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin(inputUsername.getText().toString(), inputPassword.getText().toString());
            }
        });

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin")) {
//                    Intent intent = new Intent(LoginAdmin.this, PembukaAdmin.class);
//                    startActivity(intent);
//                }
//                else {
//                    Toast.makeText(LoginAdmin.this, "Username atau Password Anda Salah", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
    }
    private void userLogin(String username, String password) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ResponseBody> call = apiInterface.loginAdmin(new User(username, password));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray json = new JSONArray(response.body().string());
                        String username = json.getJSONObject(0).getString("username");
                        String password = json.getJSONObject(0).getString("password");

                        session.setLoggedInStatus(true);
                        session.setUsername(username);
                        session.setPassword(password);

                        Intent intent = new Intent(getApplicationContext(), PembukaAdmin.class);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Credentials are not Valid.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void handleLoginGuru(View view) {
        Intent intent = new Intent(LoginAdmin.this, MainActivity.class);
        startActivity(intent);
    }

}
