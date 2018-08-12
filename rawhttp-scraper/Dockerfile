FROM findepi/graalvm AS rawhttp-scraper-native

WORKDIR /app

ADD build/libs/*.jar /app
ADD graal-reflect-config.json /app

RUN apt-get update && apt-get install -y gcc zlib1g-dev

RUN native-image -jar *.jar \
    -H:IncludeResources=META-INF/services/.* \
    -H:ReflectionConfigurationFiles=graal-reflect-config.json \
    --static

# Create minimal exectuable image with the native rawhttp-scraper command

FROM scratch

COPY --from=rawhttp-scraper-native /app/rawhttp-scraper-1.0-SNAPSHOT scraper

ENTRYPOINT ["/scraper"]
