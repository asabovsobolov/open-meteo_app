# Open-meteo App

This very simple JavaFX application showcases the use of [Open-meteo API](https://open-meteo.com/en/docs).
It presents charts of archive or forecast data of cities/geo-locations.

## Redis support - *optional*

This application supports caching via Redis that runs on `localhost:6379`

Here is a way to run the redis via docker (docker daemon must be already working):
```
docker run -d --name redis-weather -p 6379:6379 redis
docker start redis-weather
docker stop redis-weather
```

## Building

It can be built with IntelliJ IDEA by going to `Build/Build Artifacts.../Open-meteo App:jar/Build`.

