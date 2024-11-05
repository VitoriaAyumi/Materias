package br.com.cti.materias;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MateriasEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_materias);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        TextInputEditText nomeMateriaET = findViewById(R.id.nomeMateriaET);
        TextInputEditText nomeProfessorET = findViewById(R.id.nomeProfessorET);

        MaterialButton save = findViewById(R.id.save);
        MaterialButton delete = findViewById(R.id.delete);

        nomeProfessorET.setText(App.materias.getNomeProf());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(MateriasEditActivity.this)
                        .setTitle("Confirmar Exclusão")
                        .setMessage("Tem certeza que deseja deletar essas Materias?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                db.collection("materias").document(App.materias.getId()).delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(MateriasEditActivity.this, "Materias Deletada Com Sucesso!!", Toast.LENGTH_SHORT).show();
                                                Intent resultIntent = new Intent();
                                                setResult(RESULT_OK, resultIntent);
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("EditMaterias", "Erro Ao Deletar As Materias!!", e);
                                                Toast.makeText(MateriasEditActivity.this, "Erro Ao Deletar As Materias!!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("Não", null)
                        .show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MateriasEditActivity.this)
                        .setTitle("Confirmar Alteração")
                        .setMessage("Tem certeza que deseja salvar as alterações?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String nomeMateria = Objects.requireNonNull(nomeMateriaET.getText()).toString().trim();
                                String nomeProfessor = Objects.requireNonNull(nomeProfessorET.getText()).toString().trim();

                                if (nomeMateria.isEmpty() || nomeProfessor.isEmpty()) {
                                    Toast.makeText(MateriasEditActivity.this, "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                Map<String, Object> Materias = new HashMap<>();
                                Materias.put("nomeMateria", Objects.requireNonNull(nomeMateriaET.getText()).toString());
                                Materias.put("nomeProf", Objects.requireNonNull(nomeProfessorET.getText()).toString());

                                db.collection("materias").document(App.materias.getId()).update(Materias)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(MateriasEditActivity.this, "Materias Alteradas Com Sucesso!!", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("EditMaterias", "Erro ao salvar as Materias", e);
                                                Toast.makeText(MateriasEditActivity.this, "Erro ao salvar as Materias", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("Não", null)
                        .show();
            }
        });
        Log.d("EditMaterias", "App.Materias.getId(): " + App.materias.getId());

    }
}