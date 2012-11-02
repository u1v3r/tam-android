package cz.vutbr.fit.testmind;

import java.util.ArrayList;

import cz.vutbr.fit.testmind.profile.TAMPNode;
import cz.vutbr.fit.testmind.profile.TAMProfile;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TestActivity extends FragmentActivity {
    private TAMProfile profile = MainActivity.getProfile();
    private TAMPNode node;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {       
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        
        if(node == null)
        {
            set_node(profile.getRoot());
        }
        loadNode();
    }
    
    public void set_node(TAMPNode node)
    {
        this.node = node;
        loadNode();
    }
    
    // private methods =========================================
    private void loadNode()
    {
        loadPath();
        
        TextView title = (TextView) findViewById(R.id.test_textView_title);
        title.setText(node.getTitle());
        
        loadChilds();
        loadBody();
    }
    
    private void loadPath()
    {
        ArrayList<TAMPNode> path_nodes = new ArrayList<TAMPNode>();
        
        TAMPNode temp_node = node;
        
        while(temp_node.getParent() != null)
        {
            temp_node = temp_node.getParent(); 
            path_nodes.add(0, temp_node);
        }

        LinearLayout pathView = (LinearLayout) findViewById(R.id.test_linearLayout_path);

        TextView rootSep = new TextView(this);
        rootSep.setText("/");
        pathView.addView(rootSep);
        for(TAMPNode parent_node: path_nodes)
        {
            TextView text = new TextView(this);
            text.setText(parent_node.getTitle());
            pathView.addView(text);
            TextView sep = new TextView(this);
            sep.setText("/");
            pathView.addView(sep);
        }      
    }
    
    private void loadBody()
    {
        String body = node.getBody().toString();
        WebView bodyView = (WebView) findViewById(R.id.test_linearLayout_path);
        // load data
        bodyView.loadDataWithBaseURL(null, body, "text/html", "UTF-8", null);        
    }
    
    private void loadChilds()
    {
        
    }
}