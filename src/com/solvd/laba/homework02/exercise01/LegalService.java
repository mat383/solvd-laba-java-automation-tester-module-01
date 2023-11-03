package com.solvd.laba.homework02.exercise01;

public class LegalService {
    public enum Type {
        CONSULTATION,
        COURT_HEARING,
        RESEARCH,
        /**
         * legal advice without meeting
         * i.e. responding to email question
         */
        LEGAL_ADVICE
    }

    LegalService.Type type;
    TimeSpan timeSpan;
    /**
     * more precise description of service
     */
    String description;
    /**
     * additional information about service
     */
    String annotation;
    /**
     * how complex service is (could be used as multiplier when pricing).
     * default is 1, lesser the value lesser the complexity
     * should be 0 < complexity < infinity
     */
    double complexity = 1.;
    /**
     * how to modify price of service, usage depends on pricing algorithm
     * default is 1, lesser the value lesser the complexity
     * should be 0 < priceModifier < infinity
     */
    double priceModifier = 1.;

    public LegalService(LegalService.Type type, TimeSpan timeSpan,
                        String description, String annotation,
                        double complexity, double priceModifier) {
        this.type = type;
        this.timeSpan = timeSpan;
        this.description = description;
        this.annotation = annotation;
        this.complexity = complexity;
        this.priceModifier = priceModifier;
    }

    public LegalService(LegalService.Type type, TimeSpan timeSpan) {
        this.type = type;
        this.timeSpan = timeSpan;
    }

    @Override
    public String toString() {
        return "(" + this.timeSpan.getStart() + " - " + this.getTimeSpan().getEnd() + ")"
                + " " + this.type
                + " " + this.description
                + ((this.annotation != null && !this.annotation.isEmpty()) ? " (" + this.annotation + ")" : "")
                + " complexity: " + this.complexity
                + " price modifier: " + this.priceModifier;
    }


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public TimeSpan getTimeSpan() {
        return timeSpan;
    }

    public void setTimeSpan(TimeSpan timeSpan) {
        this.timeSpan = timeSpan;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public double getComplexity() {
        return complexity;
    }

    public void setComplexity(double complexity) {
        this.complexity = complexity;
    }

    public double getPriceModifier() {
        return priceModifier;
    }

    public void setPriceModifier(double priceModifier) {
        this.priceModifier = priceModifier;
    }
}


