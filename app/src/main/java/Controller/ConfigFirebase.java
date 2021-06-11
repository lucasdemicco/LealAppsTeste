package Controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfigFirebase {

    private static FirebaseAuth autenticacao;
    public static DatabaseReference firebase;

    public static DatabaseReference getFirebaseDatabase(){
        if(firebase ==null ){
            firebase = FirebaseDatabase.getInstance().getReference();
        }
        return firebase;
    }

    //Retorna instância do FirebaseAuth
    public static FirebaseAuth getFirebaseAutenticacao() {
        if (autenticacao == null) {
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }

}
