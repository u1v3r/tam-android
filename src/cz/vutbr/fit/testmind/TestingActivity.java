package cz.vutbr.fit.testmind;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import cz.vutbr.fit.testmind.MainActivity.EventObjects;
import cz.vutbr.fit.testmind.layout.FlowLayout;
import cz.vutbr.fit.testmind.profile.TAMPConnection;
import cz.vutbr.fit.testmind.profile.TAMPNode;
import cz.vutbr.fit.testmind.testing.TestingNode;
import cz.vutbr.fit.testmind.testing.TestingParcelable;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;

/**
 * activity for testing from mind maps
 * @author jules
 *
 */
public class TestingActivity extends FragmentActivity
{
    /**
     * class for serialization of state
     */
    public static class TestingActivityData implements Serializable
    {
        private static final long serialVersionUID = 1L;
        public TestingNode rootNode;
        public TestingNode node;
        public ActivityMode mode;
        public TestingPhase testingPhase;
        public ArrayList<TestingNode> testingNodes;
        public int currentIndex;

        public TestingActivityData(ActivityMode mode, TestingPhase testingPhase)
        {
            this.mode = mode;
            this.testingPhase = testingPhase;
        }
    }

    private static final float PATH_TEXT_SIZE = 20;
    private static final float CHILD_TEXT_SIZE = 16;
    private static final String DATA_STATE = "data";
    
    private enum ActivityMode {TEST, EXPLORE};
    private enum TestingPhase {QUESTION, ANSWER, END};

    private TestingActivityData data = new TestingActivityData(ActivityMode.TEST, TestingPhase.QUESTION);
    private HashMap<Button, TestingNode> buttonsNodes = new HashMap<Button, TestingNode>();
    private HashMap<TextView, TestingNode> textViewsNodes = new HashMap<TextView, TestingNode>();
    
    private MenuItem controlAction;
    private MenuItem menuMode;
    private MenuItem menuOrder;
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
        
        this.getActionBar().setHomeButtonEnabled(true);
        
        Bundle b = getIntent().getExtras();
        TestingParcelable nodeParcelable = (TestingParcelable)b.getParcelable("cz.vutbr.fit.testmind.testing.TestingParcelable");
        
        // init class
        pathView = (FlowLayout) findViewById(R.id.testing_flowLayout_path);
        childsView = (FlowLayout) findViewById(R.id.testing_flowLayout_childs);
        bodyView = (WebView) findViewById(R.id.testing_webView_body);        
        
        data.rootNode = nodeParcelable.getTestingNode();
        data.testingNodes = data.rootNode.getListTestingNodes();
        
