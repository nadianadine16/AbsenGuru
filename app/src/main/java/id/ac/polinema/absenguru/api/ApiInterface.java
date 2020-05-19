package id.ac.polinema.absenguru.api;

import java.util.List;
import java.util.Map;

import id.ac.polinema.absenguru.PilihSiswa;
import id.ac.polinema.absenguru.model.AbsenGuru;
import id.ac.polinema.absenguru.model.GuruItem;
import id.ac.polinema.absenguru.model.PilihSiswaItem;
import id.ac.polinema.absenguru.model.RekapItem;
import id.ac.polinema.absenguru.model.SiswaItem;
import id.ac.polinema.absenguru.model.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface ApiInterface {
    @POST("LoginAdmin")
    Call<ResponseBody> loginAdmin(@Body User user);

    @POST("LoginGuru")
    Call<ResponseBody> loginGuru(@Body User user);

    @GET("Guru")
    Call<List<GuruItem>> getGuru();

    @GET("Guru")
    Call<List<GuruItem>> getGuruByUsername(@Query("username") String username);

    @GET("AbsenGuru")
    Call<List<AbsenGuru>> getAbsenByUsername(@Query("username") String username);

    @POST("AbsenGuru")
    Call<ResponseBody> absenGuru(@Body AbsenGuru absenGuru);

    @GET("Rekapan")
    Call<List<RekapItem>> listRekap();

    @GET("Siswa")
    Call<List<SiswaItem>> getSiswa();

    @GET("Siswa")
    Call<List<PilihSiswaItem>> getSemuaSiswa();

    @POST("Siswa")
    Call<ResponseBody> tambahSiswa(@Body SiswaItem siswa);

    @Multipart
    @POST("Guru")
    Call<ResponseBody> tambahGuru(@Part MultipartBody.Part photo, @PartMap Map<String, RequestBody> text);
}
