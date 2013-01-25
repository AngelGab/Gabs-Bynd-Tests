package seleniumScripts;

import com.thoughtworks.selenium.*;
import org.junit.AfterClass;
import org.openqa.selenium.server.SeleniumServer;
import org.testng.annotations.*;
import java.io.File;
import jxl.*;

public class byndAutoTests extends SeleneseTestCase{

    @BeforeClass
    public void setUp() throws Exception {
    	//selenium.setSpeed("2000");
    	SeleniumServer server = new SeleniumServer();
    	server.boot();
    	server.start();
    	setUp("http://www.amazon.co.uk/", "*firefox");
    	selenium.open("/");
        selenium.windowMaximize();
        selenium.windowFocus();
    }

    @DataProvider (name = "PD1")
    public Object[][] createData1() throws Exception{
          Object[][] retObjArr=getTableArray("test//dataHandler//data//data1.xls",
                       "PhoneData", "PhoneTestData1");
          return(retObjArr);
    }


   @Test (dataProvider = "PD1")
   public void testDataProvider(String mobileName,
           String companyName) throws Exception {
       selenium.type("//input[@id='twotabsearchtextbox']", mobileName);
       //they keep switching the go button
       //if (selenium.isElementPresent("nb15go_image"))
         //  selenium.click("nb15go_image");
       //else
       selenium.click("xpath=//input[@type='submit' and @value='Go']");

       selenium.waitForPageToLoad("30000");
       verifyTrue(selenium.isTextPresent(companyName));
   }

   @AfterClass
   public void tearDown(){
       selenium.close();
       selenium.stop();
   }

   public String[][] getTableArray(String xlFilePath, String sheetName, String tableName) throws Exception{
       String[][] tabArray=null;

           Workbook workbook = Workbook.getWorkbook(new File(xlFilePath));
           Sheet sheet = workbook.getSheet(sheetName);
           int startRow,startCol, endRow, endCol,ci,cj;
           Cell tableStart=sheet.findCell(tableName);
           startRow=tableStart.getRow();
           startCol=tableStart.getColumn();

           Cell tableEnd= sheet.findCell(tableName, startCol+1,startRow+1, 100, 64000,  false);

           endRow=tableEnd.getRow();
           endCol=tableEnd.getColumn();
           System.out.println("startRow="+startRow+", endRow="+endRow+", " +
                   "startCol="+startCol+", endCol="+endCol);
           tabArray=new String[endRow-startRow-1][endCol-startCol-1];
           ci=0;

           for (int i=startRow+1;i<endRow;i++,ci++){
               cj=0;
               for (int j=startCol+1;j<endCol;j++,cj++){
                   tabArray[ci][cj]=sheet.getCell(j,i).getContents();
               }
           }


       return(tabArray);
   }




}//end of class