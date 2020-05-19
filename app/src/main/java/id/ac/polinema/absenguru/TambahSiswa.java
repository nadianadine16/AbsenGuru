package id.ac.polinema.absenguru;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;

import id.ac.polinema.absenguru.api.ApiClient;
import id.ac.polinema.absenguru.api.ApiInterface;
import id.ac.polinema.absenguru.model.SiswaItem;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahSiswa extends AppCompatActivity {

    private EditText NimTS, NamaTS, AlamatTS, TglLahirTS, KelasTS;
    private RadioGroup radioGroup;
    private RadioButton selected;
    private Button btnTambahSiswa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_siswa);
        btnTambahSiswa = findViewById(R.id.btn_tambah_data_siswa);
//        NimTS = findViewById(R.id.edt_nim);
        NamaTS = findViewById(R.id.edt_nama_siswa);
        AlamatTS = findViewById(R.id.edt_alamat_siswa);
        KelasTS = findViewById(R.id.edt_kelas);
        radioGroup = findViewById(R.id.group_jk_siswa);
        TglLahirTS = findViewById(R.id.edt_tgl_lahir);
        TglLahirTS.setInputType(InputType.TYPE_NULL);

        TglLahirTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog picker = new DatePickerDialog(TambahSiswa.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        TglLahirTS.setText(year + "-" + (month+1) + "-" + dayOfMonth);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        btnTambahSiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahDataSiswanya();
            }
        });

    }
    private void tambahDataSiswanya() {
//        String nim = NimTS.getText().toString();
        String nama = NamaTS.getText().toString();
        String alamat = AlamatTS.getText().toString();
        selected = findViewById(radioGroup.getCheckedRadioButtonId());
        String jenis_kelamin = "";
        if (selected != null) {
            jenis_kelamin = selected.getText().toString();
        }
        String tanggal_lahir = TglLahirTS.getText().toString();
        String kelas = KelasTS.getText().toString();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> call = apiInterface.tambahSiswa(new SiswaItem(nama, alamat, jenis_kelamin, tanggal_lahir, kelas));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), SiswaActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
