package cz.vutbr.fit.testmind;

import java.util.ArrayList;

import cz.vutbr.fit.testmind.MainActivity.EventObjects;
import cz.vutbr.fit.testmind.MainActivity.MenuItems;
import cz.vutbr.fit.testmind.testing.TestingNode;
import cz.vutbr.fit.testmind.testing.TestingParcelable;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * activity for testing from mind maps
 * @author jules
 *
 */
public class TestingActivity extends FragmentActivity {
    static final private String HTML_PRE = "<html><head></head><body><pre>%s</pre></body></html>";
    static final private float PATH_TEXT_SIZE = 20;
    static final private float CHILDS_TEXT_SIZE = 20;
    
    private enum ActivityMode {TEST, EXPLORE};

    private TestingNode node;
    private ActivityMode mode = ActivityMode.TEST;
    
    /**
     *  create activity 
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {       
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        
        Bundle b = getIntent().getExtras();
        TestingParcelable nodeParcelable = (TestingParcelable)b.getParcelable("cz.vutbr.fit.testmind.testing.TestingParcelable");
        
        set_node(nodeParcelable.getTestingNode());
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_testing, menu);    
        
        return true;
    }
    
    /**
     * set and load node
     * @param node
     */
    public void set_node(TestingNode node)
    {
        this.node = node;
        loadNode();
    }
    
    // private methods =========================================
    /**
     * load node
     */
    private void loadNode()
    {
        loadPath();
        
        TextView title = (TextView) findViewById(R.id.testing_textView_title);
        title.setText(node.getTitle());
        
        loadBody();
        loadChilds();
    }
    
    /**
     * load path to root
     */
    private void loadPath()
    {
        ArrayList<TestingNode> path_nodes = new ArrayList<TestingNode>();
        
        TestingNode temp_node = node;
        
        while(temp_node.getParent() != null)
        {
            temp_node = temp_node.getParent(); 
            path_nodes.add(0, temp_node);
        }

        LinearLayout pathView = (LinearLayout) findViewById(R.id.testing_linearLayout_path);

        int foreground_color = getResources().getColor(R.color.testing_foreground_path);
        
        TextView rootSep = new TextView(this);
        rootSep.setText("/");
        rootSep.setTextSize(TypedValue.COMPLEX_UNIT_DIP, PATH_TEXT_SIZE);
        rootSep.setTextColor(foreground_color);
        pathView.addView(rootSep);
        for(TestingNode parent_node: path_nodes)
        {
            TextView text = new TextView(this);
            text.setText(String.format(" %s /", parent_node.getTitle()));
            text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, PATH_TEXT_SIZE);
            text.setTextColor(foreground_color);
            pathView.addView(text);
        }      
    }
    
    /**
     * load html body
     */
    private void loadBody()
    {
        String body = node.getBody();
        WebView bodyView = (WebView) findViewById(R.id.testing_webView_body);
        // load data
        bodyView.loadDataWithBaseURL(null, String.format(HTML_PRE, body), "text/html", "UTF-8", null);        
    }
    
    /**
     * load childs of node
     */
    private void loadChilds()
    {
        LinearLayout childView = (LinearLayout) findViewById(R.id.testing_linearLayout_childs);

        for(TestingNode childNode: node.getChilds())
        {
            Button childButton = new Button(this);
            childButton.setText(childNode.getTitle());
            
            childView.addView(childButton);
        }        
    }
}