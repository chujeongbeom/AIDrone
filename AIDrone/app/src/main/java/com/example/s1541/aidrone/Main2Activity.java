package com.example.s1541.aidrone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    Button button;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        button = (Button) findViewById(R.id.button2);
        button.setEnabled(false);

        textView = (TextView) findViewById(R.id.text);
        textView.setBackgroundResource(R.drawable.background);
        textView.setText("\n     <음성 인식 명령어>" + "\n\n     드론 이륙 - 띄워,날아,이륙\n     드론 착륙 - 착륙\n     드론 앞 뒤집기 - 전방플립, 앞으로 플립\n     드론 뒤 뒤집기 - 후방플립, 뒤로플립" +
                        "\n     드론 왼쪽 뒤집기 - 좌측 플립, 왼쪽 플립\n     드론 오른쪽 뒤집기 - 우측 플립, 오른쪽 플립\n     드론 전진 비행 - 전진, 앞으로\n     드론 후진 비행 - 뒤로, 후진" +
                        "\n     드론 상승 비행 - 위로\n     드론 하강 비행 - 아래로\n     드론 왼쪽으로 비행 - 왼쪽으로\n     드론 오른쪽으로 비행 - 오른쪽으로\n     드론 왼쪽 회전 - 좌회전" +
                        "\n     드론 오른쪽 회전 - 우회전\n\n");
    }

    public void main(View view) {
        Intent intent = new Intent(Main2Activity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void intent2(View view) {
        Intent intent = new Intent(Main2Activity.this, Main3Activity.class);
        startActivity(intent);
        finish();
    }

}
