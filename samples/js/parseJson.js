if (response.statusCode !== 200) {
    throw "Unexpected status code: " + response.statusCode;
}

var body = response.body;
if (body) {
    var json = JSON.parse(body);
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
