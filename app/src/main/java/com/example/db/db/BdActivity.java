package com.example.db.db;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.db.R;
import com.example.db.db.helper.DatabaseHelper;
import com.example.db.db.model.Artigo;
import com.example.db.db.model.Tag;

import java.util.List;

public class BdActivity extends Activity {
    //Bd
    DatabaseHelper db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bdartigo);

        db = new DatabaseHelper(getApplicationContext());
        Tag tagphp = db.criarTag("PHP");
        Tag tagjava = db.criarTag("Java");
        Tag tagc = db.criarTag("C");
        Tag tagpython = db.criarTag("Python");

        Artigo artigo1 = db.criarArtigo("PHP: O que é e para que serve?", "https://www.hostinger.com.br/tutoriais/o-que-e-php/", 0, new long[]{tagphp.getId()});
        Artigo artigo2 = db.criarArtigo("Java: O que é e para que serve?", "https://www.hostinger.com.br/tutoriais/o-que-e-java/", 0, new long[]{tagjava.getId()});
        Artigo artigo3 = db.criarArtigo("C: O que é e para que serve?", "https://www.hostinger.com.br/tutoriais/o-que-e-c/", 0, new long[]{tagc.getId()});
        Artigo artigo4 = db.criarArtigo("Python: O que é e para que serve?", "https://www.hostinger.com.br/tutoriais/o-que-e-python/", 0, new long[]{tagpython.getId()});
        Log.e("Contagem de artigos", "Artigos: " + db.obterArtigos("").size());
        List<Artigo> todosArtigos = db.obterArtigos("");
        for (Artigo artigo : todosArtigos) {
            Log.e("Artigo", "Id: " + artigo.getId() + ", Titulo: " + artigo.getTitulo() + ", Url: " + artigo.getUrl());
        }

        RecyclerView rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        ArtigoAdapter adapter = new ArtigoAdapter(todosArtigos, this);
        rv.setAdapter(adapter);
        db.fecharBD();
    }
}