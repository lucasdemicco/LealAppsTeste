package Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lucas.lealappsteste.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import Model.Treino;

public class AdapterRecycler extends RecyclerView.Adapter<AdapterRecycler.MyViewHolder> {

    List<Treino> treinos;
    Context context;

    public AdapterRecycler(List<Treino> treinos, Context context) {
        this.treinos = treinos;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public AdapterRecycler.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapterprincipal, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterRecycler.MyViewHolder holder, int position) {

        Treino treino = treinos.get(position);

        holder.nomeTreino.setText(treino.getNome());
        holder.dataTreino.setText(treino.getData());
        holder.descricaoTreino.setText(treino.getDescricao());

    }

    @Override
    public int getItemCount() {
        return treinos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nomeTreino, dataTreino, descricaoTreino;
        ImageView imgTreino;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgTreino = itemView.findViewById(R.id.imgTreino);
            nomeTreino = itemView.findViewById(R.id.txtNomeTreino);
            dataTreino = itemView.findViewById(R.id.txtDataTreino);
            descricaoTreino = itemView.findViewById(R.id.txtDescricaoTreino);
        }
    }
}
