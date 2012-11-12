package cz.vutbr.fit.testmind.testing;


import java.util.ArrayList;
import java.util.List;

import cz.vutbr.fit.testmind.profile.TAMPNode;

import android.os.Parcel;
import android.os.Parcelable;

public class TestingParcelable implements Parcelable
{
    private String title;
    private String body;
    private List<TestingParcelable> childs;
    
    public TestingParcelable(TAMPNode node)
    {
        title = node.getTitle();
        body = node.getBody();
        
        List<TAMPNode> nodeChilds = node.getListOfChildNodes();
        
        childs = new ArrayList<TestingParcelable>();
        
        for(TAMPNode childNode: nodeChilds)
        {
            childs.add(new TestingParcelable(childNode));
        }
    }
    
    public TestingParcelable(Parcel in)
    {
        readFromParcel(in);
    }
    
    public int describeContents()
    {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags)
    {
        out.writeString(title);
        out.writeString(body);
        out.writeList(childs);
    }

    private void readFromParcel(Parcel in)
    {   
        title = in.readString();
        body = in.readString();
        childs = new ArrayList<TestingParcelable>();
        in.readList(childs, TestingParcelable.class.getClassLoader());
    }
    
    public static final Parcelable.Creator<TestingParcelable> CREATOR = new Parcelable.Creator<TestingParcelable>()
    {
        public TestingParcelable createFromParcel(Parcel in)
        {
            return new TestingParcelable(in);
        }

        public TestingParcelable[] newArray(int size)
        {
            return new TestingParcelable[size];
        }
    };
    
    public TestingNode getTestingNode()
    {
        TestingNode result = new TestingNode(title, body);
        
        for(TestingParcelable tParcelable: childs)
        {
            result.appendChild(tParcelable.getTestingNode());
        }
        
        return result;
    }
}