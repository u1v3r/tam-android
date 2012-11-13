package cz.vutbr.fit.testmind;

import java.util.ArrayList;
import java.util.Collections;

import cz.vutbr.fit.testmind.layout.FlowLayout;
import cz.vutbr.fit.testmind.testing.TestingNode;
import cz.vutbr.fit.testmind.testing.TestingParcelable;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

/**
 * activity for testing from mind maps
 * @author jules
 *
 */
public class TestingActivity extends FragmentActivity {
    static final private String HTML = "<html><head></head><body>%s</body></html>";
    static final private float PATH_TEXT_SIZE = 20;
    static final private float CHILD_TEXT_SIZE = 16;
    
    private enum ActivityMode {TEST, EXPLORE};

    private TestingNode node;
    private ActivityMode mode = ActivityMode.TEST;
    private ArrayList<TestingNode> testingNodes;
    private int currentIndex;
    
    private MenuItem controlAction;
    private MenuItem menuExplore;
    private MenuItem menuTest;
    private FlowLayout pathView;
    private FlowLayout childsView;
    private WebView bodyView;
    
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
        
        // init class
        pathView = (FlowLayout) findViewById(R.id.testing_flowLayout_path);
        childsView = (FlowLayout) findViewById(R.id.testing_flowLayout_childs);
        bodyView = (WebView) findViewById(R.id.testing_webView_body);        
        
        TestingNode root = nodeParcelable.getTestingNode();
        testingNodes = root.getListTestingNodes();
        
        startTesting();
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_testing, menu);
        
        controlAction = menu.findItem(R.id.testingActionControl).setVisible(true);
        menuExplore = menu.findItem(R.id.explore);
        menuTest = menu.findItem(R.id.test);
        
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        
        int id = item.getItemId();
        
        if(controlAction.getItemId() == id)
        {
            if(controlAction.getTitle() == getString(R.string.testing_control_next_question))
            {
                setNextNode();
            }
            else if(controlAction.getTitle() == getString(R.string.testing_control_show_answer))
            {
                showAnswer();
            }
        }
        else if(menuExplore.getItemId() == id && mode == ActivityMode.TEST)
        {
            mode = ActivityMode.EXPLORE;
            menuExplore.setChecked(true);
            menuTest.setChecked(false);
            controlAction.setVisible(false);
            setNode(node);
        }
        else if(menuTest.getItemId() == id && mode == ActivityMode.EXPLORE)
        {
            mode = ActivityMode.TEST;
            menuExplore.setChecked(false);
            menuTest.setChecked(true);
            controlAction.setVisible(true);
            startTesting();
        }
        
        return true;
    }
    
    // private methods =========================================
    
    /**
     * prepare for testing
     */
    private void startTesting()
    {
        Collections.shuffle(testingNodes);
        currentIndex = 0;
        if(testingNodes.size() > 0)
        {
            setNode(testingNodes.get(currentIndex));
        }
        else
        {
            Toast.makeText(this, R.string.testing_error_any_nodes, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /**
     * set and load node
     * @param node
     */
    private void setNode(TestingNode node)
    {
        this.node = node;
        loadNode();
    }
    
    /**
     * set next node through testing
     */
    private void setNextNode()
    {
        if(currentIndex+1 < testingNodes.size())
        {
            controlAction.setTitle(R.string.testing_control_show_answer);
            currentIndex++;
            setNode(testingNodes.get(currentIndex));
        }
        else
        {
            Toast.makeText(this, R.string.testing_last_node, Toast.LENGTH_LONG).show();
        }
    }
    
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
        pathView.removeAllViews();

        // generate parents
        ArrayList<TestingNode> path_nodes = new ArrayList<TestingNode>();
        
        TestingNode temp_node = node;
        
        while(temp_node.getParent() != null)
        {
            temp_node = temp_node.getParent(); 
            path_nodes.add(0, temp_node);
        }


        int foreground_color = getResources().getColor(R.color.testing_foreground_path);
        
        TextView rootSep = new TextView(this);
        rootSep.setText(" /");
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
        bodyView.loadData("", "text/html; charset=UTF-8", null);
        if(mode == ActivityMode.TEST)
        {
            bodyView.setVisibility(View.GONE);
        }
        else if(mode == ActivityMode.EXPLORE)
        {
            bodyView.setVisibility(View.VISIBLE);
        }
        
        // load data
        bodyView.loadData(String.format(HTML, node.getBody()), "text/html; charset=UTF-8", null);
    }
    
    /**
     * load childs of node
     */
    private void loadChilds()
    {
        childsView.removeAllViews();
        
        if(mode == ActivityMode.TEST)
        {
            childsView.setVisibility(View.GONE);
        }
        else if(mode == ActivityMode.EXPLORE)
        {
            childsView.setVisibility(View.VISIBLE);
        }

        // append childs
        for(TestingNode childNode: node.getChilds())
        {
            Button childButton = new Button(this);
            childButton.setTextSize(CHILD_TEXT_SIZE);
            childButton.setText(childNode.getTitle());
            
            childsView.addView(childButton);
        }        
    }
    
    /**
     * show body and childs
     */
    private void showAnswer()
    {
        bodyView.setVisibility(View.VISIBLE);
        childsView.setVisibility(View.VISIBLE);
        controlAction.setTitle(R.string.testing_control_next_question);
    }
}