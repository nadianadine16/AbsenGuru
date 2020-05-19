package id.ac.polinema.absenguru;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PembukaAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembuka_admin);
    }

    public void keAdmin(View view) {
        Intent i = new Intent(PembukaAdmin.this, AdminActivity.class);
        startActivity(i);
    }

    public void keSiswa(View view) {
        Intent i = new Intent(PembukaAdmin.this, SiswaActivity.class);
        startActivity(i);
    }

    public void keRekap(View view) {
        Intent i =new Intent(PembukaAdmin.this,Rekapan.class);
        startActivity(i);
    }
}
