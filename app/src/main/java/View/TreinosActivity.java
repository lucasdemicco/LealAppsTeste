package View;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.lucas.lealappsteste.R;

public class TreinosActivity extends AppCompatActivity {

    private ImageView imgTreino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treinos);

        imgTreino = findViewById(R.id.imgTreino);
    }
}