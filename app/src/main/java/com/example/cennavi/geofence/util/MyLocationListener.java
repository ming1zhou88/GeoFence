package com.example.cennavi.geofence.util;

import android.util.Log;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class MyLocationListener extends BDAbstractLocationListener {
    private TextView tv = null;

    @Override
    public void onReceiveLocation(BDLocation location) {
        //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
        //以下只列举部分获取经纬度相关（常用）的结果信息
        //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

        double latitude = location.getLatitude();    //获取纬度信息
        double longitude = location.getLongitude();    //获取经度信息
        float radius = location.getRadius();    //获取定位精度，默认值为0.0f

        String coorType = location.getCoorType();
        //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

        int errorCode = location.getLocType();

        Log.e("baidu", latitude + "" + longitude);

        if (this.tv != null) {
            this.tv.setText("lat:" + latitude + "lng" + longitude);
        }

        this.postMessage(longitude, latitude);
        //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
    }

    public void setTextView(TextView tv) {
        this.tv = tv;
    }

    public void postMessage(double lng, double lat) {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray arr = new JSONArray();
            JSONObject itemObject = new JSONObject();
            itemObject.put("lon", lng);
            itemObject.put("lat", lat);
            itemObject.put("time", System.currentTimeMillis());
            arr.put(0, itemObject);

            jsonObject.put("gps", arr);

            RequestParams requestParams = new RequestParams("http://datahive.minedata.cn/geofening/gpsUpload");
            requestParams.setAsJsonContent(true);
            requestParams.setBodyContent(jsonObject.toString());
            Log.e("baidu", jsonObject.toString());

            x.http().post(requestParams, new Callback.CacheCallback<String>() {
                // 发起传参为json的post请求，
                // Callback.CacheCallback<String>的泛型为后台返回数据的类型，
                // 根据实际需求更改
                @Override
                public boolean onCache(String result) {
                    return false;
                }

                @Override
                public void onSuccess(String result) {
                    //此处请求成功后的逻辑
                    Log.e("baidu", result);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.e("baidu", ex.toString());
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    Log.e("baidu", cex.toString());
                }

                @Override
                public void onFinished() {
                    Log.e("baidu", "finished");
                }
            });
        } catch (Exception e) {
            Log.e("baidu", e.toString());
        }


    }
}