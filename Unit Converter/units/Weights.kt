package converter.units

enum class Weights {
    G, GRAM, GRAMS,
    KG, KILOGRAM, KILOGRAMS,
    MG, MILLIGRAM, MILLIGRAMS,
    LB, POUND, POUNDS,
    OZ, OUNCE, OUNCES;

    companion object {
        fun isWeightUnit(unit: String): Boolean {
            for (enum in Weights.values()) {
                if (unit.toUpperCase() == enum.name) return true
            }
            return false
        }
    }
}