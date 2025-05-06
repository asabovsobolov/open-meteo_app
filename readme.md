# Open-meteo App

This very simple JavaFX application showcases the use of [Open-meteo API](https://open-meteo.com/en/docs).
It presents charts of archive or forecast data of cities/geo-locations.

## Requirements

Java `>=21.0.0` - recommended, as this was the version I aimed for.

## Redis support - *optional*

This application supports caching via Redis that runs on `localhost:6379`

Here is a way to run the Redis via docker (docker daemon must be already working):
```
docker run -d --name redis-weather -p 6379:6379 redis
docker start redis-weather
```

Stopping Redis:
```
docker stop redis-weather
```

## Building

```
mvn clean package
```
The output `.jar` file should be in `target` folder.

## Credits

- [/src/main/resources/open_meteo_app/worldcities.csv](https://simplemaps.com/data/world-cities)
