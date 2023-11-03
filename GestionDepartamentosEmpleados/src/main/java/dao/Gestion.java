package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.management.InvalidAttributeValueException;
import model.Departamento;
import model.Empleado;

/**
 * Alvaro Márquez, Achraf Boujaanan, David Martin
 */

public class Gestion {
	private Connection conn = null;

	/**
	 * Constructor de gestion de base de datos
	 */
	public Gestion() {
		conn = BD.open();
		createTables();
	}

	/**
	 * Metodo para cerrar la base de datos
	 */
	public void close() {
		BD.close();
	}

	/**
	 * Metodo para añadir departamento
	 * @param departamento
	 * @return
	 */
	public boolean addDepartamento(Departamento departamento) {
		String sentencia = "INSERT INTO DEPARTAMENTOS (NOMBRE, JEFE) VALUES (?, ?)";
		PreparedStatement ps;
		boolean anadido = false;

		try {
			ps = conn.prepareStatement(sentencia);

			ps.setString(1, departamento.getNombre());
			ps.setObject(2, null);

			anadido = ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return anadido;
	}

	/**
	 * Metodo para añadir empleado
	 * @param empleado
	 * @return
	 */
	public boolean addEmpleado(Empleado empleado) {
		String sql = "INSERT INTO EMPLEADOS (NOMBRE, SALARIO, DEPARTAMENTO) VALUES (?, ?, ?)";
		PreparedStatement ps;
		boolean anadido = false;

		try {
			ps = conn.prepareStatement(sql);

			ps.setString(1, empleado.getNombre());
			ps.setDouble(2, empleado.getSalario());
			ps.setObject(3, null);

			anadido = ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return anadido;
	}

	/**
	 * Metodo para modificar departamento
	 * @param departamento
	 * @return
	 */
	public boolean modificarDepartamento(Departamento departamento) {
		String sentencia = "UPDATE DEPARTAMENTOS SET NOMBRE = ?, JEFE = ? WHERE ID = ?";
		PreparedStatement ps;
		boolean departamentoModificado = false;

		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sentencia);
			ps.setString(1, departamento.getNombre());
			ps.setInt(3, departamento.getId());

			if (departamento.getJefe() != null) {
				ps.setInt(2, departamento.getJefe().getId());
				departamentoModificado = updateNewJefe(departamento.getId(), departamento.getJefe().getId())
						&& ps.executeUpdate() > 0;
			} else {
				ps.setObject(2, null);
				departamentoModificado = ps.executeUpdate() > 0;
			}

			if (departamentoModificado) {
				conn.commit();
			} else {
				conn.rollback();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return departamentoModificado;
	}

	/**
	 * Metodo para asignar nuevo jefe
	 * @param NewDepartamentoId
	 * @param idJefe
	 * @return
	 * @throws SQLException
	 */
	private boolean updateNewJefe(Integer NewDepartamentoId, Integer idJefe) throws SQLException {
		String sentencia = "UPDATE EMPLEADOS SET DEPARTAMENTO = ? WHERE ID = ?";
		PreparedStatement ps;
		boolean updateRealizado = false;

		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sentencia);
			ps.setInt(1, NewDepartamentoId);
			ps.setInt(2, idJefe);

			updateRealizado = ps.executeUpdate() > 0;

			if (updateRealizado) {
				conn.commit();
			} else {
				conn.rollback();
			}

		} catch (SQLException e) {
			conn.rollback();
			throw new SQLException();
		}
		return updateRealizado;
	}

	/**
	 * Metodo para modificar empleado
	 * @param empleado
	 * @return
	 */
	public boolean modificarEmpleado(Empleado empleado) {
		String sentencia = "UPDATE EMPLEADOS SET NOMBRE = ?, SALARIO = ?, DEPARTAMENTO = ? WHERE ID = ?";
		PreparedStatement ps;
		boolean empleadoModificado = false;

		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sentencia);
			ps.setString(1, empleado.getNombre());
			ps.setDouble(2, empleado.getSalario());
			ps.setInt(4, empleado.getId());

			if (empleado.getDepartamento() != null) {
				ps.setInt(3, empleado.getDepartamento().getId());

			} else {
				ps.setObject(3, null);
			}

			empleadoModificado = ps.executeUpdate() > 0;

			if (empleadoModificado) {
				conn.commit();
			} else {
				conn.rollback();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return empleadoModificado;
	}

