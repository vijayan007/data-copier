/**
 * 
 */
package com.github.vijayan007;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * @author Vijayan Srinivasan
 * @since 03-Feb-2017 8:09:51 pm
 * 
 */
public class DataCopier {

	private static final Logger LOGGER = Logger.getLogger(DataCopier.class.getName());
	private Database source;
	private Database destination;
	private List<String> sqls;

	public static void main(String[] args) {
		if (args.length == 0) {
			throw new IllegalArgumentException("please provide input-file");
		}

		DataCopier copier = getInstance(args[0]);
		LOGGER.info(copier.toString());

		Connection srcCon = copier.source.openConnection();
		Connection destCon = copier.destination.openConnection();

		try {
			for (String sql : copier.sqls) {
				try {
					Statement srcStmt = srcCon.createStatement();
					ResultSet srcRs = srcStmt.executeQuery(sql);
					ResultSetMetaData srcRsMd = srcRs.getMetaData();

					Statement destStmt = destCon.createStatement();

					String destSelectSql = "select * from " + srcRsMd.getTableName(1) + " where 1=2";
					ResultSet destRs = destStmt.executeQuery(destSelectSql);
					ResultSetMetaData destRsMd = destRs.getMetaData();

					if (destRsMd.getColumnCount() < srcRsMd.getColumnCount()) {
						throw new IllegalArgumentException(sql + " - number of coulmns do not match");
					}

					HashMap<String, Integer> destColumnNames = getColumnNames(destRsMd);

					List<String> columnNames = new ArrayList<>();
					List<String> columnParams = new ArrayList<>();

					for (int i = 1; i < srcRsMd.getColumnCount(); i++) {
						String srcCoulmnName = srcRsMd.getColumnName(i);
						if (destColumnNames.get(srcCoulmnName) == null) {
							throw new IllegalArgumentException(
									"could not find " + srcCoulmnName + " in destination db");
						}
						columnNames.add(srcCoulmnName);
						columnParams.add("?");
					}

					String columns = columnNames.toString().replace('[', '(').replace(']', ')');
					String params = columnParams.toString().replace('[', '(').replace(']', ')');

					String destInsertSql = "insert into " + srcRsMd.getTableName(1) + " " + columns + " values "
							+ params;
					LOGGER.info("Destination SQL:" + destInsertSql);
					PreparedStatement destInsertStmt = destCon.prepareStatement(destInsertSql);

					while (srcRs.next()) {
						for (int i = 1; i < srcRsMd.getColumnCount(); i++) {
							destInsertStmt.setObject(i, srcRs.getObject(i));
						}
						try {
							destInsertStmt.executeUpdate();
						} catch (SQLException e) {
							// Code to skip the conflicted rows
							LOGGER.warning(e.getMessage());
							// Assuming the PK is first column
							LOGGER.warning(
									"Skipping row where " + srcRsMd.getColumnName(1) + " = " + srcRs.getObject(1));
						}
					}
					srcRs.close();
					srcStmt.close();
					destRs.close();
					destStmt.close();
					destInsertStmt.close();
				} catch (SQLException e) {
					LOGGER.warning("Skipping executing sql = " + sql + " because of " + e.getMessage());
				}
			}
			destCon.commit();
		} catch (SQLException e) {
			LOGGER.severe(e.getMessage());
		} finally {
			LOGGER.info("Closing connections");
			Database.closeConnection(destCon);
			Database.closeConnection(srcCon);
			LOGGER.info("Closed connections successfully");
		}
	}

	private static HashMap<String, Integer> getColumnNames(ResultSetMetaData metaData) throws SQLException {
		HashMap<String, Integer> columnNames = new HashMap<>();
		for (int i = 1; i <= metaData.getColumnCount(); i++) {
			columnNames.put(metaData.getColumnName(i), i);
		}
		return columnNames;
	}

	public DataCopier() {
		this.source = new Database();
		this.destination = new Database();
		this.sqls = new ArrayList<>();
	}

	public void addSql(String sql) {
		this.sqls.add(sql);
	}

	public static DataCopier getInstance(String fileName) {
		try {
			FileInputStream in = new FileInputStream(fileName);

			DataCopier copier = new DataCopier();

			Scanner scanner = new Scanner(in);
			copier.source.setJdbcDriverClassName(scanner.nextLine());
			copier.source.setJdbcUrl(scanner.nextLine());
			copier.source.setUsername(scanner.nextLine());
			copier.source.setPassword(scanner.nextLine());

			copier.destination.setJdbcDriverClassName(scanner.nextLine());
			copier.destination.setJdbcUrl(scanner.nextLine());
			copier.destination.setUsername(scanner.nextLine());
			copier.destination.setPassword(scanner.nextLine());

			while (scanner.hasNextLine()) {
				copier.addSql(scanner.nextLine());
			}

			scanner.close();
			in.close();

			return copier;

		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("unable to locate input-file = " + fileName);
		} catch (IOException e) {
			throw new RuntimeException("unable to close the input stream");
		}
	}

	public String toString() {
		return "DataCopier {source=" + source + ", destination=" + destination + ", sqls=" + sqls + "}";
	}

}
