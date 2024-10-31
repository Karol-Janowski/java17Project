package pl.zajavka.mortgage.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Map;

public class InputData {

    private static final BigDecimal PERCENT = new BigDecimal("100");

    private LocalDate repaymentStartDate;
    private BigDecimal wiborPercent;
    private BigDecimal amount;
    private BigDecimal monthsDuration;
    private MortgageType rateType;
    private BigDecimal marginPercent;
    private BigDecimal overpaymentProvisionPercent;
    private BigDecimal overpaymentProvisionMonths;
    private BigDecimal overpaymentStartMonth;
    private Map<Integer, BigDecimal> overpaymentSchema;
    private String overpaymentReduceWay;
    private boolean mortgagePrintPayoffsSchedule;
    private Integer mortgageRateNumberToPrint;

    public InputData withRepaymentStartDate(LocalDate repaymentStartDate) {
        this.repaymentStartDate = repaymentStartDate;
        return this;
    }

    public InputData withWiborPercent(BigDecimal wiborPercent) {
        this.wiborPercent = wiborPercent;
        return this;
    }

    public InputData withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public InputData withMonthsDuration(BigDecimal monthsDuration) {
        this.monthsDuration = monthsDuration;
        return this;
    }

    public InputData withType(MortgageType type) {
        this.rateType = type;
        return this;
    }

    public InputData withMarginPercent(BigDecimal marginPercent) {
        this.marginPercent = marginPercent;
        return this;
    }

    public InputData withOverpaymentProvisionPercent(BigDecimal overpaymentProvisionPercent) {
        this.overpaymentProvisionPercent = overpaymentProvisionPercent;
        return this;
    }

    public InputData withOverpaymentProvisionMonths(BigDecimal overpaymentProvisionMonths) {
        this.overpaymentProvisionMonths = overpaymentProvisionMonths;
        return this;
    }

    public InputData withOverpaymentStartMonth(BigDecimal overpaymentStartMonth) {
        this.overpaymentStartMonth = overpaymentStartMonth;
        return this;
    }

    public InputData withOverpaymentSchema(Map<Integer, BigDecimal> overpaymentSchema) {
        this.overpaymentSchema = overpaymentSchema;
        return this;
    }

    public InputData withOverpaymentReduceWay(String overpaymentReduceWay) {
        this.overpaymentReduceWay = overpaymentReduceWay;
        return this;
    }

    public InputData withMortgagePrintPayoffsSchedule(boolean mortgagePrintPayoffsSchedule) {
        this.mortgagePrintPayoffsSchedule = mortgagePrintPayoffsSchedule;
        return this;
    }

    public InputData withMortgageRateNumberToPrint(Integer mortgageRateNumberToPrint) {
        this.mortgageRateNumberToPrint = mortgageRateNumberToPrint;
        return this;
    }

    public LocalDate getRepaymentStartDate() {
        return repaymentStartDate;
    }

    public BigDecimal getWiborPercent() {
        return wiborPercent.divide(PERCENT, 4, RoundingMode.HALF_UP);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getMonthsDuration() {
        return monthsDuration;
    }

    public MortgageType getRateType() {
        return rateType;
    }

    public BigDecimal getOverpaymentStartMonth() {
        return overpaymentStartMonth;
    }

    public Map<Integer, BigDecimal> getOverpaymentSchema() {
        return overpaymentSchema;
    }

    public BigDecimal getMarginPercent() {
        return marginPercent.divide(PERCENT, 4, RoundingMode.HALF_UP);
    }

    public BigDecimal getOverpaymentProvisionPercent() {
        return overpaymentProvisionPercent.divide(PERCENT, 4, RoundingMode.HALF_UP);
    }

    public BigDecimal getOverpaymentProvisionMonths() {
        return overpaymentProvisionMonths;
    }

    public boolean isMortgagePrintPayoffsSchedule() {
        return mortgagePrintPayoffsSchedule;
    }

    public Integer getMortgageRateNumberToPrint() {
        return mortgageRateNumberToPrint;
    }

    public String getOverpaymentReduceWay() {
        return overpaymentReduceWay;
    }

    public BigDecimal getInterestPercent() {
        return getMarginPercent().add(getWiborPercent());
    }

    public BigDecimal getInterestToDisplay() {
        return wiborPercent.add(marginPercent);
    }
}
