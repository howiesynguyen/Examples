An example of using the following
- HttpServer (JDK)
- JSON In Java (JSON.org)
- Apache HttpClient

The example provides a simple calculator via an HTTP server that accepts JSON requests via the URL http://localhost:8081/calculator

for example, HTTP POST:  {"operator":"SUBTRACTION", "a":1, "b":2}

and then return a JSON response, for example {"result":3.0, "operator":"SUBTRACTION", "a":1.0, "b":2.0}

Try executing JUnit Test class CalculatorTest to see how it runs