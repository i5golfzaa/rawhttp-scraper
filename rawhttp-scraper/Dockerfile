FROM findepi/graalvm AS rawhttp-scraper-native

WORKDIR /app
ENV APP_NAME rawhttp-scraper

ADD build/libs/${APP_NAME}.jar /app
ADD graal-reflect-config.json /app

RUN apt-get update && apt-get install -y gcc zlib1g-dev

RUN native-image -jar ${APP_NAME}.jar \
    -H:IncludeResources=META-INF/services/.* \
    -H:ReflectionConfigurationFiles=graal-reflect-config.json \
    --static

# Create minimal exectuable image with the native rawhttp-scraper command

FROM scratch

COPY --from=rawhttp-scraper-native /app/rawhttp-scraper scraper

ENTRYPOINT ["/scraper"]
