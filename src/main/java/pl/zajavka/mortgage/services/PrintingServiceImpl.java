package pl.zajavka.mortgage.services;

import pl.zajavka.mortgage.model.InputData;
import pl.zajavka.mortgage.model.Overpayment;
import pl.zajavka.mortgage.model.Rate;
import pl.zajavka.mortgage.model.Summary;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PrintingServiceImpl implements PrintingService {

    private static final String SEPARATOR = createSeparator('-', 180);

    @SuppressWarnings("SameParameterValue")
    private static String createSeparator(char sign, int length) {
        return String.valueOf(sign).repeat(Math.max(0, length));
    }

    @Override
    public void printIntroInformation(InputData inputData) {
        StringBuilder msg = new StringBuilder(NEW_LINE);
        msg.append(MORTGAGE_AMOUNT).append(inputData.getAmount()).append(CURRENCY);
        msg.append(NEW_LINE);
        msg.append(MORTGAGE_PERIOD).append(inputData.getMonthsDuration()).append(MONTHS);
        msg.append(NEW_LINE);
        msg.append(INTEREST).append(inputData.getInterestToDisplay()).append(PERCENT);
        msg.append(NEW_LINE);
        msg.append(OVERPAYMENT_START_MONTH).append(inputData.getOverpaymentStartMonth()).append(BLANK).append(MONTH);

        Optional.of(inputData.getOverpaymentSchema())
            .filter(schema -> schema.size() > 0)
            .ifPresent(schema -> logOverpayment(msg, inputData.getOverpaymentSchema(), inputData.getOverpaymentReduceWay()));

        log(msg.toString());
    }

    private void logOverpayment(final StringBuilder msg, final Map<Integer, BigDecimal> schema, final String reduceWay) {
        switch (reduceWay) {
            case Overpayment.REDUCE_PERIOD:
                msg.append(OVERPAYMENT_REDUCE_PERIOD);
                break;
            case Overpayment.REDUCE_RATE:
                msg.append(OVERPAYMENT_REDUCE_RATE);
                break;
            default:
                throw new MortgageException("Case not handled");
        }
        msg.append(NEW_LINE);
        msg.append(OVERPAYMENT_FREQUENCY).append(NEW_LINE).append(prettyPrintOverpaymentSchema(schema));
    }

    private String prettyPrintOverpaymentSchema(Map<Integer, BigDecimal> schema) {
        StringBuilder schemaMsg = new StringBuilder();
        for (Map.Entry<Integer, BigDecimal> entry : schema.entrySet()) {
            schemaMsg.append(MONTH)
                .append(entry.getKey())
                .append(COMMA)
                .append(AMOUNT)
                .append(entry.getValue())
                .append(CURRENCY)
                .append(NEW_LINE);
        }
        return schemaMsg.toString();
    }

    @Override
    public void printSchedule(final List<Rate> rates, final InputData inputData) {
        if (!inputData.isMortgagePrintPayoffsSchedule()) {
            return;
        }

        int index = 1;
        for (Rate rate : rates) {
            if (rate.rateNumber().remainder(BigDecimal.valueOf(inputData.getMortgageRateNumberToPrint())).equals(BigDecimal.ZERO)) {
                String message = String.format(SCHEDULE_TABLE_FORMAT,
                    RATE_NUMBER, rate.rateNumber(),
                    YEAR, rate.timePoint().year(),
                    MONTH, rate.timePoint().month(),
                    DATE, rate.timePoint().date(),
                    RATE, rate.rateAmounts().rateAmount(),
                    INTEREST, rate.rateAmounts().interestAmount(),
                    CAPITAL, rate.rateAmounts().capitalAmount(),
                    OVERPAYMENT, rate.rateAmounts().overpayment().amount(),
                    LEFT_AMOUNT, rate.mortgageResidual().getResidualAmount(),
                    LEFT_MONTHS, rate.mortgageResidual().getResidualDuration()
                );
                log(message);
                if (index % AmountsCalculationService.YEAR.intValue() == 0) {
                    log(SEPARATOR);
                }
                index++;
            }
        }
        log(NEW_LINE);
    }

    @Override
    public void printSummary(final Summary summary) {
        String msg = INTEREST_SUM + summary.interestSum() + CURRENCY +
            NEW_LINE +
            OVERPAYMENT_PROVISION + summary.overpaymentProvisionSum().setScale(2, RoundingMode.HALF_UP) + CURRENCY +
            NEW_LINE +
            LOSTS_SUM + summary.totalLostSum().setScale(2, RoundingMode.HALF_UP) + CURRENCY +
            NEW_LINE +
            CAPITAL_SUM + summary.totalCapital().setScale(2, RoundingMode.HALF_UP) + CURRENCY +
            NEW_LINE;

        log(msg);
    }

    private void log(String message) {
        System.out.println(message);
    }

}
