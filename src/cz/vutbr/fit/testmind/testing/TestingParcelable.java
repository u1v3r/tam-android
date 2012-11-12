package cz.vutbr.fit.testmind.testing;


import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.TextUtils;
import cz.vutbr.fit.testmind.profile.TAMPNode;

public class TestingParcelable implements Parcelable
{
    private String title;
    private SpannableString body;// TREBA OTESTOVAT CI FUNGUJE SPRAVNE so SpannableString
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

    // write your object's data to the passed-in Parcel
    public void writeToParcel(Parcel out, int flags)
    {
        out.writeString(title);
        out.writeByteArray(body.toString().getBytes());        
        out.writeList(childs);
    }

    private void readFromParcel(Parcel in)
    {   
    	byte[] bodyByte = null;
        title = in.readString();
        in.readByteArray(bodyByte);
        body = new SpannableString(new String(bodyByte,Charset.forName("UTF-8")));
        childs = new ArrayList<TestingParcelable>();
        in.readList(childs, TestingParcelable.class.getClassLoader());
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