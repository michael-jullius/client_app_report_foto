package com.roiputra.proyek.Interface;

import androidx.annotation.Nullable;

import com.roiputra.proyek.Model.ResponseDetailImage;
import com.roiputra.proyek.Model.ResponseImage;
import com.roiputra.proyek.Model.ResponseImageData;
import com.roiputra.proyek.Model.ResponseProyek;
import com.roiputra.proyek.Model.ResponseUser;
import com.roiputra.proyek.Model.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @FormUrlEncoded
    @POST("api/login")
    Call<User> login(@Field("username") String username,
                     @Field("password") String password);
    @FormUrlEncoded
    @POST("api/register")
    Call<User> register(@Field("nama_perusahaan") String nama_perusahaan,
                        @Field("nama") String nama,
                        @Field("username") String username,
                        @Field("password") String password);

    @FormUrlEncoded
    @POST("api/proyek")
    Call<ResponseProyek> getProyek(@Field("id_user") String id_user,
                                   @Field("token") String token);

    @FormUrlEncoded
    @POST("api/proyek/add")
    Call<ResponseProyek> addProyek(@Field("id_user") String id_user,
                                   @Field("token") String token,
                                   @Field("nama_proyek") String nama_proyek);

    @Multipart
    @POST("api/Galery/add")
    Call<ResponseImage> postImage(@Part("id_user") RequestBody id_user,
                                  @Part("token") RequestBody token,
                                  @Part("id_proyek") RequestBody id_proyek,
                                  @Part MultipartBody.Part file,
                                  @Nullable @Part("tukang") RequestBody tukang,
                                  @Nullable @Part("assisten_tukang") RequestBody assisten_tukang,
                                  @Nullable @Part("alokasi_pekerjaan") RequestBody alokasi_pekerjaan,
                                  @Nullable @Part("cuaca") RequestBody cuaca,
                                  @Nullable @Part("volume") RequestBody volume,
                                  @Nullable @Part("keterangan") RequestBody keterangan);

    @FormUrlEncoded
    @POST("api/Galery/date")
    Call<ResponseImage> getImageDate(@Field("id_proyek") String id_proyek,
                                     @Field("id_user") String id_user,
                                     @Field("token") String token);


    @FormUrlEncoded
    @POST("api/Galery")
    Call<ResponseImageData> getDataImage(@Field("id_proyek") String id_proyek,
                                         @Field("date") String date,
                                         @Field("id_user") String id_user,
                                         @Field("token") String token);

    @FormUrlEncoded
    @POST("api/user/add")
    Call<User> addUser(@Field("id_proyek") String id_proyek,
                       @Field("nama") String nama,
                       @Field("username") String username,
                       @Field("password") String password,
                       @Field("role") String role,
                       @Field("id_user") String id_user,
                       @Field("token") String token);


    @FormUrlEncoded
    @POST("api/user/edit")
    Call<User> editUser(@Field("nama") String nama,
                        @Field("username") String username,
                        @Field("password") String password,
                        @Field("id_user") String id_user,
                        @Field("token") String token);

    @FormUrlEncoded
    @POST("api/user/edit/management")
    Call<User> editUserData(@Field("id") String id,
                            @Field("nama") String nama,
                            @Field("username") String username,
                            @Field("password") String password,
                            @Field("role") String role,
                            @Field("id_proyek") String id_proyek,
                            @Field("id_user") String id_user,
                            @Field("token") String token);


    @FormUrlEncoded
    @POST("api/user")
    Call<ResponseUser> getUser(@Field("id_user") String id_user,
                               @Field("token") String token);


    @FormUrlEncoded
    @POST("api/user/delete")
    Call<User> deleteUser(@Field("id_user") String id_user,
                          @Field("token") String token,
                          @Field("id") String id);

    @FormUrlEncoded
    @POST("api/Galery/delete")
    Call<ResponseImage> deleteGalery(@Field("id_user") String id_user,
                                     @Field("token") String token,
                                     @Field("id") String id);

    @FormUrlEncoded
    @POST("api/Galery/detail")
    Call<ResponseDetailImage> getDetailImage(@Field("id_user") String id_user,
                                             @Field("token") String token,
                                             @Field("id_image") String id);
}
