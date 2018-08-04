if (response.statusCode !== 200) {
    throw "Unexpected status code: " + response.statusCode;
}

var body = response.getBody().orElse(null);
if (body) {
    var json = JSON.parse(body.decodeBodyToString(UTF8));
    print("Welcome to " + json.event);
    print("Number of atendees: " + json.atendees.length);
    print(json.atendees.map(function(a) { return a.name + " from " + a.org; }).join(", "));
} else {
    throw "Response has no body";
}
