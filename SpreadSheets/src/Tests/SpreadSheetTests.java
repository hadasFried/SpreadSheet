package Tests;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import Entities.Cell;
import Entities.Column;
import Entities.Spreadsheet;
import Enums.ColumnType;
import manager.SpreadsheetsService;

public class SpreadSheetTests {
	
	HashMap<String, Column> columns;
	SpreadsheetsService service = SpreadsheetsService.getInstance();
	
	public SpreadSheetTests() {
		columns = new HashMap<>();
        columns.put("A", new Column("A", ColumnType.String));
        columns.put("B", new Column("B", ColumnType.Boolean));
        columns.put("C", new Column("C", ColumnType.String));
        columns.put("D", new Column("D", ColumnType.Int));
	}

	@Test
    public void testCreateEmptySheet() {
        Spreadsheet sheet = new Spreadsheet();
        int sheetId = service.createSheet(sheet);
        assertNotNull(sheetId);
    }
	
	@Test
    public void testCreateSheetLongerNames() {
		HashMap<String, Column> columnsLongerNames = new HashMap<String, Column>();
		columnsLongerNames.put("nameA", new Column("nameA", ColumnType.String));
        Spreadsheet sheet = new Spreadsheet(columnsLongerNames);
        int sheetId = service.createSheet(sheet);
        assertNotNull(sheetId);
    }
	
	@Test
    public void testCreateNonEmptySheet() {
        Spreadsheet sheet = new Spreadsheet(columns);
        int sheetId = service.createSheet(sheet);
        assertNotNull(sheetId);
    }
	
	@Test
    public void testCreateTwoEmptySheets() {
        Spreadsheet sheet1 = new Spreadsheet();
        Spreadsheet sheet2 = new Spreadsheet();
        int sheetId1 = service.createSheet(sheet1);
        int sheetId2 = service.createSheet(sheet2);
        assertNotNull(sheetId1);
        assertNotNull(sheetId2);
    }
	
	@Test
    public void testCreateTwoNonEmptySheets() {
		Spreadsheet sheet1 = new Spreadsheet(columns);
        Spreadsheet sheet2 = new Spreadsheet(columns);
        int sheetId1 = service.createSheet(sheet1);
        int sheetId2 = service.createSheet(sheet2);
        assertNotNull(sheetId1);
        assertNotNull(sheetId2);
    }
	
	@Test
    public void testSetCellNull() {
        Spreadsheet sheet = new Spreadsheet(columns);
        int sheetId = service.createSheet(sheet);
        
        service.setCell(sheetId, "A", 10, null);
        
        // Assuming no exceptions are thrown- the test passes
        assertTrue(true);
    }

    @Test
    public void testSetCellSuccessNoLookup() {
        Spreadsheet sheet = new Spreadsheet(columns);
        int sheetId = service.createSheet(sheet);
        
        service.setCell(sheetId, "A", 10, "hello");
        service.setCell(sheetId, "B", 11, true);
        
        // Assuming no exceptions are thrown- the test passes
        assertTrue(true);
    }
    
    @Test
    public void testSetCellSuccessLookup() {
        Spreadsheet sheet = new Spreadsheet(columns);
        int sheetId = service.createSheet(sheet);
        
        service.setCell(sheetId, "A", 10, "hello");
        service.setCell(sheetId, "B", 11, true);
        service.setCell(sheetId, "C", 1, "lookup(A,10)");

        assertEquals( "hello", service.getSheet(sheetId).getCellValue("C", 1));
        
        // Assuming no exceptions are thrown- the test passes
        assertTrue(true);
    }

    @Test
    public void testSetCellFailureTypeMismatch() {
        Spreadsheet sheet = new Spreadsheet(columns);
        int sheetId = service.createSheet(sheet);
        
        try {
            service.setCell(sheetId, "A", 10, 1);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid value for column 'A'. Expected type: String", e.getMessage());
        }
    }
    
