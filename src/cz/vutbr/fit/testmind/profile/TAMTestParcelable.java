package cz.vutbr.fit.testmind.profile;

import android.os.Parcel;
import android.os.Parcelable;

public class TAMTestParcelable implements Parcelable
{
    public TAMTestParcelable(Parcel in)
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
        
    }

    private void readFromParcel(Parcel in)
    {   

    }
    
    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<TAMTestParcelable> CREATOR = new Parcelable.Creator<TAMTestParcelable>() {
        public TAMTestParcelable createFromParcel(Parcel in)
        {
            return new TAMTestParcelable(in);
        }

        public TAMTestParcelable[] newArray(int size)
        {
            return new TAMTestParcelable[size];
        }
    };
}