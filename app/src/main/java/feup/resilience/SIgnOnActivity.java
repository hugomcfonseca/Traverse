package feup.resilience;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


public class SIgnOnActivity extends AppCompatActivity {

    private ImageButton porto, psg, real, chelsea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_on);

        porto = (ImageButton) findViewById(R.id.porto);
        real = (ImageButton) findViewById(R.id.real);
        psg = (ImageButton) findViewById(R.id.psg);
        chelsea = (ImageButton) findViewById(R.id.chelsea);
        //ao carregar chamar a função
        porto.setOnClickListener(myhandler);
        real.setOnClickListener(myhandler);
        psg.setOnClickListener(myhandler);
        chelsea.setOnClickListener(myhandler);

    }
    //função de click em imagem
    View.OnClickListener myhandler = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.porto:
                     Toast.makeText(SIgnOnActivity.this, "Porto", 1000).show();
                    break;
                case R.id.real:
                    Toast.makeText(SIgnOnActivity.this, "Real Madrid", 1000).show();
                    break;
                case R.id.psg:
                    Toast.makeText(SIgnOnActivity.this, "Paris Saint Germain", 1000).show();
                    break;
                case R.id.chelsea:
                    Toast.makeText(SIgnOnActivity.this, "Chelsea", 1000).show();
                    break;
                default:
                    throw new RuntimeException("Unknow button ID");
            }
        }
    };

}
