package View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.lucas.lealappsteste.R;

public class BemVindo extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_bem_vindo);
        setButtonBackVisible(false);
        setButtonNextVisible(false);


        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.black)
                .fragment(R.layout.intros)
                .canGoBackward(false)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intros2)
                .canGoForward(false)
                .build()
        );
    }

    public void irParaAuthActivity(View view) {
       startActivity(new Intent(this, LoginActivity.class));
       finish();
    }
}
