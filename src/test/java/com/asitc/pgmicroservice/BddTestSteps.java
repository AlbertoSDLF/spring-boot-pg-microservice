package com.asitc.pgmicroservice;

import org.springframework.http.ResponseEntity;

import com.asitc.pgmicroservice.controller.user.UserDTO;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class BddTestSteps extends BddTest {

    private ResponseEntity<UserDTO> userResponseEntity;

    @When("the client calls GET \\/api\\/user\\/{int}")
    public void the_client_calls_GET_api_user(final Integer int1) {
        throw new cucumber.api.PendingException();
    }

    @When("the request is missing the Content-Type header with value {string}")
    public void the_request_is_missing_the_Content_Type_header_with_value(final String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Then("the client receives response status code of {int}")
    public void the_client_receives_response_status_code_of(final Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Then("the response body is customised to show statusCode {int} and statusMessage {string}")
    public void the_response_body_is_customised_to_show_statusCode_and_statusMessage(final Integer int1,
            final String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @When("the request includes both the Accept and the Content-Type header with value {string}")
    public void the_request_includes_both_the_Accept_and_the_Content_Type_header_with_value(final String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }

    @Then("the response body contains an id equal to {int}")
    public void the_response_body_contains_an_id_equal_to(final Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        throw new cucumber.api.PendingException();
    }
    // @When("the client calls GET \\/api\\/user\\/{int}")
    // public void the_client_calls_GET_api_user(final Integer id) {
    // this.userResponseEntity = super.getUserById(new Long(id));
    // }
    //
    // @Then("the client receives response status code of {int}")
    // public void the_client_receives_response_status_code_of(final Integer
    // statusCode) {
    // Assertions.assertThat(this.userResponseEntity.getStatusCodeValue()).isEqualTo(statusCode);
    // }

}
