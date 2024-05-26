package Entities;

import java.util.HashMap;

public class Spreadsheet {
	
	private static int latestId = 1;
	
	private HashMap<String, Column> columns;
	private int id;
	
	public Spreadsheet() {
		columns = new HashMap<>();
		id = latestId;
	}

	public Spreadsheet(HashMap<String, Column> columns) {
		this.columns = columns;
		this.id = latestId++;
	}

	public HashMap<String, Column> getColumns() {
		return columns;
	}

	public void setColumns(HashMap<String, Column> columns) {
		this.columns = columns;
	}

	public int getId() {
		return id;
	}
	
	public void setCell (String columnName, int cellId, Object value) {
		if (columns != null) {
			if(value == null) {
				Column column = columns.get(columnName);
				if (column != null) {
					column.setCellValue(cellId, new Cell());
					return;
				}
			}
			Cell cell = new Cell();
			if (value instanceof String && ((String) value).startsWith("lookup")) {
				cell = lookup((String) value, columnName, cell, cellId);
			}
			else {
				cell.setIndex(cellId);
				cell.setValue(value);
			}
			Column column = columns.get(columnName);
			if (column != null) {
				column.setCellValue(cellId, cell);
				return;
			}
		}
        throw new IllegalArgumentException("Column '" + columnName + "' doesn't exist in the sheet");	
	}
	
	private Cell lookup(String value, String columnName, Cell cell, int cellId) {
		//firstSplitedString holds in [0] the "lookup" and in [1] the start of the fields
		String fieldsOfLookup = ((String) value).substring(7);
		if (fieldsOfLookup == null || fieldsOfLookup.isEmpty()) {
			throw new IllegalArgumentException("Invalid call for the lookup function- no fields are mentioned");
		}
		//secondSplitedString holds in [0] the column name and in [1] the cell id + ")"
		String[] splitedFields = fieldsOfLookup.split(",");
		if (splitedFields[0] == null) {
			throw new IllegalArgumentException("Invalid call for the lookup function- no column name mentioned");
		}
		String lookupColumnName = splitedFields[0];
		if (splitedFields[1] == null) {
			throw new IllegalArgumentException("Invalid call for the lookup function- no cell id mentioned");
		}
		String lookupCellId = splitedFields[1].substring(0, splitedFields[1].length()-1);
		if (lookupColumnName.equals(columnName) && lookupCellId.equals(String.valueOf(cellId))) {
			throw new IllegalArgumentException("Invalid call for the lookup function- cycles are not allowed");
		}
		cell = getCell(lookupColumnName, Integer.valueOf(lookupCellId));
		if (cell != null && cell.isLookupValue()) {
			throw new IllegalArgumentException("Invalid call for the lookup function- cycles are not allowed");
		}
		if (cell == null) {
			cell = new Cell(Integer.valueOf(lookupCellId), null, true);
		}
		else {
			cell.setLookupValue(true);
		}
		return cell;
	}
	
	public Cell getCell (String columnName, int cellId) {
		if (columns.get(columnName) == null) {
			throw new IllegalArgumentException("Column '" + columnName + "' doesn't exist in the sheet");
		}
		return columns.get(columnName).getCell(cellId);
	}
	
	public Object getCellValue (String columnName, int cellId) {
		return getCell(columnName, cellId).getValue();
	}

}
