package com.stk.orderingapp.ServerCallRetrofit;

import com.stk.orderingapp.Model.ImageDetails;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.PartMap;
import retrofit2.Call;

import static org.jsoup.Connection.Method.POST;

/**
 * Created by JARVIS on 22/03/2018.
 */

public interface WebServices {

    @FormUrlEncoded
    @POST("/saleperson/login")
    void salesPersonLogin(@Field("username") String username,
                          @Field("password") String password,
                          @Field("fcm_id") String fcm_id,
                          @Field("device_id") String device_id,
                          Callback<String> loginCallBack);

    @FormUrlEncoded
    @POST("/saleperson/shop_list")
    void salesPersonLogin(@Field("salesman_id") String salesman_id,
                          Callback<String> RetailerDetailsCallBack);

    @FormUrlEncoded
    @POST("/saleperson/item_list")
    void getItemDetails(@Field("salesman_id") String salesman_id,
                        Callback<String> RetailerDetailsCallBack);

    @FormUrlEncoded
    @POST("/saleperson/shop_list")
    void salesPersonLoginWithRoute(@Field("salesman_id") String salesman_id,
                                   @Field("day") String day,
                                   Callback<String> RetailerDetailsCallBack);

    @FormUrlEncoded
    @POST("/saleperson/book_order")
    void sendOrderToServer(@Field("order_date") String order_date,
                           @Field("items") String items,
                           Callback<String> RetailerDetailsCallBack);

    @FormUrlEncoded
    @POST("/products/get_distributor_orders")
    void getMyOrders(@Field("distributor_id") String distributor_id,
                     @Field("order_date") String order_date,
                     Callback<String> callback);


    @Multipart
    @POST("/Products/add_distributor_product")
    void AddProduct(@PartMap HashMap<String, Object> map,
                    Callback<String> callback);


    @Multipart
    @POST("/Products/delete_distributor_product")
    void DeleteProduct(@PartMap HashMap<String, Object> map,
                       Callback<String> callback);

    @Multipart
    @POST("/Products/update_distributor_product")
    void UpdateProduct(@PartMap HashMap<String, Object> map,
                       Callback<String> callback);


    @Multipart
    @POST("/Products/update_order_status")
    void UpdateOrderStatus(@PartMap HashMap<String, Object> map,
                           Callback<String> callback);

    @FormUrlEncoded
    @POST("/Saleperson/uploadImagesAndRemark")
    void uploadImagesAndRemark(@Field("data") JSONObject data,
                               Callback<String> callback);


    @FormUrlEncoded
    @POST("/saleperson/update_shop_latlong")
    void updateShopLocation(@Field("shop_id") String shop_id,
                            @Field("latitude") String latitude,
                            @Field("longitude") String longitude,
                            Callback<String> callback);


    @Multipart
    @POST("/Saleperson/shop_register")
    void registerShop(@PartMap HashMap<String, Object> map,
                  Callback<String> callback);

    @Multipart
    @POST("/Saleperson/salesman_attendance")
    void sendCheckReport(@PartMap HashMap<String, Object> map,
                      Callback<String> callback);


    @FormUrlEncoded
    @POST("/Saleperson/daywise_route_list")
    void getTodaysRoutes( @Field("salesman_id") String salesman_id,
                     Callback<String> callback);

    @FormUrlEncoded
    @POST("/Saleperson/offersandnotices_list")
    void getNotificationsData( @Field("salesman_id") String salesman_id,
                               Callback<String> callback);
}