	/**
	 * Metodo para updatear el departamento antiguo
	 * @param idOldDepartamento
	 * @return
	 */
	public boolean updateOldDepartamento(Integer idOldDepartamento) {
		String sentencia = "UPDATE DEPARTAMENTOS SET JEFE = ? WHERE ID = ?";
		PreparedStatement ps;
		boolean departamentoModificado = false;

		try {
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sentencia);
			ps.setObject(1, null);
			ps.setInt(2, idOldDepartamento);

			departamentoModificado = ps.executeUpdate() > 0;

			if (departamentoModificado) {
				conn.commit();
			} else {
				conn.rollback();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return departamentoModificado;
	}

	/**
	 * MEtodo para borrar un departamento
	 * @param idDepartamento
	 * @return
	 */
	public boolean removeDepartamento(Integer idDepartamento) {
		String sql1 = """
				DELETE FROM DEPARTAMENTOS WHERE ID = ?
				""";
		try {
			PreparedStatement ps = conn.prepareStatement(sql1);
			ps.setInt(1, idDepartamento);

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
		}
		return false;
	}

	/**
	 * Metodo para borrar empleado
	 * @param idEmpleado
	 * @return
	 */
	public boolean removeEmpleado(Integer idEmpleado) {
		String sql1 = """
				DELETE FROM EMPLEADOS WHERE ID = ?
				""";

		try {
			PreparedStatement ps = conn.prepareStatement(sql1);
			ps.setInt(1, idEmpleado);

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
		}
		return false;
	}

	/**
	 * Metodo para mostrar departamentos
	 * @return
	 */
	public String showDepartamentos() {
		String sentencia = "SELECT * FROM DEPARTAMENTOS";
		StringBuffer sb;
		ResultSet rs;
		try {
			sb = new StringBuffer();
			rs = conn.createStatement().executeQuery(sentencia);

			while (rs.next()) {
				Departamento d = readDepartamento(rs);
				sb.append(d);
				sb.append("\n");
			}

			return sb.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * Metodo para leer departamento
	 * @param rs
	 * @return
	 */
	private Departamento readDepartamento(ResultSet rs) {
		Integer id;
		String nombre;

		try {
			id = rs.getInt("ID");
			nombre = rs.getString("NOMBRE");
			Departamento dep = new Departamento(id, nombre, null);
			dep.setJefe(buscarEmpleadoPorId(rs.getInt("JEFE")));

			return dep;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Metodo para mostrar empleados
	 * @return
	 */
	public String showEmpleados() {
		String sentencia = "SELECT * FROM EMPLEADOS";
		StringBuffer sb;
		ResultSet rs;

		try {
			sb = new StringBuffer();
			rs = conn.createStatement().executeQuery(sentencia);

			while (rs.next()) {
				Empleado e = readEmpleado(rs);
				sb.append(e.toString());
				sb.append("\n");
			}

			return sb.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * Metodo para leer departamento
	 * @param rs
	 * @return
	 */
	private Empleado readEmpleado(ResultSet rs) {
		Integer id;
		String nombre;
		Double salario;

		try {
			id = rs.getInt("ID");
			nombre = rs.getString("NOMBRE");
			salario = rs.getDouble("SALARIO");

			Empleado empleado = new Empleado(id, nombre, salario, null);

			empleado.setDepartamento(buscarDepartamentoPorId(rs.getInt("ID")));

			return empleado;

		} catch (SQLException | InvalidAttributeValueException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Metodo para buscar un empleado por ID
	 * @param id
	 * @return
	 */
	public Empleado buscarEmpleadoPorId(Integer id) {
		String sentencia = "SELECT * FROM EMPLEADOS WHERE ID = ?";
		ResultSet rs;

		try {
			PreparedStatement ps = conn.prepareStatement(sentencia);
			ps.setInt(1, id);
			rs = ps.executeQuery();

			if (rs.next()) {
				Empleado empleado = readEmpleado(rs);
				return empleado;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Metodo para buscar un departamento por ID
	 * @param id
	 * @return
	 */
	public Departamento buscarDepartamentoPorId(Integer id) {

		String sentencia = "SELECT * FROM DEPARTAMENTOS WHERE ID = ?";
		ResultSet rs;

		try {
			PreparedStatement ps = conn.prepareStatement(sentencia);
			ps.setInt(1, id);
			rs = ps.executeQuery();

			if (rs.next()) {
				Departamento departamento = readDepartamento(rs);
				return departamento;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Metodo para borrar las tablas
	 */
	public void dropTables() {

		dropTableDepartamento();
		dropTableEmpleado();
	}

	/**
	 * Metodo para borrar la tabla departamentos
	 */
	private void dropTableDepartamento() {
		String sentencia = "DELETE FROM DEPARTAMENTOS";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sentencia);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * MEtodo para borrar la tabla empleados
	 */
	private void dropTableEmpleado() {
		String sentencia = "DELETE FROM EMPLEADOS";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sentencia);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Metodo para crear las tablas en sqlite y mariadb
	 */
	private void createTables() {
		String sql1 = "";
		String sql2 = "";
		if (BD.typeDB.equals("sqlite")) {
			sql1 = """
					CREATE TABLE IF NOT EXISTS EMPLEADOS (
						ID INTEGER PRIMARY KEY AUTOINCREMENT,
						NOMBRE TEXT NOT NULL,
						SALARIO REAL DEFAULT 0.0,
						DEPARTAMENTO INTEGER
					)
					 """;
			sql2 = """
						CREATE TABLE IF NOT EXISTS DEPARTAMENTOS (
						ID INTEGER PRIMARY KEY AUTOINCREMENT,
						NOMBRE TEXT NOT NULL,
						JEFE INTEGER
					)
					 """;
		}

		if (BD.typeDB.equals("mariadb")) {
			sql1 = """
					CREATE TABLE IF NOT EXISTS EMPLEADOS (
						ID INT PRIMARY KEY AUTO_INCREMENT,
						NOMBRE VARCHAR(255),
						SALARIO DOUBLE(10,2),
						DEPARTAMENTO INT
					);
					 """;
			sql2 = """
						CREATE TABLE IF NOT EXISTS DEPARTAMENTOS (
						ID INT PRIMARY KEY AUTO_INCREMENT,
						NOMBRE VARCHAR(255),
						JEFE INT
					)
					 """;
		}
		
		try {
			conn.createStatement().executeUpdate(sql1);
			conn.createStatement().executeUpdate(sql2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}