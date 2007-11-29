package fr.umlv.IRPhoto;

import java.util.ArrayList;
import java.util.List;

public class PhotoCollection {

	private static final ArrayList<PhotoCollection> collections = new ArrayList<PhotoCollection>();

	private final int id;
	
	private static int counter = 0;

	private String name;

	private PhotoCollection(int id, String name) {

		this.id = id;
		this.name = name;
	}

	public String getName() {

		return this.name;
	}

	@Override
	public boolean equals(Object o) {

		if (o == null) {
			return false;
		}
		if (!(o instanceof PhotoCollection)) {
			return false;
		}

		PhotoCollection pc = (PhotoCollection) o;
		return (this.id == pc.id && this.name.equals(pc.name));

	};

	public static int addCollection(String name) {

		PhotoCollection pc = new PhotoCollection(
				counter++, name);
		PhotoCollection.collections.add(pc);
		return pc.id;
	}

	public static List<PhotoCollection> getCollections() {

		return PhotoCollection.collections;
	}

	public static boolean isAlreadyExistingCollection(PhotoCollection pc) {

		for (PhotoCollection collection : PhotoCollection.getCollections()) {
			if (collection.equals(pc)) {
				return true;
			}
		}
		return false;
	}

	public static void deleteCollection(String name) {
//TODO que faire si index = -1
		int index = indexOfCollection(name);
		System.out.println(index);
		collections.remove(index);
	}

	private static int indexOfCollection(String name) {
//TODO que faire qd le name n'est pas trouve ??
		int index = 0;
		for (PhotoCollection pc : collections) {
			if (pc.name.equals(name)) {
				return index;
			}
			index++;
		}
		return -1;
	}

}
