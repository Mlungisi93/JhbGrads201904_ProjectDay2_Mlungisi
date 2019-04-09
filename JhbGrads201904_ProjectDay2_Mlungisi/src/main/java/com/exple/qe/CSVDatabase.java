package com.exple.qe;

import net.thucydides.core.annotations.Step;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class CSVDatabase {
    private String URLName, user, password;
    private Connection conn;
    private Statement stmt;
    private ResultSet result;
    private ArrayList<Data> arData;
    private File mainFile;


    public CSVDatabase() {

    }

    public File getMainFile() {
        return mainFile;
    }



    @Step("Navigate to my CSV file")
    public void goToCSVFile(String fileName) throws DatabaseException {
//check if file exist
    mainFile = new File(fileName+".txt");
    if (mainFile.exists() == true) {
        //check if file is not empty
        if (mainFile.length() > 0) // file not empty
        {

        }else // file is empty
        {


                assertThat(fileName+" File Is Empty.", Math.toIntExact(mainFile.length()), greaterThan(0));


        }
    }else //file does not exit
    {

            String filePath = new File("").getAbsolutePath();
            filePath.concat(" path to the property file");
            assertThat(fileName +".txt File Does not exist In "+filePath, mainFile.exists(), equalTo(true));

    }
}


    @Step("Process the CSV File Against with Database")
    public void processing(File file) throws DatabaseException, SQLException {
        String[] data;
        Data objDataModel;
        String line;
        String databaseName;
        String tableName;
        String primaryKeyName;
        int primaryKeyValue;
        String otherColumnName;
        String otherColumnValue;

        Scanner sc;
        String fileName = "";
        Connection conn;
        Statement stmt;
        PreparedStatement pstmt;
        int count = 0;

  arData = new ArrayList<>();


            try {


                Scanner inputFileReader = new Scanner(new FileReader(file));
                int countDelimeters = 0; // will count the ,delimeters
                // Read the file and skip the first line
                while (inputFileReader.hasNextLine()) // Read next line
                {

                    line = inputFileReader.nextLine();

                    countDelimeters = 0; // restart count for each line
                    for (int i = 0; i < line.length(); i++) {

                        if (line.charAt(i) == ',') {
                            countDelimeters++;
                        }
                    }

                    if (countDelimeters != 5) {
                        //initialise the wrong format with a messade
                        objDataModel = new Data("", "","",
                                "","","","Line:" + line + " :is in a wrong format: Line should have 6 Values separated by comma");
                        arData.add(objDataModel);
                        count++;
                        continue;
                    } else {
                        data = line.split(",");

                        try {
                            databaseName = data[0];

                            // check if databaseName exits
                            File fileDatabase = new File(databaseName + ".db");
                            //skip first line because it has got file headers
                            if (count > 0) {
                                //print out each line where currently reading
                                System.out.println("On Line: " + count);

                                if (fileDatabase.exists()) //here's how to check if the database exist
                                {
                                    tableName = data[1]; //initialize table name
                                    //connect to sqlite database
                                    conn = DriverManager.getConnection("jdbc:sqlite:" + fileDatabase, user, password);

                                    //check if table exists
                                    pstmt = conn.prepareStatement("SELECT count(*) as count FROM " +
                                            "sqlite_master WHERE type='table' AND name =?");
                                    pstmt.setString(1, tableName);
                                    ResultSet rsNumberOfTables = pstmt.executeQuery();
                                    int numberOfTables = -1;

                                    while (rsNumberOfTables.next()) {
                                        numberOfTables = Integer.parseInt(rsNumberOfTables.getString("count"));
                                    }

                                    if (numberOfTables > 0) // table is found
                                    {
                                        //check if the primary key name
                                        primaryKeyName = data[2];

                                        // This method will check if column exists and is the primary key in your table
                                        pstmt = conn.prepareStatement("SELECT COUNT(*) AS CNTREC FROM pragma_table_info('" + tableName + "')" +
                                                " WHERE name='" + primaryKeyName + "' AND pk=1");
                                        //pstmt.setString(1, "ArtistId");
                                        ResultSet rsNumberOfRec = pstmt.executeQuery();
                                        int numberOfColumbs = -1;

                                        while (rsNumberOfRec.next()) {
                                            numberOfColumbs = Integer.parseInt(rsNumberOfRec.getString("CNTREC"));
                                        }

                                        if (numberOfColumbs > 0) // key columb found and its a primary key
                                        {
                                            // check primary key value
                                            try {
                                                primaryKeyValue = Integer.parseInt(data[3]);
                                                pstmt = conn.prepareStatement("SELECT COUNT(*) AS CNTREC FROM " + tableName +
                                                        " WHERE " + primaryKeyName + "='" + primaryKeyValue + "'");

                                                ResultSet rsNumberOfRecPK = pstmt.executeQuery();
                                                int numberOfRows = -1;

                                                while (rsNumberOfRecPK.next()) {
                                                    numberOfRows = Integer.parseInt(rsNumberOfRecPK.getString("CNTREC"));
                                                }

                                                if (numberOfColumbs > 0) // row found using its a primary columb and value
                                                {
                                                    otherColumnName = data[4];
                                                    pstmt = conn.prepareStatement("SELECT COUNT(*) AS CNTREC FROM pragma_table_info('" + tableName + "')" +
                                                            " WHERE name='" + otherColumnName + "'");
                                                    //pstmt.setString(1, otherColumnName);
                                                    ResultSet rsNumberOfOtherColumb = pstmt.executeQuery();
                                                    int numberOfOtherColumbs = -1;

                                                    while (rsNumberOfOtherColumb.next()) {
                                                        numberOfOtherColumbs = Integer.parseInt(rsNumberOfOtherColumb.getString("CNTREC"));
                                                    }

                                                    if (numberOfOtherColumbs > 0) // other columb found in the same table
                                                    {
                                                        otherColumnValue = data[5];
                                                        pstmt = conn.prepareStatement("SELECT COUNT(*) AS CNTREC FROM " + tableName +
                                                                " WHERE " + primaryKeyName + "='" + primaryKeyValue + "' AND " + otherColumnName + "='" +
                                                                otherColumnValue + "'");
                                                        //pstmt.setInt(1, primaryKeyValue);
                                                        //pstmt.setString(2, otherColumnValue);
                                                        ResultSet rsNumberOfRecOther = pstmt.executeQuery();
                                                        int numberOfRowsOther = -1;

                                                        while (rsNumberOfRecOther.next()) {
                                                            numberOfRowsOther = Integer.parseInt(rsNumberOfRecOther.getString("CNTREC"));
                                                        }

                                                        if (numberOfRowsOther > 0) // row found using its other columb and its value
                                                        {
                                                            objDataModel = new Data(data[0], data[1], data[2],data[3],data[4],data[5],"OK");
                                                            arData.add(objDataModel);

                                                        } else {
                                                            try {
                                                                //System.out.println("");
                                                                assertThat(otherColumnName + "Columb has No Record Found: for: " + otherColumnValue + " value In " + tableName + " Table", numberOfRowsOther, greaterThan(0));
                                                            } catch (AssertionError e) {
                                                                objDataModel = new Data(data[0], data[1], data[2],data[3],data[4],data[5],"No Record Found: for: " + otherColumnValue + " value");
                                                                arData.add(objDataModel);
                                                                count++;
                                                                continue; // read next line

                                                            }
                                                        }


                                                    } else {
                                                        try {
                                                            //System.out.println("");
                                                            assertThat("Invalid Columb: " + otherColumnName + " In " + tableName + " Table", numberOfOtherColumbs, greaterThan(0));

                                                        } catch (AssertionError e) {
                                                            objDataModel = new Data(data[0], data[1], data[2],data[3],data[4],data[5],"Invalid Columb: " + otherColumnName);
                                                            arData.add(objDataModel);
                                                            count++;
                                                            continue; // read next line

                                                        }

                                                    }


                                                } else {
                                                    try {
                                                        //System.out.println("");
                                                        assertThat("Invalid primary key value: " + primaryKeyValue + " In " + tableName + " Table", numberOfColumbs, greaterThan(0));

                                                    } catch (AssertionError e) {
                                                        objDataModel = new Data(data[0], data[1], data[2],data[3],data[4],data[5],"Invalid primary key value: " + primaryKeyValue + " In " + tableName + " Table");
                                                        arData.add(objDataModel);
                                                        count++;
                                                        continue; // read next line
                                                    }

                                                }
                                            } catch (NumberFormatException e) {
                                                objDataModel = new Data(data[0], data[1], data[2],data[3],data[4],data[5],"Invalid integer: " + e.getMessage());
                                                arData.add(objDataModel);
                                                count++;
                                                continue;

                                            }


                                        } else {
                                            try {
                                                //System.out.println("");
                                                assertThat("Invalid primary key name: " + primaryKeyName + " Columb is not a primary key for " + tableName + " Table", numberOfColumbs, greaterThan(0));
                                            } catch (AssertionError e) {
                                                objDataModel = new Data(data[0], data[1], data[2],data[3],data[4],data[5],"Invalid primary key name: " + primaryKeyName + " Columb is not a primary key for " + tableName + " Table");
                                                arData.add(objDataModel);
                                                count++;
                                                continue; // read next line
                                            }

                                        }


                                    } else // table does not exist
                                    {
                                        try {
                                            //System.out.println("");
                                            assertThat("Invalid table name: " + tableName + " Table does not exist In " + databaseName + " database", numberOfTables, greaterThan(0));
                                        } catch (AssertionError e) {

                                            objDataModel = new Data(data[0], data[1], data[2],data[3],data[4],data[5],"Invalid table name: " + tableName + " Table does not exist In " + databaseName);
                                            arData.add(objDataModel);

                                            count++;
                                            continue; // read next line
                                        }

                                    }


                                } else {

                                    //.out.println("Invalid Database Name: \"+\"Database does not exist:\"+databaseName");
                                    try {
                                        //System.out.println("");
                                        assertThat("Invalid Database Name: " + databaseName + " Database does not exist", fileDatabase.exists(), equalTo(true));

                                    } catch (AssertionError e) {
                                        objDataModel = new Data(data[0], data[1], data[2],data[3],data[4],data[5],"Invalid Database Name: " + databaseName + " Database does not exist");
                                        arData.add(objDataModel);
                                        count++;
                                        e.fillInStackTrace();
                                        continue;// it should teake the user to reenter name
                                    }

                                }

                            }
                            count++;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            //System.out.println("");
                            //System.out.println("On Line" + count);
                            if (e.getMessage().equals("0")) {
                                objDataModel = new Data("", "", "","","","","Empty dababase name");
                                arData.add(objDataModel);


                            } else if (e.getMessage().equals("1")) {
                                objDataModel = new Data("", "", "","","","","Empty table name");
                                arData.add(objDataModel);

                            } else if (e.getMessage().equals("2")) {
                                objDataModel = new Data("", "", "","","","","Empty Key Column name");
                                arData.add(objDataModel);


                            } else if (e.getMessage().equals("3")) {

                                objDataModel = new Data("", "", "","","","","Empty Key Value");
                                arData.add(objDataModel);


                            } else if (e.getMessage().equals("4")) {
                                objDataModel = new Data("", "", "","","","","Empty Column name");
                                arData.add(objDataModel);

                            } else if (e.getMessage().equals("5")) {

                                objDataModel = new Data("", "", "","","","","Empty Column Value");
                                arData.add(objDataModel);

                            }

                            count++;
                            continue;
                        }
                    }


                } //end of while loop
                inputFileReader.close();
            } catch (IOException ex) {

                assertThat("There was a problem reading "
                        + "from the da ex.getMessagta storage device.\n "
                        + ex.getMessage(), 1, greaterThan(2));
                count++;

            }


        }


    @Step("Get the message")
    public String getLineWithMessage()
    {
        String line = "";
       for(Data s: arData)
        {
            line = line+ s.toString();
        }

       return line;
    }

    @Step("{0}") // TODO later
    public void message(String message) {
    }
}