        startTesting();
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_testing, menu);

        controlAction = menu.findItem(R.id.testingActionControl);
        menuMode = menu.findItem(R.id.menu_item_mode);
        menuOrder = menu.findItem(R.id.menu_item_order);

        setActivityMode(data.mode);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        
        int id = item.getItemId();
        
        if(controlAction.getItemId() == id)
        {
            if(data.testingPhase == TestingPhase.ANSWER)
            {
                setNextNode();
            }
            else if(data.testingPhase == TestingPhase.QUESTION)
            {
                showAnswer();
            }
            else if(data.testingPhase == TestingPhase.END)
            {
                startTesting();
                setTestingPhase(TestingPhase.QUESTION);
            }
        }
        else if(android.R.id.home == id)
        {
            finish();
        }
        else if(menuMode.getItemId() == id)
        {
            ActivityMode nextMode = data.mode == ActivityMode.TEST 
                    ? ActivityMode.EXPLORE : ActivityMode.TEST;
            setActivityMode(nextMode);
        }
        else if(menuOrder.getItemId() == id)
        {
            changeOrder();
        }
        
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        
        outState.putSerializable(DATA_STATE, (Serializable)data);
        
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {   
        super.onRestoreInstanceState(savedInstanceState);
        
        data = (TestingActivityData) savedInstanceState.getSerializable(DATA_STATE);
        // Android 4.2 call onCreateOptionsMenu after onRestoreInstanceState
        if(menuMode != null)
        {
            setActivityMode(data.mode);
        }
    }
    
    /**
     * handler for buttons
     */
    View.OnClickListener exploreHandler = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            if(data.mode == ActivityMode.EXPLORE)
            {
                TestingNode nextNode = null;
                if(Button.class.isInstance(v))
                {
                    Button item = (Button) v;
                    nextNode = buttonsNodes.get(item);
                }
                else if(TextView.class.isInstance(v))
                {
                    TextView item = (TextView) v;
                    nextNode = textViewsNodes.get(item);
                }
                
                if(nextNode != null)
                {
                    setNode(nextNode);
                }
            }
        }
    };
    
    // private methods =========================================
    
    /**
     * prepare for testing
     */
    private void startTesting()
    {
        data.currentIndex = 0;
        if(data.testingNodes.size() > 0)
        {
            setNode(data.testingNodes.get(data.currentIndex));
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
        this.data.node = node;
        
        if(data.mode == ActivityMode.TEST)
        {
            getActionBar().setTitle(String.format("%s %d/%d", getString(R.string.app_name),
                    data.currentIndex+1, data.testingNodes.size()));
        }
        else
        {
            getActionBar().setTitle(R.string.app_name);
        }
        
        loadNode();
    }
    
    /**
     * set next node through testing
     */
    private void setNextNode()
    {
        if(data.currentIndex+1 < data.testingNodes.size())
        {
            setTestingPhase(TestingPhase.QUESTION);
            data.currentIndex++;
            setNode(data.testingNodes.get(data.currentIndex));
        }
    }
    
    /**
     * load node
     */
    private void loadNode()
    {
        loadPath();
        
        TextView title = (TextView) findViewById(R.id.testing_textView_title);
        title.setText(data.node.getTitle());
        
        loadBody();
        loadChilds();
    }
    
    /**
     * load path to root
     */
    private void loadPath()
    {
        pathView.removeAllViews();
        textViewsNodes.clear();

        // generate parents
        ArrayList<TestingNode> path_nodes = new ArrayList<TestingNode>();
        
        TestingNode temp_node = data.node;
        
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
        for(TestingNode parentNode: path_nodes)
        {
            TextView text = new TextView(this);
            text.setText(String.format(" %s /", parentNode.getTitle()));
            text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, PATH_TEXT_SIZE);
            text.setTextColor(foreground_color);
            pathView.addView(text);
            textViewsNodes.put(text, parentNode);
            text.setOnClickListener(exploreHandler);
        }      
    }
    
    /**
     * load html body
     */
    private void loadBody()
    {
        bodyView.loadData("", "text/html; charset=UTF-8", null);
        bodyView.setVisibility(View.GONE);
        bodyView.clearView();

        if(data.mode == ActivityMode.EXPLORE)
        {
            bodyView.setVisibility(View.VISIBLE);
        }
        
        // load data
        bodyView.loadData(data.node.getBody(), "text/html; charset=UTF-8", null);
    }
    
    /**
     * load childs of node
     */
    private void loadChilds()
    {
        childsView.removeAllViews();
        buttonsNodes.clear();
        
        if(data.mode == ActivityMode.TEST)
        {
            childsView.setVisibility(View.GONE);
        }
        else if(data.mode == ActivityMode.EXPLORE)
        {
            childsView.setVisibility(View.VISIBLE);
        }

        // append childs
        for(TestingNode childNode: data.node.getChilds())
        {
            Button childButton = new Button(this);
            
            childButton.setBackgroundColor(getResources().getColor(R.color.node_background_1));
            childButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, CHILD_TEXT_SIZE);
            childButton.setTextColor(getResources().getColor(R.color.white));
            childButton.setText(childNode.getTitle());
            childsView.addView(childButton);
            buttonsNodes.put(childButton, childNode);
            childButton.setOnClickListener(exploreHandler);
        }        
    }
    
    /**
     * show body and childs
     */
    private void showAnswer()
    {
        bodyView.setVisibility(View.VISIBLE);
        childsView.setVisibility(View.VISIBLE);
        
        if(data.testingNodes.size() == data.currentIndex+1)
        {
            Toast.makeText(this, R.string.testing_last_node, Toast.LENGTH_LONG).show();
            setTestingPhase(TestingPhase.END);
        }
        else
        {
            setTestingPhase(TestingPhase.ANSWER);
        }
    }
    
    /**
     * set cuurent testing phase
     * @param phase
     */
    private void setTestingPhase(TestingPhase phase)
    {
        data.testingPhase = phase;
        
        // title of action
        if(phase == TestingPhase.QUESTION)
        {
            controlAction.setTitle(R.string.testing_control_show_answer);
        }
        else if(phase == TestingPhase.ANSWER)
        {
            controlAction.setTitle(R.string.testing_control_next_question);
        }
        else if(phase == TestingPhase.END)
        {
            controlAction.setTitle(R.string.testing_control_again);
        }
    }

    /**
     * set mode of activity
     * @param mode
     */
    private void setActivityMode(ActivityMode mode)
    {
        data.mode = mode;

        if(mode == ActivityMode.EXPLORE)
        {
            menuMode.setTitle(R.string.testing_menu_test);
            menuOrder.setVisible(false);
            controlAction.setVisible(false);
            setNode(data.node);
        }
        else if(mode == ActivityMode.TEST)
        {
            menuMode.setTitle(R.string.testing_menu_explore);
            menuOrder.setVisible(true);
            controlAction.setVisible(true);
            setTestingPhase(data.testingPhase);
            setNode(data.testingNodes.get(data.currentIndex));
            if(data.testingPhase == TestingPhase.ANSWER)
            {
                showAnswer();
            }
        }
    }
    
    /**
     * change order of testing
     */
    private void changeOrder()
    {
        if(menuOrder.getTitle().equals(getString(R.string.testing_menu_random)))
        {
            Collections.shuffle(data.testingNodes);
            menuOrder.setTitle(R.string.testing_menu_sequentialy);
        }
        else if(menuOrder.getTitle().equals(getString(R.string.testing_menu_sequentialy)))
        {
            data.testingNodes = data.rootNode.getListTestingNodes();
            menuOrder.setTitle(R.string.testing_menu_random);
        }
        
        setTestingPhase(TestingPhase.QUESTION);
        startTesting();
    }
}