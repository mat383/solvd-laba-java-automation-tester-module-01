package com.solvd.laba.homework02.exercise01;

import java.time.LocalDateTime;

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

    public static final double STANDARD_COMPLEXITY = 1.;
    public static final double UNMODIFIED_PRICE = 1.;

    private LegalService.Type type;
    private TimeSpan timeSpan;
    /**
     * more precise description of service
     */
    private String description;
    /**
     * additional information about service
     */
    private String annotation;
    /**
     * how complex service is (could be used as multiplier when pricing).
     * default is 1, lesser the value lesser the complexity
     * should be 0 < complexity < infinity
     */
    private double complexity;
    /**
     * how to modify price of service, usage depends on pricing algorithm
     * default is 1, lesser the value lesser the complexity
     * should be 0 < priceModifier < infinity
     */
    private double priceModifier;

    public LegalService(LegalService.Type type, TimeSpan timeSpan,
                        String description, String annotation,
                        double complexity, double priceModifier) {
        // TODO change timeStamp to start, end to be more
        //  consistent with rest of the code (Appointment, TimeSpan)
        this.type = type;
        if (timeSpan.isInfinite()) {
            throw new TimeSpanIsInfiniteException("Legal service timeSpan cannot be infinite");
        }
        this.timeSpan = timeSpan;
        this.description = description;
        this.annotation = annotation;
        this.complexity = complexity;
        this.priceModifier = priceModifier;
    }

    public LegalService(LegalService.Type type, TimeSpan timeSpan, String description) {
        this(type, timeSpan, description, "", STANDARD_COMPLEXITY, UNMODIFIED_PRICE);
    }

    public LegalService(LegalService.Type type, TimeSpan timeSpan) {
        this(type, timeSpan, "", "", STANDARD_COMPLEXITY, UNMODIFIED_PRICE);
    }

    @Override
    public String toString() {
        return "(" + this.timeSpan.getStart().map(LocalDateTime::toString).orElse("")
                + " - " + this.timeSpan.getEnd().map(LocalDateTime::toString).orElse("") + ")"
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
        // FIXME timeSpan can be infinite use get/set start / end like appointment,
        // TODO create interface ITimeSpan with default implementations for common methods
        return timeSpan;
    }

    public void setTimeSpan(TimeSpan timeSpan) {
        if (timeSpan.isInfinite()) {
            throw new TimeSpanIsInfiniteException("Legal service timeSpan cannot be infinite");
        }
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


