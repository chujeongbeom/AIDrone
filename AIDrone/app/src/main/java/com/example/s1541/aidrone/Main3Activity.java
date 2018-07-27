package com.example.s1541.aidrone;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Main3Activity extends AppCompatActivity {

    Button button;

    String command;

    public  SendData mSendData = null;  //데이터 보낼 클래스
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //화면 가로 고정

        setContentView(R.layout.activity_main3);

        button = (Button) findViewById(R.id.button3);
        button.setEnabled(false);

    }

    public void main(View view) {
        Intent intent = new Intent(Main3Activity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void intent(View view) {
        Intent intent = new Intent(Main3Activity.this, Main2Activity.class);
        startActivity(intent);
        finish();
    }

    public void landing(View view) {
        command = "land";
        mSendData = new SendData(); //SendData 클래스 생성
        mSendData.start();  //보내기 시작
    }

    public void takeoff(View view) {
        command = "takeoff";
        mSendData = new SendData(); //SendData 클래스 생성
        mSendData.start();  //보내기 시작
    }

    public void down(View view) {
        command = "down 50";
        mSendData = new SendData(); //SendData 클래스 생성
        mSendData.start();  //보내기 시작
    }

    public void up(View view) {
        command = "up 50";
        mSendData = new SendData(); //SendData 클래스 생성
        mSendData.start();  //보내기 시작
    }

    public void right(View view) {
        command = "right 100";
        mSendData = new SendData(); //SendData 클래스 생성
        mSendData.start();  //보내기 시작
    }

    public void left(View view) {
        command = "left 100";
        mSendData = new SendData(); //SendData 클래스 생성
        mSendData.start();  //보내기 시작
    }

    public void forward(View view) {
        command = "forward 100";
        mSendData = new SendData(); //SendData 클래스 생성
        mSendData.start();  //보내기 시작
    }

    public void back(View view) {
        command = "back 100";
        mSendData = new SendData(); //SendData 클래스 생성
        mSendData.start();  //보내기 시작
    }

    public void cw(View view) {
        command = "cw 90";
        mSendData = new SendData(); //SendData 클래스 생성
        mSendData.start();  //보내기 시작
    }

    public void ccw(View view) {
        command = "ccw 90";
        mSendData = new SendData(); //SendData 클래스 생성
        mSendData.start();  //보내기 시작
    }

    class SendData extends Thread {     //Thread Class 상속
        public void run() {     //Class가 호출 되면 자동으로 실행
            try {

                InetAddress serverAddr = InetAddress.getByName("192.168.10.1"); //서버 주소

                DatagramSocket socket = new DatagramSocket();   //UDP 통신용 소켓 생성

                byte[] buf = ("command").getBytes();    //보낼 데이터 생성
                DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);  //패킷으로 변경

                socket.send(packet);    //패킷 전송

                buf = (command).getBytes();
                packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);

                socket.send(packet);

                command = "";

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
