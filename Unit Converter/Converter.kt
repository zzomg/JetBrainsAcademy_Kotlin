package converter

import converter.units.Lengths
import converter.units.Temperatures
import converter.units.Weights

class Converter {
    var value: Double = 0.0
    var sourceUnit: String = ""
    var targetUnit: String = ""
    var result: Double = 0.0

    val parser = Parser()
    val printer = Printer()

    companion object {
        fun defineSingleOrPlural(value: Double, unit: String, forcePlural: Boolean): String {
            fun singleOrPlural(unit: String): String = if (forcePlural) { unit + "s" } else { if(value == 1.0) unit else unit + "s" }
            return(when(unit) {
                "g", "gram", "grams" -> singleOrPlural("gram")
                "kg", "kilogram", "kilograms" -> singleOrPlural("kilogram")
                "mg", "milligram", "milligrams" -> singleOrPlural("milligram")
                "lb", "pound", "pounds" -> singleOrPlural("pound")
                "oz", "ounce", "ounces" -> singleOrPlural("ounce")
                "m", "meter", "meters" -> singleOrPlural("meter")
                "km", "kilometer", "kilometers" -> singleOrPlural("kilometer")
                "cm", "centimeter", "centimeters" -> singleOrPlural("centimeter")
                "mm", "millimeter", "millimeters" -> singleOrPlural("millimeter")
                "mi", "mile", "miles" -> singleOrPlural("mile")
                "yd", "yard", "yards" -> singleOrPlural("yard")
                "ft", "foot", "feet" -> if (forcePlural) "feet" else { if(value == 1.0) "foot" else "feet" }
                "in", "inch", "inches" -> if (forcePlural) "inches" else { if(value == 1.0) "inch" else "inches" }
                "degreecelsius", "degreescelsius", "celsius", "dc", "c" -> singleOrPlural("degree") + " Celsius"
                "degreefahrenheit", "degreesfahrenheit", "fahrenheit", "df", "f" -> singleOrPlural("degree") + " Fahrenheit"
                "kelvin", "kelvins", "k" -> singleOrPlural("Kelvin")
                else -> "???"
            })
        }
    }

    inner class Parser {
        private fun minInputLenCheck(input: List<String>): Boolean =
                if(input.size < 4) {
                    println("Parse error")
                    false
                } else true

        private fun correctValueCheck(value: String): Boolean {
            for(element in value) {
                if(!(element.isDigit() || element == '.' || element == '-')) {
                    println("Parse error")
                    return false
                }
            }
            return true
        }

        private fun isKnownUnit(unit: String) = if(
                !Weights.isWeightUnit(unit) &&
                !Lengths.isLengthUnit(unit) &&
                !Temperatures.isTemperatureUnit(unit)) "???" else unit

        private fun pickTargetUnit(input: List<String>, pos: Int) =
                if(input[pos] == "degree" || input[pos] == "degrees")
                    (input[pos] + input[pos + 1])
                else input[pos]

        private fun pickSourceUnit(input: List<String>, sourceUnitStartPos: Int, sourceUnitEndPos: Int) =
                if(sourceUnitStartPos == sourceUnitEndPos)
                    input[sourceUnitStartPos]
                else (input[sourceUnitStartPos] + input[sourceUnitEndPos])

        private fun pickSourceUnitPos(input: List<String>): Int =
            if(input[1] == "degree" || input[1] == "degrees") 2 else 1

        private fun nonNegativeUnitCheck(unit: String, value: Double): Boolean =
            if (Weights.isWeightUnit(unit) && value < 0) {
                println("Weight shouldn't be negative")
                false
            }
            else if (Lengths.isLengthUnit(unit) && value < 0) {
                println("Length shouldn't be negative")
                false
            }
            else true

        private fun possibleConversionCheck(value: Double, sourceUnit: String, targetUnit: String): Boolean =
            if ((Weights.isWeightUnit(sourceUnit) && !Weights.isWeightUnit(targetUnit))
                    || (Lengths.isLengthUnit(sourceUnit) && !Lengths.isLengthUnit(targetUnit))
                    || (Temperatures.isTemperatureUnit(sourceUnit) && !Temperatures.isTemperatureUnit(targetUnit))
                    || (sourceUnit == "???") || (targetUnit == "???")
            ) {
                println("Conversion from ${defineSingleOrPlural(value, sourceUnit, true)} to " +
                        "${defineSingleOrPlural(value, targetUnit, true)} is impossible")
                false
            } else true

        fun parseInput(input: String): Int {
            val valuePos = 0
            val sourceUnitStartPos = 1
            val sourceUnitEndPos: Int
            val randomWordPos: Int
            val targetUnitStartPos: Int

            val inputTokenized: List<String> = input.split(" ")

            // pre-check : input length at least 4 words
            if (!minInputLenCheck(inputTokenized)) return -1

            // checking if input contains value to convert
            val valueStr: String = inputTokenized[valuePos]
            if (!correctValueCheck(valueStr)) return -1
            this@Converter.value = valueStr.toDouble()

            // checking if units are correct
            // skipping "in"/"to"/etc.
            sourceUnitEndPos = pickSourceUnitPos(inputTokenized)
            randomWordPos = sourceUnitEndPos + 1
            targetUnitStartPos = randomWordPos + 1

            this@Converter.sourceUnit = isKnownUnit(pickSourceUnit(inputTokenized, sourceUnitStartPos, sourceUnitEndPos))
            this@Converter.targetUnit = isKnownUnit(pickTargetUnit(inputTokenized, targetUnitStartPos))

            if (!nonNegativeUnitCheck(this@Converter.sourceUnit, this@Converter.value)) return -1

            if (!possibleConversionCheck(this@Converter.value, this@Converter.sourceUnit, this@Converter.targetUnit)) return -1

            return 0
        }
    }

