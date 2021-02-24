import java.io.Serializable;
import java.util.LinkedList;
import java.util.ListIterator;

public class RentedCars implements Serializable {
	
	private LinkedList<String> ownerRentedCars = new LinkedList<String>();
	private int ownerCarsNo = 0;
	
	public void addPlateNo(String plateNo) {
		ownerRentedCars.add(plateNo);
		ownerCarsNo++;
	}
	
	public void removePlateNo(String plateNo) {
		if(ownerRentedCars.contains(plateNo)) {
			ownerRentedCars.remove(plateNo);
			ownerCarsNo--;
		}
	}
	
	public int getCarsNo() {
		return this.ownerCarsNo;
	}
	
	public String toString() {
		String output = "";
		ListIterator<String> iterator = (ListIterator<String>) ownerRentedCars.iterator();
		while(iterator.hasNext()) {
			output += iterator.next() + " ";
		}
		return output;
	}

}
