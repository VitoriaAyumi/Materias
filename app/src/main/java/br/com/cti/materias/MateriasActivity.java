package br.com.cti.materias;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MateriasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_materias);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        RecyclerView recyclerView = findViewById(R.id.recycler);

        FloatingActionButton add = findViewById(R.id.addMateria);
        add.setOnClickListener(view -> startActivity(new Intent(MateriasActivity.this, MateriasAddActivity.class)));

        db.collection("materias").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(MateriasActivity.this, "Falha ao carregar dados: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<Materias> arrayList = new ArrayList<>();
                assert value != null;
                for (QueryDocumentSnapshot document : value) {
                    Materias materias = document.toObject(Materias.class);
                    materias.setId(document.getId());
                    arrayList.add(materias);
                }

                MateriasAdapter adapter = new MateriasAdapter(MateriasActivity.this, arrayList);
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new MateriasAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(Materias materias) {
                        App.materias = materias;
                        startActivity(new Intent(MateriasActivity.this, MateriasEditActivity.class));
                    }
                });
            }
        });
    }
}