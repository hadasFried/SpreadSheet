package Entities;

public class Cell {
	
	private int index;
	private Object value;
	private boolean isLookupValue;
	
	public Cell() {
		isLookupValue = false;
	}

	public Cell(int index, Object value, boolean isLookupValue) {
		super();
		this.index = index;
		this.value = value;
		this.isLookupValue = isLookupValue;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public boolean isLookupValue() {
		return isLookupValue;
	}

	public void setLookupValue(boolean isLookupValue) {
		this.isLookupValue = isLookupValue;
	}

}
