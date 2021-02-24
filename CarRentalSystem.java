import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CarRentalSystem  {

	public static final String OWNER_RENTED_CARS_FILENAME = "owner-rented-cars.txt";
	public static final String RENTED_CARS_FILENAME = "rented-cars.txt";
	private static final Scanner sc = new Scanner(System.in);

	private static int nrOfRentedCars = 0;

	private static HashMap<String, String> rentedCars = new HashMap<>(100, 0.5f);
	private static HashMap<String, RentedCars> ownerRentedCars = new HashMap<>(100, 0.75f);
	
	private ArrayList<HashMap> backup = new ArrayList<HashMap>();
	
	public void backup() {
		this.backup.add(this.rentedCars);
		this.backup.add(this.ownerRentedCars);
	}
	
	// add a new (key, value) pair
	private void rentCar(String plateNo, String ownerName) {

		if (rentedCars.containsKey(plateNo)) {
			System.out.println("Eroare: masina cu acest numar de inmatriculare a fost deja inchiriata.");
		} else {
			rentedCars.put(plateNo, ownerName);
			nrOfRentedCars++;
			if (ownerRentedCars.containsKey(ownerName)) {
				ownerRentedCars.get(ownerName).addPlateNo(plateNo);
			} else {
				RentedCars car1 = new RentedCars();
				ownerRentedCars.put(ownerName, car1);
				car1.addPlateNo(plateNo);
			}
		}
	}

	private static String getPlateNo() {

		System.out.println("Introduceti numarul de inmatriculare:");
		try {
			return sc.nextLine();
		} catch (NoSuchElementException e) {
			System.out.println("Introducerea datelor a suferit o eroare!..");
			return null;
		}
	}

	private static String getOwnerName() {

		System.out.println("Introduceti numele proprietarului:");
		try {
			return sc.nextLine();
		} catch (NoSuchElementException e) {
			System.out.println("Introducerea datelor a suferit o eroare!...");
			return null;
		}

	}

	// search for a key in hashtable
	private boolean isCarRent(String plateNo) {

		return rentedCars.containsKey(plateNo);
	}

	// remove an existing (key, value) pair
	private void returnCar(String plateNo) {

		String ownerName = rentedCars.get(plateNo);
		if (rentedCars.remove(plateNo) == null) {
			System.out.println("Aceasta masina nu exista in hashtable.");
		} else {
			System.out.println("Masina a fost stearsa din hashtable.");
			nrOfRentedCars--;
			ownerRentedCars.get(ownerName).removePlateNo(plateNo);
		}
	}

	// get the value associated to a key
	private String getCarRent(String plateNo) {

		if (rentedCars.get(plateNo) == null) {
			System.out.println(" Autovehiculul nu exista in hashtable.");
		}
		return rentedCars.get(plateNo);
	}

	private int totalRented() {

		return nrOfRentedCars;
	}

	private void getCarsNo(String ownerName) {

		System.out.println(ownerRentedCars.get(ownerName).getCarsNo());
	}

	private void getCarsList(String ownerName) {

		System.out.println(ownerRentedCars.get(ownerName).toString());
	}
	
	public void run() throws IOException {

		boolean quit = false;

		while (!quit) {
			System.out.println("Asteapta comanda: (help - Afiseaza lista de comenzi)");
			try {
				String command = sc.nextLine();
				switch (command) {
					case "help":
						printCommandsList();
						break;
					case "add":
						rentCar(getPlateNo(), getOwnerName());
						break;
					case "check":
						System.out.println(isCarRent(getPlateNo()));
						break;
					case "remove":
						returnCar(getPlateNo());
						break;
					case "getOwner":
						System.out.println(getCarRent(getPlateNo()));
						break;
					case "totalRentedCars":
						System.out.println(totalRented());
						break;
					case "getCarsNo":
						getCarsNo(getOwnerName());
						break;
					case "getListOfCars":
						getCarsList(getOwnerName());
						break;
					case "quit":
						System.out.println("Aplicatia se inchide...");
						quit = true;
						break;
					case "backup":
						backup();
						writeToBinaryFile(this.backup);
//						writeRentedCarsToFile(rentedCars, RENTED_CARS_FILENAME);
//						writeOwnerRentedCarsToFile(ownerRentedCars, OWNER_RENTED_CARS_FILENAME);
						System.out.println("Datele curente au fost salvate");
						break;
					default:
						System.out.println("Unknown command. Choose from:");
						printCommandsList();
				}
			} catch (NoSuchElementException e) {
				System.out.println("Introducerea datelor a survenit o eroare!..");
				break;
			}
		}
	}

	private static void printCommandsList() {

		System.out.println("help            - Afiseaza aceasta lista de comenzi");
		System.out.println("add             - Adauga o noua pereche (masina, sofer)");
		System.out.println("check           - Verifica daca o masina este deja luata");
		System.out.println("remove          - Sterge o masina existenta din hashtable");
		System.out.println("getOwner        - Afiseaza proprietarul curent al masinii");
		System.out.println("totalRentedCars - Afiseara toate masinile care sunt inchiriate");
		System.out.println("backup          - Salveaza datele introduse");
		System.out.println("quit            - Inchide aplicatia");
	}
	
	public static void writeToBinaryFile(ArrayList<HashMap> data) throws IOException {
		try(ObjectOutputStream binaryFileOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("carRentedSystem.txt")))) {
			binaryFileOut.writeObject(data);
		}
	}
//	private void writeRentedCarsToFile(Map<String, String> rentedCars, String filename) {
//		
//		try {
//			FileOutputStream fos = new FileOutputStream(filename);
//			ObjectOutputStream oos = new ObjectOutputStream(fos);
//
//			oos.writeObject(rentedCars);
//
//			oos.flush();
//			oos.close();
//			fos.close();
//		} catch (IOException e) {
//			System.out.println("nu s-au putut salva datele");
//		}
//	}
//	
//	private void writeOwnerRentedCarsToFile(Map<String, RentedCars> ownerRentedCars, String filename) {
//		
//		try {
//			FileOutputStream fos = new FileOutputStream(filename);
//			ObjectOutputStream oos = new ObjectOutputStream(fos);
//
//			oos.writeObject(ownerRentedCars);
//
//			oos.flush();
//			oos.close();
//			fos.close();
//		} catch (IOException e) {
//			System.out.println("nu s-au putut salva datele");
//		}
//	}
//	
	public static void main(String[] args) throws IOException {
		 
	    // create and run an instance (for test purpose)
	    new CarRentalSystem().run();
	 
	  }
}