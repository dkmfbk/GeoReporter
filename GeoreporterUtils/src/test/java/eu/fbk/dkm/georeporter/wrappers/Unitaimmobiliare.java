package eu.fbk.dkm.georeporter.wrappers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class Unitaimmobiliare {

	private String name;
	private int age;
	private String position;
	private BigDecimal salary;
	private Map listaattributi;
	private Nota notainiziale;
	private Nota notafinale;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public BigDecimal getSalary() {
		return salary;
	}
	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}
	public Map getListaattributi() {
		return listaattributi;
	}
	public void setListaattributi(Map listaattributi) {
		this.listaattributi = listaattributi;
	}
	public Nota getNotainiziale() {
		return notainiziale;
	}
	public void setNotainiziale(Nota notainiziale) {
		this.notainiziale = notainiziale;
	}
	public Nota getNotafinale() {
		return notafinale;
	}
	public void setNotafinale(Nota notafinale) {
		this.notafinale = notafinale;
	}
	
	
	
}