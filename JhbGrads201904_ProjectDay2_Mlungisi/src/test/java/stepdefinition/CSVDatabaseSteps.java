package stepdefinition;

import com.exple.qe.CSVDatabase;
import com.exple.qe.DatabaseException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;

import java.sql.SQLException;

public class CSVDatabaseSteps {
    @Steps
    CSVDatabase csvDatabase;

    @Given("^That I have a CSV file$")
    public void that_I_have_a_CSV_file() throws DatabaseException {

        csvDatabase.goToCSVFile("ProjectDay2");

    }

    @When("^I process CSV file against database$")
    public void i_process_CSV_file_against_database() throws DatabaseException, SQLException {

        csvDatabase.processing(csvDatabase.getMainFile());

    }

    @Then("^The correct output should be generated in Serenity$")
    public void then_the_correct_output_should_be_generated_in_Serenity() {
          csvDatabase.message(csvDatabase.getLineWithMessage());
    }


}
