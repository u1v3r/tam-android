package com.example.testmind;

import cz.vutbr.fit.tesmind.graphics.ITAMItem;
import cz.vutbr.fit.tesmind.graphics.ITAMNode;
import cz.vutbr.fit.tesmind.graphics.TAMGraph;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.view.Menu;

public class MainActivity extends Activity {
	
	TAMGraph graph;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        
        graph = new TAMGraph(this);
        graph.setBackgroundColor(Color.WHITE);
        
        ITAMNode node1 = graph.addRoot(ITAMNode.NODE_TYPE_RECTANGLE, 10, 10);
        
        node1.addChild(10, 40);
        
        ITAMNode node2 = graph.addRoot(ITAMNode.NODE_TYPE_RECTANGLE, 60, 60);
        
        node2.addChild(100, 100);
        
        setContentView(graph);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}