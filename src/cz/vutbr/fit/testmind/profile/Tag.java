package cz.vutbr.fit.testmind.profile;

import java.io.Serializable;

public class Tag implements Serializable{

	private static final long serialVersionUID = -8427152982308657235L;
	
	private String tag;		
	private int start;
	private int end;
		
	public Tag(String tag) {
		this(tag, 0, 0);
	}
	
	public Tag(String tag, int start, int end) {
		this.tag = tag;
		this.start = start;
		this.end = end;
	}
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	
	/**
	 * Porovnava len na zaklade mena
	 */
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		
		if( o instanceof Tag == false){ 
			return false;
		}
		
		Tag tagEqual = (Tag)o;
		
		if(this.tag.equals(tagEqual.tag)){
			return true;
		}
		
		/*
		if(this.tag.equals(tagEqual.tag) && this.start == tagEqual.start && this.end == tagEqual.end){
			return true;
		}
		*/
		return false;
	}

	@Override
	public String toString() {
		return "Tag [tag=" + tag + ", start=" + start + ", end=" + end + "]";
	}
}