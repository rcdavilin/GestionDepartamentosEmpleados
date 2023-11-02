package model;

import javax.management.InvalidAttributeValueException;

public class Empleado {
	private Integer id;
	private String nombre;
	private Double salario;
	private Departamento departamento;

	private void setId(int id) {
		this.id = id;
	}

	public Empleado(Integer id, String nombre, Double salario, Departamento departamento)
			throws InvalidAttributeValueException {
		setId(id);
		setNombre(nombre);
		setSalario(salario);
		setDepartamento(departamento);
	}

	public Empleado(String nombre, Double salario, Departamento departamento) throws InvalidAttributeValueException {
		this(0, nombre, salario, departamento);
	}
	
	@Override
	public String toString() {
		if (departamento == null) {
			return "Empleado (id: " + getId() + ", nombre: " + getNombre() + ", salario: " + getSalario()
					+ ", no pertenece a ningun departamento)";
		}
		return "Empleado (id: " + getId() + ", nombre: " + getNombre() + ", salario: " + getSalario()
				+ ", pertenece al departamento " + departamento.getNombre() + " con id: " + departamento.getId() + ")";
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getSalario() {
		return salario;
	}

	public void setSalario(double salario) throws InvalidAttributeValueException {
		if (salario >= 0) {
			this.salario = salario;
		} else {
			throw new InvalidAttributeValueException("El salario no puede ser negativo.\n");
		}

	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public int getId() {
		return id;
	}
}