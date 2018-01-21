import com.typesafe.scalalogging.LazyLogging
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.model.Element

object Main extends LazyLogging {
  val brandMapping = Map(
    "BMW" -> "1a67a3d8-178b-43ee-9071-9ae7f19b316a",
    "Ford" -> "44b04cbf-5eae-4538-b55a-b126f4b20318"
  )

  def main(args: Array[String]): Unit = {
    val configuration = Configuration(
      page = 1,
      pageSize = 100,
      brands = List("Ford"),
      minimumYear = 2010
    )

    val cars = (61 to 83).flatMap(page => {
      logger.debug(s"Get page $page")

      getPage(configuration.copy(page = page))
    })

    for (car <- cars) {
      println(car)
    }
  }

  def getPage(configuration: Configuration): List[Car] = {
    val url = getUrl(configuration)
    val browser = JsoupBrowser()
    val catalog = browser.get(url)
    val articles = catalog >> elementList("article")
    val cars = articles.map(extractCar)

    cars
  }

  def extractCar(article: Element): Car = {
    val link = article >> element("a.searchresult__link") >> attr("href")

    val priceText = article >> element("span.searchresult__price") >> allText
    val price = extractPrice(priceText)

    val summaryElements = article >> elementList("ul.searchresult__summary li")
    val mileageText = summaryElements(0) >> text
    val mileage = extractMileage(mileageText)

    val yearText = summaryElements(1) >> text
    val year = extractYear(yearText)

    Car(link, year, mileage, price)
  }

  def extractPrice(priceText: String): Int = {
    if (priceText == "Prijs op aanvraag") {
      -1
    } else {
      if (priceText.endsWith(" (Ex. BTW)")) {
        priceText.substring(2, priceText.length-10).replace(".", "").toInt
      } else {
        priceText.substring(2).replace(".", "").toInt
      }
    }
  }

  def extractMileage(mileageText: String): Int = {
    if (mileageText == "-") {
      -1
    } else {
      mileageText.substring(0, mileageText.length - 3).replace(".", "").toInt
    }
  }

  def extractYear(yearText: String): Int = {
    yearText.toInt
  }

  def getUrl(configuration: Configuration): String = {
    val brandIds = configuration.brands.map(brandMapping.apply)
    val brands = brandIds.mkString(",")

    s"https://www.autotrack.nl/tweedehands" +
      s"?policy=accepted" +
      s"&autosoorten=OCCASION" +
      s"&sortering=" +
      s"&paginagrootte=${configuration.pageSize}" +
      s"&postcode=" +
      s"&afstand=5" +
      s"&merkIds=" + brands +
      s"&minimumprijs=" +
      s"&maximumprijs=" +
      s"&minimumbouwjaar=${configuration.minimumYear}" +
      s"&maximumbouwjaar=" +
      s"&minimumkilometerstand=" +
      s"&maximumkilometerstand=" +
      s"&trefwoord=" +
      s"&aanbodSinds=" +
      s"&at-switch=on" +
      s"&minimaleMotorinhoud=" +
      s"&maximaleMotorinhoud=" +
      s"&paginanummer=${configuration.page}"
  }
}

// Hypothesis: BMWs are more expensive to drive than Fords. Specifically, BMWs loose more value per time/distance than Fords.