response = Polyglot.import('response');
UTF8 = Polyglot.import('UTF8');

if (response.getStatusCode() !== 200) {
    throw "Unexpected status code: " + response.getStatusCode();
}

var body = response.getBody().orElse(null);
if (body) {
    var json = JSON.parse(body.decodeBodyToString(UTF8));
    print("Welcome to the " + json.event);
    print("Number of speakers: " + json.speakers.length);
    print(json.speakers.map(describeSpeaker).join("\n"));
} else {
    throw "Response has no body";
}

function describeSpeaker(speaker) {
    return speaker.name + " from " + speaker.org +
        " talks about " + speaker.topic;
}
