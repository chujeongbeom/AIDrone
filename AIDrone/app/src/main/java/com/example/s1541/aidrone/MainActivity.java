package com.example.s1541.aidrone;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import  ai.api.AIListener;
import  ai.api.android.AIConfiguration;
import  ai.api.android.AIService;
import  ai.api.model.AIError;
import  ai.api.model.AIResponse;
import  ai.api.model.Result;
import  com.google.gson.JsonElement;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import  java.util.Map;

import android.util.Log;
import  android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements AIListener {    //AI를 사용하기 위한 implements

    ImageButton listenButton;   //마이크 모양 Image Button
    TextView resultTextView;    //결과가 띄워질 Text View
    TextView listen;            //드론에게 전송하고 받은 명령이 띄워질 Edit Text
    Button button;

    //AI
    private AIService aiService;    //A.I.를 사용하기 위해
    //udp Server
    //private final String sIP = "192.168.10.1";  //서버주소
    //public static final int sPORT = 8889;   //사용할 통신 포트
    public  SendData mSendData = null;  //데이터 보낼 클래스

    String parameterString = "";    //Entity를 받는 문자열
    String command;     //드론에게 내릴 명령어 문자열

    Result result;  //전역 필드화 함으로써 모든 곳에 Query와 Intent, Entity를 사용할 수 있다.

    ListView m_ListView;    //list view 사용 객체
    CustomAdapter m_CustomAdapter;

    final AIConfiguration config = new AIConfiguration("7f043221c84a4f1eba0c7dcd8ae332b9", AIConfiguration.SupportedLanguages.Korean, AIConfiguration.RecognitionEngine.System);
    //A.I.설정(A.I.의 주소, 언어설정, 분석 시스템)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SystemClock.sleep(2000);
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listenButton  = (ImageButton) findViewById(R.id.listenButton);  //Image Button을 사용하기 위해 연결
        listen = (TextView) findViewById(R.id.Listen);                  //Edit Text를 사용하기 위해 연결
        button = (Button) findViewById(R.id.button);
        button.setEnabled(false);

        aiService = AIService.getService(this, config); //A.I. 설정을 A.I. Service에 넣음
        aiService.setListener(this);

        m_CustomAdapter = new CustomAdapter();  //커스텀 어댑터 생성
        m_ListView = (ListView) findViewById(R.id.listView1);   //xml에서 추가한 List View 연결
        m_ListView.setAdapter(m_CustomAdapter); //리스트뷰에 어댑터 연결

    }

    public void listenButtonOnClick(final View view) {  //Image Button이 눌린다면
        aiService.startListening();     //인식 시작
    }

    public void onResult(final AIResponse response) {   //A.I. 결과
        result = response.getResult();      //result에 결과값 받기

        if(result.getResolvedQuery().equals("1번 주행")) {     //만약 Query문이 "1번 주행"이라면 if문 안에 들어가겠다
            FreeDrive1 freeDrive1 = new FreeDrive1();   //1번 자율 주행 class를 사용하기 위하여 객체 생성
            freeDrive1.start();     //1번 자율 주행 class 시작
        } else if(result.getResolvedQuery().equals("이번 주 행") || result.getResolvedQuery().equals("2번 주행")) {  //만약 Query문이 "2번 주행"이라면 if문 안에 들어가겠다
            FreeDrive2 freeDrive2 = new FreeDrive2();   //2번 자율 주행 class를 사용하기 위하여 객체 생성
            freeDrive2.start();     //2번 자율 주행 class 시작
        } else if(result.getResolvedQuery().equals("3번 주행")) {    //만약 Query문이 "3번 주행"이라면 if문 안에 들어가겠다
            FreeDrive3 freeDrive3 = new FreeDrive3();   //3번 자율 주행 class를 사용하기 위하여 객체 생성
            freeDrive3.start();     //3번 자율 주행 class 시작
        } else {
            mSendData = new SendData(); //SendData 클래스 생성
            mSendData.start();  //보내기 시작
        }

        //Get prameters
        if(result.getParameters() != null && !result.getParameters().isEmpty()) {   //만약 parameter값이 nul이 아니고 parameter값이 비어 있지 않다면
            for (final Map.Entry<String, JsonElement> entry: result.getParameters().entrySet()) {   //Map에 저장되어 있는 parameter만큼 돌려라
                parameterString = "(" + entry.getKey() + ", " + entry.getValue() + ") ";   //parameter 값 = 키, 값
            }
        }

        listen.setText(result.getResolvedQuery());
        //m_ListView.removeAllViews();

        m_CustomAdapter.add(result.getResolvedQuery(), 1);

        switch (command) {
            case "" :
                m_CustomAdapter.add("잘 모르겠습니다.\n 다시 한번 말씀해 주시겠습니까?", 0);
                break;

            default :
                m_CustomAdapter.add("네, 알겠습니다.\n 드론에 " + result.getResolvedQuery() + "을(를) 명령하겠습니다.", 0);
                command = "";
                break;
        }

        result = null;  //result를 초기화하여 명령이 중복될 가능성 방지
    }

    @Override
    public void onError(AIError error) {    //A.I.에 Error가 난다면
        resultTextView.setText("\n\n\n\n\n" + error.toString());    //Error 내용을 띄워라
    }

    @Override
    public void onAudioLevel(float level) {}    //A.I. Audio 크기

    @Override
    public void onListeningStarted() {}     //A.I. 음성녹음 시작

    @Override
    public void onListeningCanceled() {}    //A.I. 음성녹음 취소

    @Override
    public void onListeningFinished() {}    //A.I. 음성인식 완료

    public void func01(View view) {     //비상착륙 버튼
        new Thread() {
            public void run() {
                try {
                    InetAddress serverAddr = InetAddress.getByName("192.168.10.1"); //정보를 전송할 IP 주소

                    DatagramSocket socket = new DatagramSocket();   //소켓 선언

                    byte[] buf = ("command").getBytes();    //보낼 정보가 들어간 메모리 작성
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);  //패킷에 정보가 들어간 메모리와 IP주소, port

                    socket.send(packet);    //소켓에 패킷(데이터의 블럭 단위)의 내용을 전달

                    buf = ("land").getBytes();
                    packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);

                    socket.send(packet);

                    String msg = new String(packet.getData());
                    listen.setText("드론을 비상 " + msg + " 하겠습니다.");

                } catch(Exception e) {
                    e.printStackTrace();    //Exception의 내용 출력
                }
            }
        }.start();
    }

    public void intent(View view) {
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        startActivity(intent);
        finish();
    }

    public void intent2(View view) {
        Intent intent = new Intent(MainActivity.this, Main3Activity.class);
        startActivity(intent);
        finish();
    }

    //데이터 보내는 쓰레드 클래스 I.o.t를 위한 UDP 통신
    class SendData extends Thread {     //Thread Class 상속
        public void run() {     //Class가 호출 되면 자동으로 실행
            try {
                command = "";            //드론에게 내릴 명령어를 넣을 문자열 초기화

                switch (result.getResolvedQuery()) {    //음성인식 후의 Query내용에 따른 명령어 저장
                    case "띄워" :
                        command = "takeoff";
                        break;
                    case "날아" :
                        command = "takeoff";
                        break;
                    case "이륙" :
                        command = "takeoff";
                        break;
                    case "착륙" :
                        command = "land";
                        break;
                    case "전방 플립" :
                        command = "flip f";
                        break;
                    case "전방 필립" :
                        command = "flip f";
                        break;
                    case "앞으로 필립" :
                        command = "flip f";
                        break;
                    case "앞으로 플립" :
                        command = "flip f";
                        break;
                    case "후방 플립" :
                        command = "flip b";
                        break;
                    case "후방 필립" :
                        command = "flip b";
                        break;
                    case "뒤로 필립" :
                        command = "flip b";
                        break;
                    case "뒤로 플립" :
                        command = "flip b";
                        break;
                    case "좌측 플립" :
                        command = "flip l";
                        break;
                    case "좌측 필립" :
                        command ="flip l";
                        break;
                    case "왼쪽 플립" :
                        command = "flip l";
                        break;
                    case "왼쪽 필립" :
                        command ="flip l";
                        break;
                    case "우측 플립" :
                        command = "flip r";
                        break;
                    case "우측 필립" :
                        command = "flip r";
                        break;
                    case "오른쪽 플립" :
                        command = "flip r";
                        break;
                    case "오른쪽 필립" :
                        command = "flip r";
                        break;
                    case "전진" :
                        command = "forward 100";
                        break;
                    case "앞으로" :
                        command = "forward 100";
                        break;
                    case "뒤로" :
                        command = "back 100";
                        break;
                    case "후진" :
                        command = "back 100";
                        break;
                    case "위로" :
                        command = "up 50";
                        break;
                    case "아래로" :
                        command = "down 50";
                        break;
                    case "왼쪽으로" :
                        command = "left 100";
                        break;
                    case "오른쪽으로" :
                        command = "right 100";
                        break;
                    case "좌회전" :
                        command = "ccw 90";
                        break;
                    case "우회전" :
                        command = "cw 90";
                        break;
                    case "베터리" :
                        command = "Battery?";
                        break;
                    case "배터리" :
                        command = "Battery?";
                        break;
                    case "시간" :
                        command = "Time?";
                        break;
                    default :
                        break;
                }

                InetAddress serverAddr = InetAddress.getByName("192.168.10.1"); //서버 주소

                DatagramSocket socket = new DatagramSocket();   //UDP 통신용 소켓 생성

                byte[] buf = ("command").getBytes();    //보낼 데이터 생성
                DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);  //패킷으로 변경

                socket.send(packet);    //패킷 전송

                buf = (command).getBytes();
                packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);

                socket.send(packet);

                socket.receive(packet);

                String msg = new String(packet.getData());  //데이터 수신 되었다면 문자열로 변환

                listen.setText("드론이 " + msg + " 입니다.");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //1번 자율주행 데이터 보내는 쓰레드 클래스
    class FreeDrive1 extends Thread {
        public void run() {
            try {
                command = "1번 주행";

                InetAddress serverAddr = InetAddress.getByName("192.168.10.1"); //서버 주소

                DatagramSocket socket = new DatagramSocket();   //UDP 통신용 소켓 생성

                byte[] buf = ("command").getBytes();    //보낼 데이터 생성
                DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);  //패킷으로 변경

                socket.send(packet);    //패킷 전송

                buf = ("takeoff").getBytes();
                packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);

                socket.send(packet);

                //드론 Action 후 시간이 흐름
                sleep(8000);    //상승 시간 때문에 8초가 적당

                //자율 주행 코딩할 부분-------------------------------------------------------------

                buf = ("flip f").getBytes();
                packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);

                socket.send(packet);

                sleep(3000);    //앞에서 내린 명령어 수행 중인 드론의 delay time 때문에 중간 중간 해당 숫자만큼의 millisecnds만큼 멈춰 준다

                buf = ("forward 100").getBytes();
                packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);

                socket.send(packet);

                sleep(3000);    //단순 이동은 금방 진행되기에 짧게 시간

                buf = ("flip b").getBytes();
                packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);

                socket.send(packet);

                sleep(3000);

                buf = ("back 100").getBytes();
                packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);

                socket.send(packet);

                sleep(3000);

                //----------------------------------------------------------------------------------

                buf = ("land").getBytes();
                packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);

                socket.send(packet);

                String msg = new String(packet.getData());  //데이터 수신 되었다면 문자열로 변환
                listen.setText("드론을 " + msg + " 하겠습니다.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //2번 자율주행 데이터 보내는 쓰레드 클래스
    class FreeDrive2 extends Thread {
        public void run() {
            try {
                command = "2번 주행";

                InetAddress serverAddr = InetAddress.getByName("192.168.10.1"); //서버 주소

                DatagramSocket socket = new DatagramSocket();   //UDP 통신용 소켓 생성

                byte[] buf = ("command").getBytes();    //보낼 데이터 생성
                DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);  //패킷으로 변경

                socket.send(packet);    //패킷 전송

                buf = ("takeoff").getBytes();
                packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);

                socket.send(packet);

                sleep(8000);    //상승 시간 때문에 8초가 적당

                //자율주행 코딩할 부분--------------------------------------------------------------

                buf = ("forward 150").getBytes();
                packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);

                socket.send(packet);

                sleep(3000);

                buf = ("cw 90").getBytes();
                packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);

                socket.send(packet);

                sleep(3000);

                buf = ("forward 150").getBytes();
                packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);

                socket.send(packet);

                sleep(3000);

                buf = ("cw 90").getBytes();
                packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);

                socket.send(packet);

                sleep(3000);

                buf = ("forward 150").getBytes();
                packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);

                socket.send(packet);

                sleep(3000);

                buf = ("cw 90").getBytes();
                packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);

                socket.send(packet);

                sleep(3000);

                buf = ("forward 150").getBytes();
                packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);

                socket.send(packet);

                sleep(3000);

                //----------------------------------------------------------------------------------

                buf = ("land").getBytes();
                packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);

                socket.send(packet);

                String msg = new String(packet.getData());  //데이터 수신 되었다면 문자열로 변환
                listen.setText("드론을 " + msg + " 하겠습니다.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //3번 자율주행 데이터 보내는 쓰레드 클래스
    class FreeDrive3 extends Thread {
        public void run() {
            try {
                command = "3번 주행";

                InetAddress serverAddr = InetAddress.getByName("192.168.10.1"); //서버 주소

                DatagramSocket socket = new DatagramSocket();   //UDP 통신용 소켓 생성

                byte[] buf = ("command").getBytes();    //보낼 데이터 생성
                DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);  //패킷으로 변경

                socket.send(packet);    //패킷 전송

                buf = ("takeoff").getBytes();
                packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);

                socket.send(packet);

                sleep(8000);    //상승 시간 때문에 8초가 적당

                //자율주행 코딩할 부분 -------------------------------------------------------------

                buf = ("flip f").getBytes();
                packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);

                socket.send(packet);

                sleep(3000);

                buf = ("flip l").getBytes();
                packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);

                socket.send(packet);

                sleep(3000);

                buf = ("flip r").getBytes();
                packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);

                socket.send(packet);

                sleep(3000);

                buf = ("flip b").getBytes();
                packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);

                socket.send(packet);

                sleep(3000);

                buf = ("cw 400").getBytes();
                packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);

                socket.send(packet);

                sleep(4000);

                buf = ("ccw 400").getBytes();
                packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);

                socket.send(packet);

                sleep(4000);
                //----------------------------------------------------------------------------------

                buf = ("land").getBytes();
                packet = new DatagramPacket(buf, buf.length, serverAddr, 8889);

                socket.send(packet);

                String msg = new String(packet.getData());  //데이터 수신 되었다면 문자열로 변환
                listen.setText("드론을 " + msg + " 하겠습니다.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}