package id.ac.polinema.absenguru;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
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
import id.ac.polinema.absenguru.model.RekapItem;
import id.ac.polinema.absenguru.model.SiswaItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Rekapan extends AppCompatActivity {
    TextView rekap;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekapan);

        session = new Session(getApplicationContext());

        final RecyclerView rekapView = findViewById(R.id.rvRekap);
        final ItemAdapter itemAdapter = new ItemAdapter<>();
        final FastAdapter fastAdapter = FastAdapter.with(itemAdapter);
        final List rekap = new ArrayList<>();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<RekapItem>> call = apiInterface.listRekap();

        call.enqueue(new Callback<List<RekapItem>>() {
            @Override
            public void onResponse(Call<List<RekapItem>> call, Response<List<RekapItem>> response) {
                List<RekapItem> RekapItems = response.body();
                if (response.isSuccessful()) {
                    for (RekapItem item : RekapItems) {
                        rekap.add(new RekapItem(item.getUsername(), item.getPassword(), item.getRekap()));
                    }

                    itemAdapter.add(rekap);
                    rekapView.setAdapter(fastAdapter);

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    rekapView.setLayoutManager(layoutManager);
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal menampilkan data", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<RekapItem>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void handleLogoutRekap(View view) {
        Intent i = new Intent(Rekapan.this, LoginAdmin.class);
        startActivity(i);
    }
}
