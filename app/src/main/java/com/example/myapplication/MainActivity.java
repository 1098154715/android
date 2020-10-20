package com.example.myapplication;



import android.Manifest;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.myapplication.core.custinterface.BindView;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity implements LocationSource, AMap.OnMyLocationChangeListener {
    MapView mapView;
    AMap aMap;
    MyLocationStyle myLocationStyle;
    @BindView(id = R.id.point, click = true)
    Button point;
    Location location;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initBindView(this);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState); //此方法必须重写
        aMap = mapView.getMap();
        aMap.showIndoorMap(true);
        /*  MAP_TYPE_NORMAL = 标准模式;
            MAP_TYPE_SATELLITE = 卫星模式;
            MAP_TYPE_NIGHT = 夜间模式;
            MAP_TYPE_NAVI = 导航模式;
            MAP_TYPE_BUS = 交通模式;*/
        aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 卫星地图模式
        aMap.moveCamera(CameraUpdateFactory.zoomTo(19));
        showGPSContacts();//添加权限
        setupMapView();
        setupLocationStyle();
        point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "定位地点：" + location.getExtras().getString("Address") +
                        ",地址已发送到学孔1098154715@qq.com的邮箱，蝶儿，我要来找你了！", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupMapView() {
        // 设置默认定位按钮是否显示，非必需设置
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false
        aMap.setMyLocationEnabled(true);
        aMap.setOnMyLocationChangeListener(this);
    }

    private void setupLocationStyle() {
        myLocationStyle = new MyLocationStyle();
        // 默认模式，连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动，1秒1次定位
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        // 设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒
        myLocationStyle.showMyLocation(true);
        myLocationStyle.interval(3000);
        // 设置定位蓝点的Style
        aMap.setMyLocationStyle(myLocationStyle);
    }

    @Override
    public void onMyLocationChange(Location location) {
        this.location = location;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }

    public static void initBindView(Object currentClass) {
        Field[] fields = currentClass.getClass().getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                if (field.isAnnotationPresent(BindView.class)) {
                    BindView bindView = field.getAnnotation(BindView.class);
                    int viewId = bindView.id();
                    try {
                        field.setAccessible(true);
                        field.set(currentClass, ((Activity) currentClass).findViewById(viewId));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    /**
     * 检测GPS、位置权限是否开启
     */
    public void showGPSContacts() {
        int num = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(num!=0){//未获取该权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
    }
}