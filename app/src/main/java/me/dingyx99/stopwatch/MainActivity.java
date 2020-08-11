package me.dingyx99.stopwatch;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    Button bt_start, bt_stop, bt_clear;
    Thread thread;
    long time0, time1;
    enum State {STATE_CLEAR, STATE_RUN, STATE_STOP};
    State S;

    Runnable work = new Runnable() {
        @Override
        public void run() {
            try {
                while ( !Thread.interrupted()) {
                    if(S == State.STATE_RUN) {
                        time1 = System.currentTimeMillis();
                        display();
                    }
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
            }
        }
    };

    void display() {
        tv.post(new Runnable() {
            @Override
            public void run() {
                long min,sec,msec;
                long temp = time1 - time0;
                String dmin,dsec,dmsec;
                msec = temp % 1000;
                temp = temp/1000;
                sec = temp%60;
                temp = temp/60;
                min = temp%60;
                dmin = String.format("%2d", min).replace(" ","0");
                dsec = String.format("%2d", sec).replace(" ","0");
                dmsec = String.format("%3d", msec).replace(" ","0");
                tv.setText(dmin+":"+dsec+":"+dmsec);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        init();

        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(S != State.STATE_RUN) {
                    S=State.STATE_RUN;
                    time0 = System.currentTimeMillis();
                    time1 = time0;
                }
            }
        });

        bt_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                S = State.STATE_STOP;
            }
        });

        bt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                S = State.STATE_CLEAR;
                time0 = 0;
                time1 = 0;
                tv.setText("00:00:000");
            }
        });
    }

    public void init() {
        tv = (TextView) findViewById(R.id.displayTime);
        bt_start = (Button) findViewById(R.id.btn_start);
        bt_stop = (Button) findViewById(R.id.btn_stop);
        bt_clear = (Button) findViewById(R.id.btn_clear);
        thread = new Thread(work);
        S=State.STATE_CLEAR;
        time0 = 0;
        time1 = 0;
    }


    @Override
    protected void onStart() {
        super.onStart();
        thread.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        thread.interrupt();
    }
}