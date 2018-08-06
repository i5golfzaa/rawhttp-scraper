# RawHTTP Scraper

A simple Web scraper based on [RawHTTP](https://github.com/renatoathaydes/rawhttp).

## Usage

```
java -jar rawhttp-scraper.jar <request-file> <js-script>
```

`<request-file>` should be a file containing a raw HTTP request.

`<js-script>` is a file containing a JavaScript script. The JS script has access to a `response` object
which can be used to print the desired output from the HTTP response.

See [samples/js](samples/js) for examples.

For examples of request files, see the [samples/requests](samples/requests) directory.

## Building


Create a runnable jar:

```
./gradlew jar
```

Create a native executable:

```
./gradlew native
```

## Testing

```
./gradlew check
```

## Running

To run the jar:

```
java -jar build/libs/rawhttp-scraper-<version>.jar <request-file> <js-script>
```

To run the executable:

```
./rawhttp-scraper<version> <request-file> <js-script>
```

Example:

```
./rawhttp-scraper<version> samples/requests/get-speakers samples/js/parseJson.js
```
