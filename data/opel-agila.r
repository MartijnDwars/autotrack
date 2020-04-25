setwd("/Users/martijn/Projects/autotrack/data")

# Import Opel Agila data from .csv file (header: id, year, mileage, price)
data = read.csv("opel-agila.csv", header=TRUE, colClasses=c("character", "numeric", "numeric", "numeric"))
attach(data)
names(data)

# Filter on 2009 model
#data2009 = data[data$year == 2009, ]

# Plot points (p) mileage (x-axis) against price (y-axis)
plot(data[, 3], data[, 4], xlab="Mileage", ylab="Price", type="p", main="Opel Agila Occassion: Price vs. Mileage")

# Fit exponential model
fit = lm(log(price) ~ mileage, data)
mileage <- seq(0, 290000, 1000)
price <- exp(predict(fit, list(Mileage=mileage)))
lines(mileage, price, col = "red")
