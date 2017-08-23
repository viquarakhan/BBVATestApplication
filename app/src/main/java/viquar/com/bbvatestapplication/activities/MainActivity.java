package viquar.com.bbvatestapplication.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import viquar.com.bbvatestapplication.R;
import viquar.com.bbvatestapplication.fragments.ListFragment;
import viquar.com.bbvatestapplication.fragments.MapFragment;
import viquar.com.bbvatestapplication.model.BankLocation;
import viquar.com.bbvatestapplication.utils.Communicator;

public class MainActivity extends AppCompatActivity implements ListFragment.OnListFragmentInteractionListener,Communicator{
    MapFragment fragment_map;
    ListFragment fragment_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragment_map=new MapFragment();
        fragment_list=new ListFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragment_map,"map_fragment").commit();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i =  item.getItemId();
        if(i==R.id.menu_item_map){
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction ft=fragmentManager.beginTransaction();
            ft.replace(R.id.fragment_container,fragment_map,"map_fragment");
            ft.commit();
           }
        if(i==R.id.menu_item_list){
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction ft=fragmentManager.beginTransaction();
            ft.replace(R.id.fragment_container,fragment_list,"list_fragment");
            ft.commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(int a) {

    }

    @Override
    public void passList(ArrayList<BankLocation> bankLocations) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("listkey",bankLocations);
        fragment_list.setArguments(bundle);
    }
}
