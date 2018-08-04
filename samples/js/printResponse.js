print(response.startLine);
print(response.headers);

var body = response.getBody().orElse(null);
if (body) {
    print(body.decodeBodyToString(UTF8));
}
