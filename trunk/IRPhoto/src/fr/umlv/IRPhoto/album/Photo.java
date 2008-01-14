package fr.umlv.IRPhoto.album;

import java.util.Comparator;

public class Photo {

	private double latitude;
	private double longitude;
	private String name;
	private String path;
	private String type;
	// TODO date de modif

	public static final Comparator<Photo> DATE_ORDER = new Comparator<Photo>() {

		@Override
		public int compare(Photo o1, Photo o2) {
			// TODO Auto-generated method stub
			return 0;
		}

	};

	public static final Comparator<Photo> NAME_ORDER = new Comparator<Photo>() {

		@Override
		public int compare(Photo o1, Photo o2) {
			return (o1.getName().compareTo(o2.getName()));
		}

	};

	public static final Comparator<Photo> TYPE_ORDER = new Comparator<Photo>() {

		@Override
		public int compare(Photo o1, Photo o2) {
			return (o1.getType().compareTo(o2.getType()));
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
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		return this.path.hashCode() * this.name.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Photo))
			return false;
		Photo p = (Photo) o;
		if (this.hashCode() == p.hashCode())
			return true;
		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Photo ").append("(name=").append(this.name).append(") ")
				.append("(latidude=").append(this.latitude).append(
						", longitude=").append(this.longitude).append(")");
		return sb.toString();
	}

}
