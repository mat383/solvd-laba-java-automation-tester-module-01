package com.solvd.laba.homework02.exercise01;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

public class ContractPerHour extends Contract {
    private BigDecimal consultationCost;
    private BigDecimal courtHearingCost;
    private BigDecimal researchCost;
    private BigDecimal legalAdviceCost;

    public ContractPerHour(String description,
                           BigDecimal consultationCost,
                           BigDecimal courtHearingCost,
                           BigDecimal researchCost,
                           BigDecimal legalAdviceCost) {
        super(description);
        this.consultationCost = consultationCost;
        this.courtHearingCost = courtHearingCost;
        this.researchCost = researchCost;
        this.legalAdviceCost = legalAdviceCost;
    }

    public ContractPerHour(BigDecimal consultationCost,
                           BigDecimal courtHearingCost,
                           BigDecimal researchCost,
                           BigDecimal legalAdviceCost) {
        this.consultationCost = consultationCost;
        this.courtHearingCost = courtHearingCost;
        this.researchCost = researchCost;
        this.legalAdviceCost = legalAdviceCost;
    }

    public BigDecimal getConsultationCost() {
        return consultationCost;
    }

    public void setConsultationCost(BigDecimal consultationCost) {
        this.consultationCost = consultationCost;
    }

    public BigDecimal getCourtHearingCost() {
        return courtHearingCost;
    }

    public void setCourtHearingCost(BigDecimal courtHearingCost) {
        this.courtHearingCost = courtHearingCost;
    }

    public BigDecimal getResearchCost() {
        return researchCost;
    }

    public void setResearchCost(BigDecimal researchCost) {
        this.researchCost = researchCost;
    }

    public BigDecimal getLegalAdviceCost() {
        return legalAdviceCost;
    }

    public void setLegalAdviceCost(BigDecimal legalAdviceCost) {
        this.legalAdviceCost = legalAdviceCost;
    }

    @Override
    public BigDecimal calculateTotalPrice(LegalCase legalCase) {
        BigDecimal owned = new BigDecimal("0");

        for (LegalService service : legalCase.getServices()) {
            owned = owned.add(calculateServicePrice(service));
        }
        return owned;
    }

    private BigDecimal calculateServicePrice(LegalService service) {
        BigDecimal price = null;

        Duration serviceDuration = service.getTimeSpan().duration();
        // convert duration in seconds to duration in hours
        BigDecimal durationHours = BigDecimal.valueOf(serviceDuration.toSeconds())
                .divide(BigDecimal.valueOf(3600), RoundingMode.HALF_UP);

        BigDecimal serviceCostModifier = BigDecimal.valueOf(service.getComplexity())
                .multiply(BigDecimal.valueOf(service.getPriceModifier()));

        switch (service.getType()) {
            case CONSULTATION:
                price = this.consultationCost
                        .multiply(durationHours)
                        .multiply(serviceCostModifier);
                break;
            case COURT_HEARING:
                price = this.courtHearingCost
                        .multiply(durationHours)
                        .multiply(serviceCostModifier);
                break;
            case RESEARCH:
                price = this.researchCost
                        .multiply(durationHours)
                        .multiply(serviceCostModifier);
                break;
            case LEGAL_ADVICE:
                price = this.legalAdviceCost
                        .multiply(durationHours)
                        .multiply(serviceCostModifier);
                break;
        }

        return price;
    }
}
