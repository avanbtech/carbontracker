package com.cmpt276.indigo.carbontracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cmpt276.indigo.carbontracker.carbon_tracker_model.CarbonFootprintComponentCollection;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.JourneyModel;
import com.cmpt276.indigo.carbontracker.carbon_tracker_model.RouteModel;


import java.util.ArrayList;
import java.util.List;

/*
    implments Route selection activity
 */
public class RouteSelectActivity extends AppCompatActivity {

    private long indexOfRouteEditing = -1;
    CarbonFootprintComponentCollection carbonFootprintInterface;
    private static final int ACTIVITY_RESULT_ADD = 40;
    private static final int ACTIVITY_RESULT_EDIT = 60;
    List<Integer> routePositionList;
    ArrayList<RouteModel> routes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_route_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Allows for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
        startAddActivity();
        createListView();
        setupEditRouteLongPress();
    }

    // FAB button to launch add activity
    private void startAddActivity() {
        //set reference to fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RouteSelectActivity.this,RouteAddActivity.class);
                startActivityForResult(intent, ACTIVITY_RESULT_ADD);
            }
        });
    }


    //sample for demonstartion purposes
    private void createListView() {
        //set reference to listview
        ListView routeList = (ListView) findViewById(R.id.route_select_list);
        populateRoutesList();

        //handle click for each element in listview
        routeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = getIntent();
                // Passing selected route to the caller activity
                int realPosition = routePositionList.get(position);
                RouteModel selectedRoute = routes.get(realPosition);
                intent.putExtra("route", selectedRoute);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void populateRoutesList() {
        ListView routeList = (ListView) findViewById(R.id.route_select_list);
        carbonFootprintInterface = CarbonFootprintComponentCollection.getInstance();
        routes = carbonFootprintInterface.getRoutes(this);
        // putting routes in list
        List<String> routeNameList = new ArrayList<>();
        routePositionList = new ArrayList<>();
        //Add elements
        int counter = 0;
        for(RouteModel v: routes){
            if(!v.getIsDeleted()) {
                routeNameList.add(v.getName());
                routePositionList.add(counter);
            }
            counter++;
        }

        //Create array adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this, //context
                android.R.layout.simple_list_item_1,
                routeNameList //arrayList
        );

        //apply adapter ro listview
        routeList.setAdapter(arrayAdapter);
    }

    private void setupEditRouteLongPress() {
        routes = carbonFootprintInterface.getRoutes(this);
        ListView list = (ListView) findViewById(R.id.route_select_list);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                RouteModel route = routes.get(position);
                indexOfRouteEditing = route.getId();
                Intent intent = RouteAddActivity.makeIntentForEditRoute(RouteSelectActivity.this, route);
                startActivityForResult(intent, ACTIVITY_RESULT_EDIT);
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case ACTIVITY_RESULT_ADD:
                    populateRoutesList();
                    break;
                case ACTIVITY_RESULT_EDIT:
                    RouteModel modifiedRoute = (RouteModel) data.getSerializableExtra("route");
                    modifiedRoute.setId(indexOfRouteEditing);
                    carbonFootprintInterface.edit(this, modifiedRoute);
                    populateRoutesList();
                    break;
            }

        }
        else if (resultCode == RouteAddActivity.RESULT_DELETE){
            populateRoutesList();
        }

    }

}
