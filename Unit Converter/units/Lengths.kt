package converter.units

enum class Lengths {
    M, METER, METERS,
    KM, KILOMETER, KILOMETERS,
    CM, CENTIMETER, CENTIMETERS,
    MM, MILLIMETER, MILLIMETERS,
    MI, MILE, MILES,
    YD, YARD, YARDS,
    FT, FOOT, FEET,
    IN, INCH, INCHES;

    companion object {
        fun isLengthUnit(unit: String): Boolean {
            for (enum in Lengths.values()) {
                if (unit.toUpperCase() == enum.name) return true
            }
            return false
        }
    }
}