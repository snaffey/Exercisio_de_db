package com.example.db.db.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.db.db.model.Artigo;
import com.example.db.db.model.Tag;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Nome da base de dados
    private static final String DATABASE_NAME = "SergioManager";
    // Versão da base de dados
    private static final int DATABASE_VERSION = 1;
    // Nome das tabelas
    private static final String TABLE_ARTIGO = "artigo";
    private static final String TABLE_TAG = "tag";
    private static final String TABLE_ARTIGO_TAG = "artigo_tag";
    // Nomes das colunas das tabelas
    private static final String KEY_ID = "id";
    private static final String KEY_TITUTLO = "titulo";
    private static final String KEY_URL = "url";
    private static final String KEY_ESTADO = "estado";
    private static final String KEY_DATA_CRIACAO = "data_criacao";
    private static final String KEY_NOME = "nome";
    private static final String KEY_ARTIGO_ID = "artigo_id";
    private static final String KEY_TAG_ID = "tag_id";

    //Tag para o LogCat
    private static final String LOG = "DatabaseHelper";

    // Instrução SQL para criação da tabela ARTIGO
    private static final String CREATE_TABLE_ARTIGO = "CREATE TABLE "+TABLE_ARTIGO+" ( "+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_TITUTLO+" TEXT, "+KEY_URL+" TEXT, "+KEY_ESTADO+" INTEGER, "+KEY_DATA_CRIACAO+" DATETIME )";

    // Instrução SQL para criação da tabela TAG
    private static final String CREATE_TABLE_TAG = "CREATE TABLE "+TABLE_TAG+" ( "+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_NOME+" TEXT )";

    // Instrução SQL para criação da tabela ARTIGO_TAG
    private static final String CREATE_TABLE_ARTIGO_TAG = "CREATE TABLE " + TABLE_ARTIGO_TAG + " ( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_ARTIGO_ID + " INTEGER, " + KEY_TAG_ID + " INTEGER)";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criação das tabelas
        db.execSQL(CREATE_TABLE_ARTIGO);
        db.execSQL(CREATE_TABLE_TAG);
        db.execSQL(CREATE_TABLE_ARTIGO_TAG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Remoção das tabelas existentes
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ARTIGO);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_TAG);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ARTIGO_TAG);
        // Criação das novas tabelas
        onCreate(db);
    }

    // Associar uma tag a um artigo
    public long associarArtigoTag(long artigo_id, long tag_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ARTIGO_ID, artigo_id);
        values.put(KEY_TAG_ID, tag_id);
        /*
        insert()
        Parametros
        1 - nome da tabela
        2 - o que fazer se ContentValues esteja vazio
        Se especificarmos o nome de uma coluna, a estrutura insere uma linha e termina o valor dessa coluna como nulo
        Se especificar null , a estrutra não insire uma linha quando guarda valores. Retorna a identificação da linha recem inserida ou -1 se ouver erro devolvendo um long
        3 - valores
         */
        long id = db.insert(TABLE_ARTIGO_TAG, null, values);
        return id;
    }

    // Criar um artigo e associar tags
    public Artigo criarArtigo(String titulo, String url, int estado, long[] tag_ids) {
        Artigo artigo = new Artigo(titulo, url, estado);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITUTLO, artigo.getTitulo());
        values.put(KEY_URL, artigo.getUrl());
        values.put(KEY_ESTADO, artigo.getEstado());
        values.put(KEY_DATA_CRIACAO, getDateTime());
        // insereir artigo
        int artigo_id = (int) db.insert(TABLE_ARTIGO, null, values);
        artigo.setId(artigo_id);
        for (long tag_id : tag_ids) {
            associarArtigoTag(artigo_id, tag_id);
        }
        return artigo;
    }

    //Criar tag
    public Tag criarTag(String nome) {
        SQLiteDatabase db = this.getWritableDatabase();
        Tag tag = new Tag(nome);
        ContentValues values = new ContentValues();
        values.put(KEY_NOME, tag.getNome());
        int tag_id = (int) db.insert(TABLE_TAG, null, values);
        tag.setId(tag_id);
        return tag;
    }

    // COnsulta de um artigo dado um id de artigo
    @SuppressLint("Range")
    public Artigo obterArtigo(long artigo_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ARTIGO + " WHERE " + KEY_ID + " = " + artigo_id;
        Log.e(LOG, query);
        /*
        Primerio parametro: A query sql
        Segundo parametro: Os slectionArgs são os valores para os parametros da query que sao defenido por "?"
        Ex: rawQuery("SELECT * FROM people WHERE name = ? AND id = ?", new String[] {"David", "2"});
        No exemplo seria;
        Cursor c = db.rawQuery("SELECT * FROM TABLE_ARTIGO WHERE KEY_ID = ?", new String[] {artigo_id});
        */
        Cursor c = db.rawQuery(query, null);
        if (c != null) {
            c.moveToFirst();
            Artigo artigo = new Artigo();
            artigo.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            artigo.setTitulo(c.getString(c.getColumnIndex(KEY_TITUTLO)));
            artigo.setUrl(c.getString(c.getColumnIndex(KEY_URL)));
            artigo.setEstado(c.getInt(c.getColumnIndex(KEY_ESTADO)));
            artigo.setDataCriacao(c.getString(c.getColumnIndex(KEY_DATA_CRIACAO)));
            return artigo;
        }
        return null;
    }

    // Devolver todos os artigos numa lista
    public List<Artigo> obterArtigos(String nomeTag){
        String query;
        List<Artigo> artigos = new ArrayList<Artigo>();
        if(nomeTag.equals(""))
            query = "SELECT * FROM "+TABLE_ARTIGO;
        else
            query = "SELECT * FROM "+TABLE_ARTIGO+" ta, "+TABLE_TAG+" tt, "+TABLE_ARTIGO_TAG+" tat WHERE tt."+KEY_NOME+" = '"+nomeTag+"' AND tt."+KEY_ID+" = tat."+KEY_TAG_ID+" AND ta."+KEY_ID+" = tat."+KEY_ARTIGO_ID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        // Iterar
        if(c.moveToFirst()){
            do{
                Artigo artigo = new Artigo();
                artigo.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                artigo.setTitulo(c.getString(c.getColumnIndex(KEY_TITUTLO)));
                artigo.setUrl(c.getString(c.getColumnIndex(KEY_URL)));
                artigo.setEstado(c.getInt(c.getColumnIndex(KEY_ESTADO)));
                artigo.setDataCriacao(c.getString(c.getColumnIndex(KEY_DATA_CRIACAO)));
                artigos.add(artigo);
            }while(c.moveToFirst());
        }
        return artigos;

    }

    // Atualizar um artigo
    public int atualizarArtigo(Artigo artigo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITUTLO, artigo.getTitulo());
        values.put(KEY_URL, artigo.getUrl());
        values.put(KEY_ESTADO, artigo.getEstado());
        values.put(KEY_DATA_CRIACAO, getDateTime());
        // Atualizar a linha
        return db.update(TABLE_ARTIGO, values, KEY_ID + " = ?", new String[]{String.valueOf(artigo.getId())});
    }

    // Apagar um artigo
    public void removerArtigo(long artigo_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ARTIGO, KEY_ID + " = ?", new String[]{String.valueOf(artigo_id)});
    }

    // Apagar uma tag
    public void removerTag(Tag tag, boolean removerArtigos){
        SQLiteDatabase db = this.getWritableDatabase();
        if (removerArtigos) {
            List<Artigo> todosArtigos = obterArtigos(tag.getNome());
            for (Artigo artigo : todosArtigos) {
                removerArtigo(artigo.getId());
            }
        }
        db.delete(TABLE_TAG, KEY_ID + " = ?", new String[]{String.valueOf(tag.getId())});
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void fecharBD() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}