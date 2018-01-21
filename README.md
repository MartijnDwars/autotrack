# AutoTrack analysis

- The Scala code is used to download the year, mileage, and price for every Ford and BMW listed on autotrack.nl.
- The `data/` directory contains the raw data and the crunched data in CSV format.
- The `.r` scripts plot the data, fit an exponential model, and make some predictions.

## Results

- If you buy a BMW with 50,000 km and drive it until 100,000 km, it will be worth 9216 euro less.
- If you buy a Ford with 50,000 km and drive it until 100,000 km, it will be worth 3721 euro less.

Driving 50,000 km in a BMW costs you 5495 euro more than driving 50,000 km in a Ford. Better buy a Ford.

## Future Work

- We collected data for _all_ Fords and BMWs that were built in 2010 or later, but some models will loose value more quickly than others. Account for this in the analysis.

