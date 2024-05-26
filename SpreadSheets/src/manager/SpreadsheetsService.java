package manager;

import java.util.HashMap;

import Entities.Spreadsheet;

public class SpreadsheetsService {
	
	private HashMap<Integer, Spreadsheet> spreadSheets = new HashMap<>();
	private static SpreadsheetsService instance = null;

	private SpreadsheetsService() {
		this.spreadSheets = new HashMap<>();
	}
	
	public static synchronized SpreadsheetsService getInstance() {
		if (instance == null) {
			instance = new SpreadsheetsService();
		}
		return instance;
	}

	public int createSheet(Spreadsheet spreadsheet) {
		spreadSheets.put(spreadsheet.getId(), spreadsheet);
        return spreadsheet.getId();
    }
	
	public void setCell (int sheetId, String columnName, int cellId, Object value) {
		Spreadsheet sheet = getSheet(sheetId);
		sheet.setCell(columnName, cellId, value);	
	}
	
	public Spreadsheet getSheet(int id) {
		return spreadSheets.get(id);
	}
}
