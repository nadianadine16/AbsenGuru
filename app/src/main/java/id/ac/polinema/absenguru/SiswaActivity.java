package id.ac.polinema.absenguru;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import java.util.ArrayList;
import java.util.List;

import id.ac.polinema.absenguru.api.ApiClient;
import id.ac.polinema.absenguru.api.ApiInterface;
import id.ac.polinema.absenguru.helper.Session;
import id.ac.polinema.absenguru.model.AbsenGuru;
import id.ac.polinema.absenguru.model.GuruItem;
import id.ac.polinema.absenguru.model.SiswaItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiswaActivity extends AppCompatActivity {
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siswa);

        session = new Session(getApplicationContext());

        final RecyclerView siswaView = findViewById(R.id.rv_siswa);
        final ItemAdapter itemAdapter = new ItemAdapter<>();
        final FastAdapter fastAdapter = FastAdapter.with(itemAdapter);
        final List siswa = new ArrayList<>();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<SiswaItem>> call = apiInterface.getSiswa();

        call.enqueue(new Callback<List<SiswaItem>>() {
            @Override
            public void onResponse(Call<List<SiswaItem>> call, Response<List<SiswaItem>> response) {
                List<SiswaItem> SiswaItems = response.body();
                if (response.isSuccessful()) {
                    for (SiswaItem item : SiswaItems) {
                        siswa.add(new SiswaItem(item.getNim(), item.getNama(), item.getAlamat(),
                                item.getJenis_kelamin(), item.getTanggal_lahir(), item.getKelas()));
                    }

                    itemAdapter.add(siswa);
                    siswaView.setAdapter(fastAdapter);

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    siswaView.setLayoutManager(layoutManager);
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal menampilkan data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SiswaItem>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void handleLogoutSiswa(View view) {
        Intent i = new Intent(SiswaActivity.this, LoginAdmin.class);
        startActivity(i);
    }

    public void handleTambahSiswa(View view) {
        Intent i = new Intent(SiswaActivity.this, TambahSiswa.class);
        startActivity(i);
    }

    public void handlebalikSiswa(View view) {
        Intent i = new Intent(SiswaActivity.this, PembukaAdmin.class);
        startActivity(i);
    }
}
