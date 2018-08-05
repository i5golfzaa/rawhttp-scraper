response = Polyglot.import('response');
UTF8 = Polyglot.import('UTF8');

print(response.getStartLine());
print(response.getHeaders());

var body = response.getBody().orElse(null);
if (body) {
    print(body.decodeBodyToString(UTF8));
}
