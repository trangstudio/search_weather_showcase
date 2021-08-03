Feature: Search Weather function
  As a Customer I can search weather of a city


  @Api
    @SmokeTest
  Scenario Outline: Call weather data for one location - Search weather by city name
    Given User search weather by "<city_name>" with API
    Then User receive result by "city name" with "<status_code>" and "<response_city_name>" with API
    Examples:
      | city_name    | status_code | response_city_name |
      | Ho Chi Minh  | 200         | Ho Chi Minh City   |
      | Tokyo        | 200         | Tokyo              |

  @Api
    @SmokeTest
  Scenario Outline: Call weather data for one location - Search weather by city name & state code
    Given User search weather by "<city_name>" and "<state_code>" with API
    Then User receive result by "city name" with "<status_code>" and "<response_city_name>" with API
    Examples:
      | city_name             | status_code | state_code | response_city_name |
      | West Orange           | 200         | 5106298    | West Orange        |
      | Woodland Park         | 200         | 5444689    | Woodland Park      |
      | Thanh pho Ho Chi Minh | 200         | 1566083    | Ho Chi Minh City   |

  @Api
    @SmokeTest
  Scenario Outline: Call weather data for one location - Search weather by city name & state code & country code
    Given User search weather by "<city_name>" and "<state_code>" and "<country_code>" with API
    Then User receive result by "city name" with "<status_code>" and "<response_city_name>" with API
    Examples:
      | city_name             | status_code | state_code | country_code | response_city_name |
      | West Orange           | 200         | 5106298    | US           | West Orange        |
      | Woodland Park         | 200         | 5106303    | US           | Woodland Park      |
      | Thanh pho Ho Chi Minh | 200         | 1566083    | VN           | Ho Chi Minh City   |

  @Api
    @SmokeTest
  Scenario Outline: Call weather data for one location - Search weather by city ID
    Given User search weather by city ID "<city_id>" with API
    Then User receive result by "city id" with "<status_code>" and "<response_city_name>" with API
    Examples:
      | city_id | status_code | response_city_name |
      | 1566053 | 200         | Thanh Son          |
      | 1565022 | 200         | Thu Dau Mot        |

  @Api
    @SmokeTest
  Scenario Outline: Call weather data for one location - Search weather by geographic coordinates
    Given User search weather by geographic coordinates "<lat>" and "<lon>" with API
    Then User receive result by "geographic coordinates" with "<status_code>" and "<response_city_name>" with API
    Examples:
      | lat     | lon     | status_code | response_city_name |
      | 35      | 139     | 200         | Shuzenji           |
      | 40.7143 | -74.006 | 200         | New York           |

  @Api
    @SmokeTest
  Scenario Outline: Call weather data for one location - Search weather by Zip code
    Given User search weather by zip code "<zip_code>" and "<country_code>" with API
    Then User receive result by "zip code" with "<status_code>" and "<response_city_name>" with API
    Examples:
      | zip_code | status_code | country_code | response_city_name |
      | 94040    | 200         | us           | Mountain View      |

  @Api
    @SmokeTest
  Scenario Outline: Call weather data for several cities - Search weather within a rectangle zone
    Given User search weather by rectangle zone "<rectangle_zone>" with API
    Then User receive result by "rectangle zone" for several cities with "<status_code>" with API
    Examples:
      | rectangle_zone | status_code |
      | 12,32,15,37,10 | 200         |

  @Api
    @SmokeTest
  Scenario Outline: Call weather data for several cities - Search weather within Cities in circle
    Given User search weather by circle with  "<lat>" and "<lon>" and "<number_of_cities>" with API
    Then User receive result by "circle" for several cities with "<status_code>" with API
    Examples:
      | lat  | lon  | status_code | number_of_cities |
      | 55.5 | 37.5 | 200         | 10               |


