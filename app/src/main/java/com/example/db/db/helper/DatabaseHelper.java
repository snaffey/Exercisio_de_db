package com.example.aplicacaoabilio.bd.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.db.db.model.Artigo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private static final String CREATE_TABLE_ARTIGO_TAG = "CREATE TABLE "+TABLE_ARTIGO_TAG+" ( "+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_ARTIGO_ID+" INTEGER, "+KEY_ARTIGO_ID+" INTEGER)";

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
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}