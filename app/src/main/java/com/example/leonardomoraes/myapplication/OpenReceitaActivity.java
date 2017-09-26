package com.example.leonardomoraes.myapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class OpenReceitaActivity extends MainActivity {

    private TextView nome, ingredientes, tempo, tipo, preparo;
    private int posTipo;
    private String idProprio;
    private Uri imgUri;
    private ProgressDialog progress;
    private ImageView img;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    protected DrawerLayout mDrawerLayout;
    private TextView profile;
    private static String TAG = MainActivity.class.getSimpleName();

    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_receita);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Pesquisar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        //View contentView = inflater.inflate(R.layout.activity_open_receita, null, false);
        //mDrawerLayout.addView(contentView, 0);

        Button version = (Button) findViewById(R.id.version);

        version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OpenReceitaActivity.this, VersionamentoActivity.class);
                intent.putExtra("nome", nome.getText());
                intent.putExtra("ingred", ingredientes.getText());
                intent.putExtra("tempo", tempo.getText());
                intent.putExtra("tipo", tipo.getText());
                intent.putExtra("tipoID", posTipo);
                intent.putExtra("preparo", preparo.getText());
                intent.putExtra("idPai", idProprio);
                intent.putExtra("img", imgUri);
                startActivity(intent);
            }
        });

        nome = (TextView) findViewById(R.id.tv_nomeReceita_Act_openReceita);
        ingredientes = (TextView) findViewById(R.id.tv_ingredientesReceita_Act_openReceita);
        tempo = (TextView) findViewById(R.id.tv_tempoReceita_Act_openReceita);
        tipo = (TextView) findViewById(R.id.tv_tipoReceita_Act_openReceita);
        preparo = (TextView) findViewById(R.id.tv_preparoReceita_Act_openReceita);
        img = (ImageView) findViewById(R.id.imageView_Act_openReceita);
        progress = new ProgressDialog(OpenReceitaActivity.this);



        Intent i = getIntent();
        String nome_string = i.getExtras().getString("nome");
        String ingredientes_string = i.getExtras().getString("ingredientes");
        String tempo_string = i.getExtras().getString("tempo");
        String tipo_string = i.getExtras().getString("tipo");
        String preparo_string = i.getExtras().getString("preparo");

        idProprio = i.getExtras().getString("idProprio");
        posTipo = i.getExtras().getInt("tipoID");

        String image = i.getExtras().getString("uri");
        if(image!= null) {
            imgUri = Uri.parse(image);
        }
        else{
            Toast.makeText(this, "Receita sem imagem", Toast.LENGTH_SHORT).show();
        }

        nome.setText(nome_string);
        ingredientes.setText(ingredientes_string);
        tempo.setText(tempo_string);
        tipo.setText(tipo_string);
        preparo.setText(preparo_string);

        Glide.with(OpenReceitaActivity.this).load(imgUri).placeholder(R.drawable.ic_file_download_black_24dp).into(img);


        //MENU
        mNavItems.add(new NavItem("Feed de receitas", "Onde estão toas as receitas", R.drawable.ic_home));
        mNavItems.add(new NavItem("Preferências", "Altere suas preferências", R.drawable.ic_action_settings));
        mNavItems.add(new NavItem("Sobre", "Conheça os desenvolvedores", R.drawable.ic_action_about));
        mNavItems.add(new NavItem("Sair", "Sair do seu perfil", R.drawable.ic_close));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d(TAG, "onDrawerClosed: " + getTitle());

                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        Intent intent = new Intent (this, SearchActivity.class);
        intent.putExtra("query", query);
        startActivity(intent);

        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }

    private void selectItemFromDrawer(int position) {
        Fragment fragment = new PreferencesFragment();

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.openContent, fragment)
                .commit();

        if(position == 3){
            auth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
        }

        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).mTitle);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("position", position);
        editor.commit();

        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }
}


