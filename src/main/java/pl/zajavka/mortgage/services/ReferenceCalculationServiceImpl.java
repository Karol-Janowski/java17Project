package pl.zajavka.mortgage.services;

import pl.zajavka.mortgage.model.*;

import java.math.BigDecimal;

public class ReferenceCalculationServiceImpl implements ReferenceCalculationService {

    @Override
    public MortgageReference calculate(RateAmounts rateAmounts, InputData inputData) {
        if (BigDecimal.ZERO.equals(inputData.getAmount())) {
            return new MortgageReference(BigDecimal.ZERO, BigDecimal.ZERO);
        }

        return new MortgageReference(inputData.getAmount(), inputData.getMonthsDuration());
    }

    @Override
    public MortgageReference calculate(RateAmounts rateAmounts, final InputData inputData, Rate previousRate) {
        if (BigDecimal.ZERO.equals(previousRate.mortgageResidual().residualAmount())) {
            return new MortgageReference(BigDecimal.ZERO, BigDecimal.ZERO);
        }

        switch (inputData.getOverpaymentReduceWay()) {
            case Overpayment.REDUCE_RATE:
                return reduceRateMortgageReference(rateAmounts, previousRate.mortgageResidual());
            case Overpayment.REDUCE_PERIOD:
                return new MortgageReference(inputData.getAmount(), inputData.getMonthsDuration());
            default:
                throw new MortgageException("Case not handled");
        }

    }

    private MortgageReference reduceRateMortgageReference(final RateAmounts rateAmounts, final MortgageResidual previousResidual) {
        if (rateAmounts.overpayment().amount().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal residualAmount = calculateResidualAmount(previousResidual.residualAmount(), rateAmounts);
            return new MortgageReference(residualAmount, previousResidual.residualDuration().subtract(BigDecimal.ONE));
        }

        return new MortgageReference(previousResidual.residualAmount(), previousResidual.residualDuration());
    }

    private BigDecimal calculateResidualAmount(final BigDecimal residualAmount, final RateAmounts rateAmounts) {
        return residualAmount
            .subtract(rateAmounts.capitalAmount())
            .subtract(rateAmounts.overpayment().amount())
            .max(BigDecimal.ZERO);
    }

}
