package br.com.cti.materias;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MateriasAddActivity extends AppCompatActivity {

    private static final String TAG = "AddMaterias";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_add_materias);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        TextInputEditText nomeMateriaET = findViewById(R.id.nomeMateriaET);
        TextInputEditText nomeProfessorET = findViewById(R.id.nomeProfessorET);

        MaterialButton addMaterias = findViewById(R.id.addMaterias);

        addMaterias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nomeMateria = Objects.requireNonNull(nomeMateriaET.getText()).toString().trim();
                String nomeProf = Objects.requireNonNull(nomeProfessorET.getText()).toString().trim();

                if (nomeMateria.isEmpty() || nomeProf.isEmpty()) {
                    Toast.makeText(MateriasAddActivity.this, "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Object> materias = new HashMap<>();
                materias.put("nomeMateria", Objects.requireNonNull(nomeMateriaET.getText()).toString());
                materias.put("nomeProf", Objects.requireNonNull(nomeProfessorET.getText()).toString());

                db.collection("materias").add(materias).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MateriasAddActivity.this, "Mateiras adicionadas com sucesso!!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MateriasAddActivity.this, "Falha Ao Tentar Adicionar Matéria: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Erro ao adicionar matéria", e);
                    }
                });
            }
        });

    }

}