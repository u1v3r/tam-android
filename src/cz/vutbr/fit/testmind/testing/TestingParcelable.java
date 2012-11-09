package cz.vutbr.fit.testmind.testing;


import java.util.List;

import cz.vutbr.fit.testmind.profile.TAMPNode;

import android.os.Parcel;
import android.os.Parcelable;

public class TestingParcelable implements Parcelable
{
    private String title;
    private String body;
    private int count_childs;
    private TestingParcelable[] childs;
    
    public TestingParcelable(TAMPNode node)
    {
        title = node.getTitle();
        body = node.getBody();
        
        List<TAMPNode> nodeChilds = node.getListOfChildNodes();
        
        count_childs = nodeChilds.size();
        if(count_childs > 0)
        {
            childs = new TestingParcelable[count_childs];
        }
        
        for(int i = 0; i < count_childs; i++)
        {
            childs[i] = new TestingParcelable(nodeChilds.get(i));
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

    // write your object's data to the passed-in Parcel
    public void writeToParcel(Parcel out, int flags)
    {
        out.writeString(title);
        out.writeString(body);
        out.writeInt(count_childs);
        if(count_childs > 0)
        {
            out.writeParcelableArray(childs, flags);
        }
    }

    private void readFromParcel(Parcel in)
    {   
        title = in.readString();
        body = in.readString();
        count_childs = in.readInt();
        if(count_childs > 0)
        {
            childs = (TestingParcelable[])in.readParcelableArray(TestingParcelable.class.getClassLoader());
        }
    }
    
    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<TestingParcelable> CREATOR = new Parcelable.Creator<TestingParcelable>() {
        public TestingParcelable createFromParcel(Parcel in)
        {
            return new TestingParcelable(in);
        }

        public TestingParcelable[] newArray(int size)
        {
            return new TestingParcelable[size];
        }
    };
}