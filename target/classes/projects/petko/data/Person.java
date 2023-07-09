package projects.petko.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


public class Person {

	private int id;
	private String category, group, firstName, lastName, nickname;
	private int yearsOld;
	private LocalDate birthdayDate;
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");


	public Person(int id, String category, String group, String firstName, 
			String lastName,String nickname, LocalDate birthdayDate) {
		this.id = id;
		this.category = category;
		this.group = group;
		this.firstName = firstName;
		this.lastName = lastName;
		this.nickname = nickname;
		this.birthdayDate = birthdayDate;
	}

	public void updateAge() {
		this.yearsOld = Math.toIntExact(java.time.temporal.ChronoUnit.YEARS.between(this.birthdayDate, LocalDate.now()));

	}

	public int getId() {
		return id;
	}

	public String getCategory() {
		return category;
	}

	public String getGroup() {
		return group;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getNickname() {
		return nickname;
	}

	public int getYearsOld() {
		updateAge();
		return yearsOld;
	}

	public LocalDate getBirthdayDate() {
		return birthdayDate;
	}

	public int getDaysTill() {
		int i = this.birthdayDate.getDayOfYear();
		int k = LocalDate.now().getDayOfYear();
		if (i == k) {
			return 0;
		}
		if (i > k) {
			return i - k;
		} else {
			int m = Math.toIntExact(ChronoUnit.DAYS.between(LocalDate.now().withYear(LocalDate.now().getYear()), 
												this.getBirthdayDate().withYear(LocalDate.now().getYear() + 1)));
			return m;
		}
		
		
	}
	
	public String toString() {
		return firstName + ";" + lastName + ";" + nickname + ";" + category + ";" + group + ";" + this.birthdayDate.format(dtf);
	}
	
	public void setCategory(String category) {
		this.category = category;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setBirthdayDate(LocalDate birthdayDate) {
		this.birthdayDate = birthdayDate;
	}
}
