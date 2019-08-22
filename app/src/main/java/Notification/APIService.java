package Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAkyvXQgc:APA91bENKE_FLXdGdKqrn2cFzBrNWr1J99TmcrOnfns3ixbx4lx4JEWFTHGsuA91dMMmcMjYY2EyoWaxqsWLQmMVJcmrM2CJaXvrMqzu-4hvv0UTys3Y-rybN4BKIy3g-bOoKIrQ0A2o"
            }
    )

    @POST("fcm/send")
    Call<MyResponce> sendNotification(@Body Sender body);
}
