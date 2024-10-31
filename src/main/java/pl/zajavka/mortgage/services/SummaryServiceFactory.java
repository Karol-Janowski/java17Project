package pl.zajavka.mortgage.services;

import pl.zajavka.mortgage.model.Rate;
import pl.zajavka.mortgage.model.RateAmounts;
import pl.zajavka.mortgage.model.Summary;

import java.math.BigDecimal;
import java.util.List;

public class SummaryServiceFactory {

    public static SummaryService create() {
        return rates -> {
            BigDecimal interestSum = calculate(rates, rate -> rate.getRateAmounts().interestAmount());
            BigDecimal overpaymentProvisionSum = calculate(rates, rate -> rate.getRateAmounts().overpayment().getProvisionAmount());
            BigDecimal totalLostSum = interestSum.add(overpaymentProvisionSum);
            BigDecimal totalCapital = calculate(rates, rate -> totalCapital(rate.getRateAmounts()));
            return new Summary(interestSum, overpaymentProvisionSum, totalLostSum, totalCapital);
        };
    }

    private static BigDecimal totalCapital(final RateAmounts rateAmounts) {
        return rateAmounts.capitalAmount().add(rateAmounts.overpayment().getAmount());
    }

    private static BigDecimal calculate(final List<Rate> rates, Function function) {
        return rates.stream()
            .reduce(BigDecimal.ZERO, (sum, next) -> sum.add(function.calculate(next)), BigDecimal::add);
    }

}
