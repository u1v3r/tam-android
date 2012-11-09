package cz.vutbr.fit.testmind;

import java.util.ArrayList;

import cz.vutbr.fit.testmind.testing.TestingNode;
import cz.vutbr.fit.testmind.testing.TestingParcelable;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TestActivity extends FragmentActivity {
    private TestingNode node;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {       
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        
        Bundle b = getIntent().getExtras();
        TestingParcelable nodeParcelable = (TestingParcelable)b.getParcelable("cz.vutbr.fit.testmind.testing.TestingParcelable");
    }
    
    public void set_node(TestingNode node)
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
        ArrayList<TestingNode> path_nodes = new ArrayList<TestingNode>();
        
        TestingNode temp_node = node;
        
        while(temp_node.getParent() != null)
        {
            temp_node = temp_node.getParent(); 
            path_nodes.add(0, temp_node);
        }

        LinearLayout pathView = (LinearLayout) findViewById(R.id.test_linearLayout_path);

        TextView rootSep = new TextView(this);
        rootSep.setText("/");
        pathView.addView(rootSep);
        for(TestingNode parent_node: path_nodes)
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
        String body = node.getBody();
        WebView bodyView = (WebView) findViewById(R.id.test_linearLayout_path);
        // load data
        bodyView.loadDataWithBaseURL(null, body, "text/html", "UTF-8", null);        
    }
    
    private void loadChilds()
    {
        
    }
}