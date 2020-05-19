package id.ac.polinema.absenguru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.ac.polinema.absenguru.api.ApiClient;
import id.ac.polinema.absenguru.api.ApiInterface;
import id.ac.polinema.absenguru.helper.Session;
import id.ac.polinema.absenguru.model.AbsenGuru;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuruActivity extends AppCompatActivity {

    private ImageView profil;
    private TextView id_guru, nama, alamat, jenis_kelamin, no_telp, username, password, namasiswa, nimsiswa, kelassiswa;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guru);

        profil = findViewById(R.id.foto_profil);
        id_guru = findViewById(R.id.id_guru);
        nama = findViewById(R.id.nama_guru);
        alamat = findViewById(R.id.alamat_guru);
        jenis_kelamin = findViewById(R.id.jenis_kelamin_guru);
        no_telp = findViewById(R.id.telp_guru);
        username = findViewById(R.id.username_guru);
        password = findViewById(R.id.password_guru);

        session = new Session(getApplicationContext());

        final RecyclerView absenView = findViewById(R.id.rv_absen);
        final ItemAdapter itemAdapter = new ItemAdapter<>();
        final FastAdapter fastAdapter = FastAdapter.with(itemAdapter);
        final List absen = new ArrayList<>();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<AbsenGuru>> call1 = apiInterface.getAbsenByUsername(session.getUsername());

        call1.enqueue(new Callback<List<AbsenGuru>>() {
            @Override
            public void onResponse(Call<List<AbsenGuru>> call, Response<List<AbsenGuru>> response) {
                String x = getIntent().getStringExtra("nim");
                String y = getIntent().getStringExtra("nama");
                String z = getIntent().getStringExtra("kelas");
                if (response.isSuccessful()) {
                    List<AbsenGuru> absenItems = response.body();

                    for (AbsenGuru item : absenItems) {
                        absen.add(new AbsenGuru(item.getUsername(), item.getPassword(), item.getJam_login(),
                                item.getJam_logout(), item.getTanggal(), item.getLokasi_latitude(),
                                item.getLokasi_longitude(), item.getNim(),item.getNama(),item.getKelas()));
                    }

                    absen.add(new AbsenGuru(session.getUsername(), session.getPassword(), session.getLoginTime(),
                            session.getLogoutTime(), session.getDate(), session.getLocLatitude(), session.getLocLongitude(),x,y,z));

                    itemAdapter.add(absen);
                    absenView.setAdapter(fastAdapter);

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    absenView.setLayoutManager(layoutManager);
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal mengambil data, Silahkan Coba Lagi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AbsenGuru>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void AlertMessageNoGps() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(GuruActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
    public void handleLogout(View view) {
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        session.setLogoutTime(currentTime);

        String username = session.getUsername();
        String password = session.getPassword();
        String jam_login = session.getLoginTime();
        String jam_logout = session.getLogoutTime();
        String tanggal = session.getDate();
        double lokasi_latitude = session.getLocLatitude();
        double lokasi_longitude = session.getLocLongitude();
        String nimsiswa = getIntent().getStringExtra("nim");
        String namasiswa = getIntent().getStringExtra("nama");
        String kelassiswa = getIntent().getStringExtra("kelas");

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.absenGuru(new AbsenGuru(username, password, jam_login, jam_logout, tanggal, lokasi_latitude, lokasi_longitude,nimsiswa,namasiswa,kelassiswa));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    session.logout();
                    Toast.makeText(getApplicationContext(), "Logout Berhasil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Server Bermasalah, Silahkan Coba Lagi", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
