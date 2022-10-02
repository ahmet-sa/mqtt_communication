package com.example.mqttuzaktan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {
    static String MQTTHOST = "tcp://broker.emqx.io:1883";
    static String USERNAME = "emqx";
    static String PASSWORD = "public";
    String topicStr = "LED";
    MqttAndroidClient client;
    TextView txt_StatusDurum;
    String topicDurum = "durum";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), MQTTHOST, clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());
        txt_StatusDurum = findViewById(R.id.txt_yon);
        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(MainActivity.this,"Bağlandı...",Toast.LENGTH_LONG).show();
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(MainActivity.this,"Bağlantı Yok...",Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
            }
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                if (topic.equals("durum"))
                {
                    txt_StatusDurum.setText(new String(message.getPayload()));
                }
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
            }
        });





    }
    public void ac (View v)
    {
        String topic = topicStr;
        String message = "1";
        setSubscrition();
        try
        {
            client.publish(topic, message.getBytes(),0,false);
            Toast.makeText(MainActivity.this,message.get,Toast.LENGTH_LONG).show();
        }
        catch (MqttException e)
        {
            e.printStackTrace();
        }
    }
    public void kapat (View v)
    {
        String topic = topicStr;
        String message = "2";
        setSubscrition();
        try
        {
            client.publish(topic, message.getBytes(),0,false);
        }
        catch (MqttException e)
        {
            e.printStackTrace();
        }
    }
    private void setSubscrition()
    {
        try
        {
            client.subscribe(topicDurum,0);
        }
        catch (MqttException e)
        {
            e.printStackTrace();
        }
    }


}