    inner class Printer {
        private fun printConverted(value: Double, sourceUnit: String, result: Double, targetUnit: String) {
            println("$value $sourceUnit is $result $targetUnit")
        }

        fun prettyPrintResult() {
            val sourceUnitToPrint: String = defineSingleOrPlural(value, sourceUnit, false)
            val targetUnitToPrint: String = defineSingleOrPlural(result, targetUnit, false)
            printConverted(value, sourceUnitToPrint, result, targetUnitToPrint)
        }
    }

    private fun fromGramsToTarget(value: Double, targetUnit: String): Double {
        var newValue = 0.0
        when(targetUnit) {
            "g", "gram", "grams" -> newValue = value / 1
            "kg", "kilogram", "kilograms" -> newValue = value / 1000
            "mg", "milligram", "milligrams" -> newValue = value / 0.001
            "lb", "pound", "pounds" -> newValue = value / 453.592
            "oz", "ounce", "ounces" -> newValue = value / 28.3495
        }
        return newValue
    }

    private fun toGrams(value: Double, sourceUnit: String): Double {
        var newValue = 0.0
        when(sourceUnit) {
            "g", "gram", "grams" -> newValue = value * 1
            "kg", "kilogram", "kilograms" -> newValue = value * 1000
            "mg", "milligram", "milligrams" -> newValue = value * 0.001
            "lb", "pound", "pounds" -> newValue = value * 453.592
            "oz", "ounce", "ounces" -> newValue = value * 28.3495
        }
        return newValue
    }

    private fun fromMetersToTarget(value: Double, targetUnit: String): Double {
        var newValue = 0.0
        when(targetUnit) {
            "m", "meter", "meters" -> newValue = value / 1
            "km", "kilometer", "kilometers" -> newValue = value / 1000
            "cm", "centimeter", "centimeters" -> newValue = value / 0.01
            "mm", "millimeter", "millimeters" -> newValue = value / 0.001
            "mi", "mile", "miles" -> newValue = value / 1609.35
            "yd", "yard", "yards" -> newValue = value / 0.9144
            "ft", "foot", "feet" -> newValue = value / 0.3048
            "in", "inch", "inches" -> newValue = value / 0.0254
        }
        return newValue
    }

    private fun toMeters(value: Double, sourceUnit: String): Double {
        var newValue = 0.0
        when(sourceUnit) {
            "m", "meter", "meters" -> newValue = value * 1
            "km", "kilometer", "kilometers" -> newValue = value * 1000
            "cm", "centimeter", "centimeters" -> newValue = value * 0.01
            "mm", "millimeter", "millimeters" -> newValue = value * 0.001
            "mi", "mile", "miles" -> newValue = value * 1609.35
            "yd", "yard", "yards" -> newValue = value * 0.9144
            "ft", "foot", "feet" -> newValue = value * 0.3048
            "in", "inch", "inches" -> newValue = value * 0.0254
        }
        return newValue
    }

    private fun fromCelsiusToTarget(value: Double, targetUnit: String): Double {
        var newValue = 0.0
        when(targetUnit) {
            "degreecelsius", "degreescelsius", "celsius", "dc", "c" -> newValue = value
            "degreefahrenheit", "degreesfahrenheit", "fahrenheit", "df", "f" -> newValue = value * 9 / 5 + 32
            "kelvin", "kelvins", "k" -> newValue = value + 273.15
        }
        return newValue
    }

    private fun toCelsius(value: Double, sourceUnit: String): Double {
        var newValue = 0.0
        when(sourceUnit) {
            "degreecelsius", "degreescelsius", "celsius", "dc", "c" -> newValue = value
            "degreefahrenheit", "degreesfahrenheit", "fahrenheit", "df", "f" -> newValue = (value - 32) * 5 / 9
            "kelvin", "kelvins", "k" -> newValue = value - 273.15
        }
        return newValue
    }

    private fun convertWeights(value: Double, sourceUnit: String, targetUnit: String): Double {
        val result: Double = toGrams(value, sourceUnit)
        return fromGramsToTarget(result, targetUnit)
    }

    private fun convertLength(value: Double, sourceUnit: String, targetUnit: String): Double {
        val result: Double = toMeters(value, sourceUnit)
        return fromMetersToTarget(result, targetUnit)
    }

    private fun convertTemperature(value: Double, sourceUnit: String, targetUnit: String): Double {
        val result: Double = toCelsius(value, sourceUnit)
        return fromCelsiusToTarget(result, targetUnit)
    }

    fun convert() {
        when {
            Weights.isWeightUnit(this.sourceUnit) -> {
                result = convertWeights(value, sourceUnit, targetUnit)
            }
            Lengths.isLengthUnit(this.sourceUnit) -> {
                result = convertLength(value, sourceUnit, targetUnit)
            }
            Temperatures.isTemperatureUnit(this.sourceUnit) -> {
                result = convertTemperature(value, sourceUnit, targetUnit)
            }
        }
    }
}