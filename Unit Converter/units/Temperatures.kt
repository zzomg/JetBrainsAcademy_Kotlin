package converter.units

enum class Temperatures {
    DEGREECELSIUS, DEGREESCELSIUS, CELSIUS, DC, C,
    DEGREEFAHRENHEIT, DEGREESFAHRENHEIT, FAHRENHEIT, DF, F,
    KELVIN, KELVINS, K;

    companion object {
        fun isTemperatureUnit(unit: String): Boolean {
            for (enum in Temperatures.values()) {
                if (unit.toUpperCase() == enum.name) return true
            }
            return false
        }
    }
}