    @Test
    public void testSetCellFailureTypeMismatchLookup() {
        Spreadsheet sheet = new Spreadsheet(columns);
        int sheetId = service.createSheet(sheet);
        
        try {
            service.setCell(sheetId, "A", 10, "hello");
            service.setCell(sheetId, "B", 1, "lookup(A,10)");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid value for column 'B'. Expected type: Boolean", e.getMessage());
        }
    }
    
    
    @Test
    public void testSetCellNullValue() {
        Spreadsheet sheet = new Spreadsheet(columns);
        int sheetId = service.createSheet(sheet);
        
        service.setCell(sheetId, "B", 1, "lookup(A,10)");
        // Assuming no exceptions are thrown- the test passes
        assertTrue(true);
    }

    @Test
    public void testSetCellFailureOneCircle() {
        Spreadsheet sheet = new Spreadsheet(columns);
        int sheetId = service.createSheet(sheet);
        
        try {
            service.setCell(sheetId, "C", 1, "lookup(C,1)");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Test passed
            assertEquals("Invalid call for the lookup function- cycles are not allowed", e.getMessage());
        }
    }
    
    @Test
    public void testSetCellFailureTwoCircle() {
        Spreadsheet sheet = new Spreadsheet(columns);
        int sheetId = service.createSheet(sheet);
        
        try {
        	service.setCell(sheetId, "C", 1, "lookup(A,10)");
            service.setCell(sheetId, "A", 10, "lookup(C,1)");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Test passed
            assertEquals("Invalid call for the lookup function- cycles are not allowed", e.getMessage());
        }
    }
    
    @Test
    public void testSetCellFailureThreeCircle() {
        Spreadsheet sheet = new Spreadsheet(columns);
        int sheetId = service.createSheet(sheet);
        
        try {
        	service.setCell(sheetId, "C", 1, "lookup(A,1)");
            service.setCell(sheetId, "A", 1, "lookup(B,1)");
            service.setCell(sheetId, "B", 1, "lookup(C,1)");
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Test passed
            assertEquals("Invalid call for the lookup function- cycles are not allowed", e.getMessage());
        }
    }

    @Test
    public void testGetSheet() {
        Spreadsheet sheet = new Spreadsheet(columns);
        int sheetId = service.createSheet(sheet);
        
        Spreadsheet retrievedSheet = service.getSheet(sheetId);
        assertTrue(assertTwoSheetsEquals(sheet, retrievedSheet));
    }
    
    @Test
    public void testGetSheetAndSetCell() {
        Spreadsheet sheet = new Spreadsheet(columns);
        int sheetId = service.createSheet(sheet);
        
        service.setCell(sheetId, "C", 1, "lookup(A,1)");
        service.setCell(sheetId, "A", 1, "lookup(B,1)");
        
        Spreadsheet retrievedSheet = service.getSheet(sheetId);
        assertTrue(assertTwoSheetsEquals(sheet, retrievedSheet));
    }
    
    private boolean assertTwoSheetsEquals(Spreadsheet sheet1, Spreadsheet sheet2) {
    	if (sheet1.getId() != sheet2.getId())
    		return false;
    	for (Column column1 : sheet1.getColumns().values()) {
    		if (!sheet2.getColumns().containsKey(column1.getName())) {
    			return false;
    		}
    		Column column2 =  sheet2.getColumns().get(column1.getName());
    		if (!column1.getName().equals(column2.getName())) {
    			return false;
    		}
    		if (!column1.getType().equals(column2.getType())) {
    			return false;
    		}
    		for (Cell cell1 : column1.getCells().values()) {
    			if (!column2.getCells().containsKey(cell1.getIndex())) {
        			return false;
        		}
        		Cell cell2 =  column2.getCells().get(cell1.getIndex());
        		if (cell1.getIndex() != (cell2.getIndex())) {
        			return false;
        		}
        		if ((cell1.getValue() != null && cell1.getValue().equals(cell2.getValue()))
        				|| (cell1.getValue() == null && cell2.getValue() != null)) {
        			return false;
        		}
        		if (cell1.isLookupValue() != cell2.isLookupValue()) {
        			return false;
        		}
			}
    	}    	
    	return true;
    }
    

}
