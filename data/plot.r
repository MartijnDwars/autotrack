setwd("/Users/martijn/Projects/autotrack/data")

## First, we look at BMW

# Import BMW data from .csv file (header: ID, Year, Mileage, Price)
data = read.csv("bmw.csv", header = TRUE, colClasses=c("character", "numeric", "numeric", "numeric"))

# Filter those with price = -1 or mileage = -1
data = data[data$Price != -1, ]
data = data[data$Mileage != -1, ]

# Plot points (p) mileage (x-axis) against price (y-axis)
plot(data[, 3], data[, 4], xlab = "Mileage", ylab = "Price", type = "p", main = "BMW Occassion: Price vs. Mileage")

# Fit linear model (bad model)
#fit = lm(Price ~ Mileage, data)
#lines(data$Mileage, fit$coefficients[1] + data$Mileage * fit$coefficients[2], col = "red")

# Fit exponential model
fit = lm(log(Price) ~ Mileage, data)
mileage <- seq(0, 350000, 1000)
price <- exp(predict(fit, list(Mileage=mileage)))
lines(mileage, price, col = "red")

# What is the price for mileage = 50,000? And 100,000?
exp(predict(fit, list(Mileage=50000)))
exp(predict(fit, list(Mileage=100000)))

## Now, we look at Ford

# Import Ford data from .csv file (header: ID, Year, Mileage, Price)
data = read.csv("ford.csv", header = TRUE, colClasses=c("character", "numeric", "numeric", "numeric"))

# Remove those with price <= 0 or mileage <= 0
data = data[data$Price > 0, ]
data = data[data$Mileage > 0, ]

# Remove outliers (invalid data)
data = data[data$Mileage < 600000, ]

# Plot points (p) mileage (x-axis) against price (y-axis)
plot(data[, 3], data[, 4], xlab = "Mileage", ylab = "Price", type = "p", main = "Ford Occassion: Price vs. Mileage")

# Fit linear model (bad model)
#fit = lm(Price ~ Mileage, data)
#lines(data$Mileage, fit$coefficients[1] + data$Mileage * fit$coefficients[2], col = "red")

# Fit exponential model: a * Mileage + b = log(Price)
# a = -5.477673e-06
# b = 9.924531e
# Price = e^(a * Mileage + b)
fit = lm(log(Price) ~ Mileage, data)
mileage <- seq(0, 350000, 1000)
price <- exp(predict(fit, list(Mileage=mileage)))
lines(mileage, price, col = "red")

# What is the price for mileage = 100,000? (Answer: 26,442)
price <- exp(predict(fit, list(Mileage=100000)))
