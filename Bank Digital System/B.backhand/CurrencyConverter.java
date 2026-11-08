import java.util.HashMap;
import java.util.Map;

public class CurrencyConverter {
    private static final Map<String, Double> exchangeRates = new HashMap<>();

    static {
        exchangeRates.put("MYR", 1.0000);
        exchangeRates.put("USD", 0.2130);
        exchangeRates.put("SGD", 0.2860);
        exchangeRates.put("EUR", 0.1960);
        exchangeRates.put("GBP", 0.1680);
        exchangeRates.put("JPY", 32.5000);
        exchangeRates.put("CNY", 1.5400);
        exchangeRates.put("THB", 7.7500);
        exchangeRates.put("IDR", 3450.0000);
        exchangeRates.put("AUD", 0.3180);
        exchangeRates.put("BND", 0.2860);
        exchangeRates.put("INR", 17.8000);
    }

    public static double convert(double amount, String fromCurrency, String toCurrency) {
        if (amount <= 0) {
            System.out.println(ColorUtil.RED + "[X] Amount must be greater than 0!" + ColorUtil.RESET);
            return -1;
        }

        if (fromCurrency == null || toCurrency == null) {
            System.out.println(ColorUtil.RED + "[X] Currency code cannot be empty!" + ColorUtil.RESET);
            return -1;
        }

        fromCurrency = fromCurrency.trim().toUpperCase();
        toCurrency = toCurrency.trim().toUpperCase();

        if (!exchangeRates.containsKey(fromCurrency) || !exchangeRates.containsKey(toCurrency)) {
            System.out.println(ColorUtil.RED + "[X] Invalid currency code!" + ColorUtil.RESET);
            return -1;
        }

        double amountInMYR = amount / exchangeRates.get(fromCurrency);
        return amountInMYR * exchangeRates.get(toCurrency);
    }

    public static String getAvailableCurrencies() {
        StringBuilder sb = new StringBuilder();
        sb.append(ColorUtil.YELLOW)
          .append("\n")
          .append(StringUtil.createLine('=', 50))
          .append(ColorUtil.RESET);

        sb.append(ColorUtil.CYAN_BOLD)
          .append("\n")
          .append(StringUtil.center("EXCHANGE RATES (1 MYR)", 50))
          .append(ColorUtil.RESET);

        sb.append(ColorUtil.YELLOW)
          .append("\n")
          .append(StringUtil.createLine('=', 50))
          .append(ColorUtil.RESET);

        for (Map.Entry<String, Double> entry : exchangeRates.entrySet()) {
            sb.append(String.format("\n  %-10s : %10.4f %s",
                entry.getKey(), entry.getValue(), entry.getKey()));
        }

        sb.append(ColorUtil.YELLOW)
          .append("\n")
          .append(StringUtil.createLine('=', 50))
          .append(ColorUtil.RESET);

        return sb.toString();
    }

    public static String formatConversion(double amount, String fromCurr, double result, String toCurr) {
        return String.format("%.2f %s = %.2f %s",
            amount,
            fromCurr.toUpperCase(),
            result,
            toCurr.toUpperCase()
        );
    }
}