import com.typesafe.scalalogging.LazyLogging
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.model.Element

object Main extends LazyLogging {
  val brandMapping = Map(
    "BMW" -> "1a67a3d8-178b-43ee-9071-9ae7f19b316a",
    "Ford" -> "44b04cbf-5eae-4538-b55a-b126f4b20318",
    "Peugeot" -> "1b83b9d6-c1c1-4ceb-ae38-d720f21ad741",
    "Opel" -> "7ccf5430-eafb-4042-82c0-43ce39ba1b02",
  )

  val modelToBrandMapping = Map(
    "Agila" -> "Opel",
    "107" -> "Peugeot",
  )

  val modelMapping = Map(
    "Agila" -> "e0f373aa-6be1-4154-aeac-5d754cc4d2a9",
    "107" -> "ba9ddf1e-6973-40a2-ba0d-ddcc4e2ce024",
  )

  def main(args: Array[String]): Unit = {
    val configuration = Configuration(
      page = 1,
      pageSize = 100,
      brands = List("Peugeot"),
      models = List("107")
    )

    val cars = (1 to 1).flatMap(page => {
      logger.debug(s"Get page $page")

      getPage(configuration.copy(page = page))
    })

    for (car <- cars) {
      println(car)
    }
  }

  def getPage(configuration: Configuration): List[Car] = {
    val url = getUrl(configuration)
    println(url)
    val browser = JsoupBrowser()
    val catalog = browser.get(url)
    val articles = catalog >> elementList("article")
    val cars = articles.map(extractCar)

    cars
  }

  def extractCar(article: Element): Car = {
    val link = article >> element("a.result-item__bottom-layer") >> attr("href")

    val priceText = article >> element("data.result-item__price") >> attr("value")
    val price = priceText.toInt

    val yearText = article >> element("span[itemprop=productionDate]") >> text
    val year = yearText.toInt

    val mileageText = article >> element("meta[itemprop=mileageFromOdometer]") >> attr("content")
    val mileage = mileageText.toInt

    Car(link, year, mileage, price)
  }

  def getUrl(configuration: Configuration): String = {
    val brandIds = configuration.brands.map(brandMapping.apply)
    val brands = brandIds.mkString(",")

    // TODO: Assumes one brand and one model
    val modelIds = configuration.models.map(modelMapping.apply)
    val models = s"${brandIds(0)}=${modelIds(0)}"

    s"https://www.autotrack.nl/aanbod" +
      s"?policy=accepted-20190101" +
      s"&paginagrootte=${configuration.pageSize}" +
      s"&postcode=" +
      s"&afstand=5" +
      s"&merkIds=" + brands +
      s"&modelIds." + models +
      s"&minimumprijs=" +
      s"&maximumprijs=99999" +
      s"&minimumbouwjaar=" +
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
