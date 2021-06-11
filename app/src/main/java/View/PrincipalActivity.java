package View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.lucas.lealappsteste.R;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class PrincipalActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        calendarView = findViewById(R.id.calendarView);


    }


    public void adicionarTreino(View v) {
        startActivity(new Intent(this, TreinosActivity.class));
    }
}
