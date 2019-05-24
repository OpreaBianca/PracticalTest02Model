package ro.pub.cs.systems.eim.lab06.practicaltest02.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private Button connect_button = null;
    private Button start_server = null;
    private EditText send_to_server = null;
    private TextView from_server = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        connect_button = findViewById(R.id.connect_to_server);
        start_server = findViewById(R.id.start_server_button);
        send_to_server = findViewById(R.id.to_server);
        from_server = findViewById(R.id.text_view);

        connect_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = send_to_server.getText().toString();
                String clientAddress = "localhost";
                String clientPort = "6000";
                if (serverThread == null || !serverThread.isAlive()) {
                    Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                    return;
                }

                clientThread = new ClientThread(clientAddress, Integer.parseInt(clientPort), text, from_server);
                clientThread.start();

            }
        });

        start_server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serverPortEditText = "6000";

                serverThread = new ServerThread(Integer.parseInt(serverPortEditText));
                serverThread.start();

            }
        });
    }
}
