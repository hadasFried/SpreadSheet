package Entities;

import java.util.HashMap;

import Enums.ColumnType;

public class Column {
	
	private String name;
	private ColumnType type;
	private HashMap<Integer,Cell> cells;
	
	
	public Column(String name, ColumnType type) {
		super();
		this.name = name;
		this.type = type;
		this.cells = new HashMap<>();
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public ColumnType getType() {
		return type;
	}


	public void setType(ColumnType type) {
		this.type = type;
	}
	
	public void setCellValue(int index, Cell cell) {
		if (!isValidType(cell.getValue())) {
            throw new IllegalArgumentException("Invalid value for column '" + name + "'. Expected type: " + type);
        }

        cells.put(index, cell);
	}
	
	private boolean isValidType(Object value) {
		if (value == null) {
			return true;
		}
		switch (this.type) {
			case Boolean:
				return value instanceof Boolean;
			case String:
				return value instanceof String;
            case Int:
                return value instanceof Integer;
            case Double:
                return value instanceof Double;
            default:
                return false;
		}
	}
	
	public Cell getCell(int index) {
		return cells.get(index);
	}


	public HashMap<Integer, Cell> getCells() {
		return cells;
	}


	public void setCells(HashMap<Integer, Cell> cells) {
		this.cells = cells;
	}
	
}
