package view;

import dao.Gestion;
import io.IO;
import model.Departamento;
import model.Empleado;
import java.util.InputMismatchException;
import javax.management.InvalidAttributeValueException;

/**
 * Alvaro Márquez, Achraf Boujaanan, David Martin
 */

public class Main {
	public static void main(String[] args) {
		
		Gestion g = new Gestion();
		int opc = 0;
		int opc2 = 0;
		do {
			try {
				menuPrincipal();
				opc = IO.readInt();
				switch (opc) {
				case 0:
					closeBD(g);
					break;
				case 1:
					do {
						menuEmpleado();
						opc2 = IO.readInt();
						switch (opc2) {
						case 1:
							addEmpleado(g);
							break;
						case 2:
							deleteEmpleado(g);
							break;
						case 3:
							modifyEmpleado(g);
							break;
						case 4:
							IO.println(g.showEmpleados());
							break;
						default:
							break;
						}
					} while (opc2 != 0);
					break;
				case 2:
					do {
						menuDepartamento();
						opc2 = IO.readInt();
						switch (opc2) {
						case 1:
							addDepartamento(g);
							break;
						case 2:
							deleteDepartamento(g);
							break;
						case 3:
							modifyDepartamento(g);
							break;
						case 4:
							IO.println(g.showDepartamentos());
							break;
						}
					} while (opc2 != 0);
					break;

				default:
					break;
				}
			} catch (InputMismatchException e) {
				IO.println("Sólo se permiten números");
			}

		} while (opc != 0);
	}

	/**
	 * Metodo de añadir departamento
	 * @param bd
	 */
	private static void addDepartamento(Gestion bd) {
		String nombre;
		IO.println("Introduce el nombre");
		nombre = IO.readString();
		IO.println(bd.addDepartamento(new Departamento(nombre, null)) ? "Añadido" : "No se ha añadido");
	}

	/**
	 * Metodo para añadir empleado
	 * @param bd
	 */
	private static void addEmpleado(Gestion bd) {
		Double salario;
		String nombre;
		IO.println("Introduce el nombre");
		nombre = IO.readString();
		IO.println("Introduce el salario");
		salario = IO.readDouble();
		try {
			IO.println(bd.addEmpleado(new Empleado(nombre, salario, null)) ? "Añadido" : "No se ha añadido");
		} catch (InvalidAttributeValueException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo para modificar el departamento
	 * @param bd
	 */
	private static void modifyDepartamento(Gestion bd) {
		Integer id, idJefe;
		String nombre;
		
		IO.println(bd.showDepartamentos());
		IO.println("Que departamento quieres modificar, ponga su id: ");
		id = IO.readInt();
		IO.println("Introduce el nuevo nombre del departamento: ");
		nombre = IO.readString();
		
		IO.println(bd.showEmpleados());
		IO.println("Introduce el nuevo jefe por id: ");
		idJefe = IO.readInt();
		IO.println(bd.modificarDepartamento(new Departamento(id, nombre, bd.buscarEmpleadoPorId(idJefe))) ? "Añadido"
				: "No se ha añadido");
	}

	/**
	 * Metodo para borrar departamento
	 * @param bd
	 */
	private static void deleteDepartamento(Gestion bd) {
		Integer id;
		IO.println(bd.showDepartamentos());
		IO.println("Id del departamento?");
		id = IO.readInt();
		IO.println(bd.removeDepartamento(id) ? "Departamento con id " + id + " eliminado con exito"
				: "No se ha podido eliminar el departamento");
	}

	/**
	 * Metodo para borrar empleado
	 * @param bd
	 */
	private static void deleteEmpleado(Gestion bd) {
		Integer id;
		IO.println(bd.showEmpleados());
		IO.println("Id del empleado?");
		id = IO.readInt();
		IO.println(bd.removeEmpleado(id) ? "Empleado con id " + id + " eliminado con exito"
				: "No se ha podido eliminar el empleado");
	}

	/**
	 * Metodo para cerrar la Base de Datos
	 * @param bd
	 */
	private static void closeBD(Gestion bd) {
		IO.println("Saliendo..");
		bd.close();
	}

	/**
	 * Metodo para modificar empleado
	 * @param bd
	 */
	private static void modifyEmpleado(Gestion bd) {
		Integer id;
		Empleado empleado;
		Integer opt;
		boolean finDelMenu = false;
		String nombre;
		double salario;
		int idDepartamento;

		IO.println(bd.showEmpleados());

		IO.println("Introduce el id del empleado a modificar: ");
		id = IO.readInt();
		empleado = bd.buscarEmpleadoPorId(id);

		do {
			IO.println("1)\tNombre\n2)\tSalario\n3)\tDepartamento\n0)\tSalir");
			IO.println("Qué desea realizar: ");

			opt = IO.readInt();
			switch (opt) {
			case 1:
				IO.println("Está modificando el nombre\nIntroduzca el nuevo nombre del empleado: ");
				nombre = IO.readString();
				empleado.setNombre(nombre);
				break;
			case 2:
				IO.println("Está modificando el salario\nIntroduzca el nuevo salario: ");
				salario = IO.readDouble();
				try {
					empleado.setSalario(salario);
				} catch (InvalidAttributeValueException e) {
					IO.println(e.getMessage());
				}
				break;
			case 3:
				IO.println("Está modificando el departamento");
				IO.println(bd.showDepartamentos());
				IO.println("Introduzca el id del nuevo departamento: ");
				idDepartamento = IO.readInt();
				if (empleado.getDepartamento() != null) {
					if (bd.updateOldDepartamento(empleado.getDepartamento().getId())) {
						empleado.setDepartamento(bd.buscarDepartamentoPorId(idDepartamento));
					} else {
						IO.println("No se pudo modificar el departamento del empleado.\n");
					}
				}
				else {
					empleado.setDepartamento(bd.buscarDepartamentoPorId(idDepartamento));
				}
				

				break;
			case 0:
				finDelMenu = true;
			default:
				IO.println("No es una elección válida.\n");
				break;
			}

		} while (!finDelMenu);

		IO.println(bd.modificarEmpleado(empleado) ? "Modificado" : "No modificado");

	}

	/**
	 * Menu
	 */
	private static void menuPrincipal() {
		IO.println("1)\tEmpleados");
		IO.println("2)\tDepartamentos");
		IO.println("0)\tSalir");
		IO.println("");
		IO.println("Elige la opcion deseada");
	}

	/**
	 * Menu de empleado
	 */
	private static void menuEmpleado() {
		IO.println("1)\tCrear empleado");
		IO.println("2)\tBorrar empleado");
		IO.println("3)\tModificar empleado");
		IO.println("4)\tMostrar empleado");
		IO.println("0)\tSalir");
		IO.println("");
		IO.println("Elige la opcion deseada");
	}

	/**
	 * Menu Departamento
	 */
	private static void menuDepartamento() {
		IO.println("1)\tCrear departamento");
		IO.println("2)\tBorrar departamento");
		IO.println("3)\tModificar departamento");
		IO.println("4)\tMostrar departamento");
		IO.println("0)\tSalir");
		IO.println("");
		IO.println("Elige la opcion deseada");
	}
}
