package io.builders.demo.dtcc.application.dlt

class FormatDecimals {

    private static final BigDecimal TENELEVATEDTWO = 10 ** TWO
    private static final Integer TWO = 2

    static BigInteger toBigInteger(BigDecimal number) {
        return number * TENELEVATEDTWO
    }

    static BigDecimal toBigDecimal(BigInteger number) {
        return number / TENELEVATEDTWO
    }

}
