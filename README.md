# AutoTrack analysis

- The Scala code is used to download the year, mileage, and price for every Ford and BMW listed on autotrack.nl.
- The `data/` directory contains the raw data and the crunched data in CSV format.
- The `.r` scripts plot the data, fit an exponential model, and make some predictions.

## Results

- If you buy a BMW with 50,000 km and drive it until 100,000 km, it will be worth 9216 euro less.
- If you buy a Ford with 50,000 km and drive it until 100,000 km, it will be worth 3721 euro less.

Driving 50,000 km in a BMW costs you 5495 euro more than driving 50,000 km in a Ford. Better buy a Ford.

## Future Work

- We analysed price as a function of mileage, but other factors (year, model) influence the depreciation as well.

