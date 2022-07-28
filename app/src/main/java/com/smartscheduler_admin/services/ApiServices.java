package com.smartscheduler_admin.services;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiServices {
    @Headers(
            {"Content-Type:application/json",
                    "Authorization:key=AAAAXOHq77U:APA91bG5LRrChqd1Fm0wQK-9yOaWG2V53zFyNQosCofSALktcxNXKqmlmMvuM2PflRGX0i4fPBODzx4Uv7cNpmjF8VQu_yHHpayN8pC4HvOQNHkp0VbYXrYWSbJS8CI_nGAkMkWyMGaf"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification (@Body NotificationSender body);

}
