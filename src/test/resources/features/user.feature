Feature: Users can be managed via /user endpoints
  Scenario: 400 when client makes call to retrieve an existing user
    When the client calls GET /api/user/1
    And the request is missing the Content-Type header with value "application/json"
	Then the client receives response status code of 400
	And the response body is customised to show statusCode 400 and statusMessage "Bad Request"

  Scenario: client makes call to retrieve an existing user
    When the client calls GET /api/user/1
    And the request includes both the Accept and the Content-Type header with value "application/json"
	Then the client receives response status code of 200
	And the response body contains an id equal to 1
