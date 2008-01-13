package fr.umlv.IRPhoto.album;

import java.util.Comparator;


public class Photo implements Comparable {
	
	

	private double latitude;
	private double longitude;
	private String name;
	private String path;
	private String type;

	public static final Comparator NAME_ORDER = new Comparator() {
	    public int compare(Object o1, Object o2){
	      if(!(o1 instanceof Photo))
	        throw new ClassCastException();
	      return (new Integer(((Photo)o1).getName())).compareTo(
	                        new Integer(((Photo)o2).getName()));
	    }
	  };
	 public static final Comparator TYPE_ORDER = new Comparator() {
	    public int compare(Object o1, Object o2){
	      if(!(o1 instanceof Photo))
	        throw new ClassCastException();
	      return (((Photo)o1).getType()).compareTo(
	                        ((Photo)o2).getType());
	    }
	  };
	
	public double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public String getType() {
		return null;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public int hashCode(){
	    return this.path.hashCode() * this.name.hashCode();
	  }
	@Override
	  public boolean equals(Object o){
	    if(!(o instanceof Photo))
	      return false;
	    Photo p = (Photo)o;
	    if(this.hashCode() == p.hashCode())
	      return true;
	    return false;
	  }
	@Override
	  public int compareTo(Object o){
	    if(!(o instanceof Photo))
	      throw new ClassCastException();
	    Photo p = (Photo)o;
	    int compare;
	    return 1;
	  }

}
