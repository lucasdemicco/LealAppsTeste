package View;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lucas.lealappsteste.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Controller.AdapterRecycler;
import Controller.Base64Custom;
import Controller.ConfigFirebase;
import Model.Treino;

public class PrincipalActivity extends AppCompatActivity {

    private final FirebaseAuth autenticacao = ConfigFirebase.getFirebaseAutenticacao();
    private final DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();
    private DatabaseReference usuarioRef, treinoRef;
    private ValueEventListener valueEventListenerUsuario;
    private String mesAnoSelecionado;

    private RecyclerView RecyclerTreinos;
    private AdapterRecycler adapterTreino;

    private Treino treino;
    private final List<Treino> treinos = new ArrayList<>();


    private MaterialCalendarView calendarView;

    public PrincipalActivity() {
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        calendarView = findViewById(R.id.calendarView);
        RecyclerTreinos = findViewById(R.id.RecyclerTreinos);

        //Adapter
        adapterTreino = new AdapterRecycler(treinos, this);

        //Config Recycler
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerTreinos.setLayoutManager(layoutManager);
        RecyclerTreinos.setHasFixedSize(true);
        RecyclerTreinos.setAdapter(adapterTreino);
        configuraCalendarView();
        swipe();
    }

    public void swipe(){

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder) {

                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags );
            }

            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluirTreino(viewHolder);
            }
        };
            new ItemTouchHelper( itemTouch).attachToRecyclerView(RecyclerTreinos);
    }

    public void excluirTreino(RecyclerView.ViewHolder viewHolder){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Excluir treino");
        alertDialog.setMessage("Você tem certeza que deseja excluir esse treino?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                treino = treinos.get(position);

                String emailUsusario = autenticacao.getCurrentUser().getEmail();
                String idUsuario = Base64Custom.codificarBase64(emailUsusario);
                usuarioRef = firebaseRef.child("Treinos")
                        .child(idUsuario)
                        .child(mesAnoSelecionado);

                usuarioRef.child( treino.getIdTreino() ).removeValue();
                adapterTreino.notifyItemRemoved(position);

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PrincipalActivity.this,
                        "Exclusão cancelada",
                        Toast.LENGTH_SHORT).show();
                adapterTreino.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void configuraCalendarView(){

        CharSequence meses [] = {"Janeiro","Fevereiro","Março","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"};
        calendarView.setTitleMonths(meses);

        CalendarDay dataAtual = calendarView.getCurrentDate();
        String mesSelecionado = String.format("%02d", (dataAtual.getMonth() + 1) );
        mesAnoSelecionado = String.valueOf( mesSelecionado + "" + dataAtual.getYear() );

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String mesSelecionado = String.format("%01d", ((date.getMonth() + 1)) );
                mesAnoSelecionado = String.valueOf( mesSelecionado + "" + date.getYear() );
                recuperarTreino();
            }
        });
    }


    public void recuperarTreino(){
        String emailUsusario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsusario);
        usuarioRef = firebaseRef.child("Treinos")
                .child(idUsuario)
                .child(mesAnoSelecionado);

        valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
               treinos.clear();
               for(DataSnapshot dados : snapshot.getChildren()){
                   Treino treino = dados.getValue(Treino.class);
                   treino.setIdTreino(dados.getKey());
                   treinos.add(treino);
               }
                Collections.reverse(treinos);
                    adapterTreino.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuprincipal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSair:
                autenticacao.signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void adicionarTreino(View v) {
        startActivity(new Intent(this, TreinosActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarTreino();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioRef.removeEventListener(valueEventListenerUsuario);
    }
}
