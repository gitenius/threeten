/*
 * Copyright (c) 2012, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package javax.time.calendrical;

import static javax.time.calendrical.LocalDateField.EPOCH_MONTH;
import javax.time.CalendricalException;
import javax.time.DateTimes;
import javax.time.Duration;
import javax.time.LocalDate;
import javax.time.LocalDateTime;
import javax.time.LocalTime;
import javax.time.Period;

/**
 * A standard set of date periods units.
 * <p>
 * These are the basic set of units common across many calendar systems.
 * The calculation part of the units is specific to the ISO calendar system,
 * however the units as concepts may be used as simple constants with other calendar systems.
 * 
 * <h4>Implementation notes</h4>
 * This is a final, immutable and thread-safe enum.
 */
public enum LocalDateUnit implements PeriodUnit {

    /**
     * Unit that represents the concept of a day.
     * For the ISO calendar system, it is the standard day from midnight to midnight.
     * The estimated duration of a day is {@code 24 Hours}.
     * <p>
     * When used with other calendar systems it must correspond to the day defined by
     * the rising and setting of the Sun on Earth. It is not required that days begin
     * at midnight - when converting between calendar systems, the date should be
     * equivalent at midday.
     */
    DAYS("Days", Duration.ofSeconds(86400)),
    /**
     * Unit that represents the concept of a week.
     * For the ISO calendar system, it is equal to 7 days.
     * <p>
     * When used with other calendar systems it must correspond to an integral number of days.
     */
    WEEKS("Weeks", Duration.ofSeconds(7 * 86400L)),
    /**
     * Unit that represents the concept of a month.
     * For the ISO calendar system, the length of the month varies by month-of-year.
     * The estimated duration of a month is one twelfth of {@code 365.2425 Days}.
     * <p>
     * When used with other calendar systems it must correspond to an integral number of days.
     */
    MONTHS("Months", Duration.ofSeconds(31556952L / 12)),
    /**
     * Unit that represents the concept of a quarter-year.
     * For the ISO calendar system, it is equal to 3 months.
     * The estimated duration of a quarter-year is one quarter of {@code 365.2425 Days}.
     * <p>
     * When used with other calendar systems it must correspond to an integral number of days
     * or months roughly equal to one quarter the length of a year.
     */
    QUARTER_YEARS("QuarterYears", Duration.ofSeconds(31556952L / 4)),
    /**
     * Unit that represents the concept of a half-year.
     * For the ISO calendar system, it is equal to 6 months.
     * The estimated duration of a half-year is half of {@code 365.2425 Days}.
     * <p>
     * When used with other calendar systems it must correspond to an integral number of days
     * or months roughly equal to half the length of a year.
     */
    HALF_YEARS("HalfYears", Duration.ofSeconds(31556952L / 2)),
    /**
     * Unit that represents the concept of a year.
     * For the ISO calendar system, it is equal to 12 months.
     * The estimated duration of a year is {@code 365.2425 Days}.
     * <p>
     * When used with other calendar systems it must correspond to an integral number of days
     * or months roughly equal to a year defined by the passage of the Earth around the Sun.
     */
    YEARS("Years", Duration.ofSeconds(31556952L)),
    /**
     * Unit that represents the concept of a decade.
     * For the ISO calendar system, it is equal to 10 years.
     * <p>
     * When used with other calendar systems it must correspond to an integral number of days
     * and is normally an integral number of years.
     */
    DECADES("Decades", Duration.ofSeconds(31556952L * 10L)),
    /**
     * Unit that represents the concept of a century.
     * For the ISO calendar system, it is equal to 100 years.
     * <p>
     * When used with other calendar systems it must correspond to an integral number of days
     * and is normally an integral number of years.
     */
    CENTURIES("Centuries", Duration.ofSeconds(31556952L * 100L)),
    /**
     * Unit that represents the concept of a millenium.
     * For the ISO calendar system, it is equal to 1000 years.
     * <p>
     * When used with other calendar systems it must correspond to an integral number of days
     * and is normally an integral number of years.
     */
    MILLENIA("Millenia", Duration.ofSeconds(31556952L * 1000L)),
    /**
     * Unit that represents the concept of an era.
     * The ISO calendar system doesn't have eras thus it is impossible to add
     * an era to a date or date-time.
     * The estimated duration of the era is artificially defined as {@code 1,000,00,000 Years}.
     * <p>
     * When used with other calendar systems there are no restrictions on the unit.
     */
    ERAS("Eras", Duration.ofSeconds(31556952L * 1000000000L)),
    /**
     * Artificial unit that represents the concept of forever.
     * This is primarily used with {@link DateTimeField} to represent unbounded fields
     * such as the year or era.
     * The estimated duration of the era is artificially defined as the largest duration
     * supported by {@code Duration}.
     */
    FOREVER("Forever", Duration.ofSeconds(Long.MAX_VALUE, 999999999));

    private final String name;
    private final Duration duration;

    private LocalDateUnit(String name, Duration estimatedDuration) {
        this.name = name;
        this.duration = estimatedDuration;
    }

    //-----------------------------------------------------------------------
    @Override
    public String getName() {
        return name;
    }

    //-----------------------------------------------------------------------
    /**
     * Calculates the period in this unit between two dates.
     * <p>
     * This will return the number of complete units between the local dates.
     * If the second date is before the first, the result will be negative.
     * For example, {@code DAYS.between(date1, date2)} will calculate the difference in days.
     * 
     * @param date1  the first date, not null
     * @param date2  the second date, not null
     * @return the period in terms of this unit, not null
     */
    public Period between(LocalDate date1, LocalDate date2) {
        return Period.of(calculateBetween(date1, date2), this);
    }

    /**
     * Calculates the period in this unit between two date-times.
     * <p>
     * This will return the number of complete units between the local date-times.
     * If the second date-time is before the first, the result will be negative.
     * For example, {@code MONTHS.between(dateTime1, dateTime2)} will calculate the difference in months.
     * 
     * @param dateTime1  the first date-time, not null
     * @param dateTime2  the second date-time, not null
     * @return the period in terms of this unit, not null
     */
    public Period between(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return Period.of(calculateBetween(dateTime1, dateTime2), this);
    }

    /**
     * Gets the estimated duration of this unit in the ISO calendar system.
     * <p>
     * All of the units in this class have an estimated duration.
     * Days vary due to daylight savings time, while months have different lengths.
     * 
     * @return the estimated duration of this unit, not null
     */
    @Override
    public Duration getDuration() {
        return duration;
    }

    /**
     * Checks if the duration of the unit is an estimate.
     * <p>
     * All date units in this class are estimated.
     * Days vary due to daylight savings time, while months have different lengths.
     * 
     * @return true always for these date units
     */
    @Override
    public boolean isDurationEstimated() {
        return true;
    }

    //-----------------------------------------------------------------------
    @Override
    public LocalDate calculateAdd(LocalDate date, long amount) {
        if (amount == 0) {
            return date;
        }
        switch (this) {
            case DAYS: return date.plusDays(amount);
            case WEEKS: return date.plusWeeks(amount);
            case MONTHS: return date.plusMonths(amount);
            case QUARTER_YEARS: return date.plusYears(amount / 256).plusMonths((amount % 256) * 3);  // no overflow (256 is multiple of 4)
            case HALF_YEARS: return date.plusYears(amount / 256).plusMonths((amount % 256) * 6);  // no overflow (256 is multiple of 2)
            case YEARS: return date.plusYears(amount);
            case DECADES: return date.plusYears(DateTimes.safeMultiply(amount, 10));
            case CENTURIES: return date.plusYears(DateTimes.safeMultiply(amount, 100));
            case MILLENIA: return date.plusYears(DateTimes.safeMultiply(amount, 1000));
            case ERAS: throw new CalendricalException("Unable to add era, ISO calendar system only has one era");
            case FOREVER: return (amount > 0 ? LocalDate.MAX_DATE : LocalDate.MIN_DATE);
        }
        throw new IllegalStateException("Unreachable");
    }

    @Override
    public LocalTime calculateAdd(LocalTime time, long amount) {
        return time;
    }

    @Override
    public LocalDateTime calculateAdd(LocalDateTime dateTime, long amount) {
        return dateTime.with(calculateAdd(dateTime.toLocalDate(), amount));
    }

    //-----------------------------------------------------------------------
    @Override
    public long calculateBetween(LocalDate date1, LocalDate date2) {
        switch (this) {
            case DAYS: return date2.toEpochDay() - date1.toEpochDay();  // no overflow
            case WEEKS: return DAYS.calculateBetween(date1, date2) / 7;
            case MONTHS: {
                long months = EPOCH_MONTH.getDateRules().get(date2) - EPOCH_MONTH.getDateRules().get(date1);  // no overflow
                return (date2.getDayOfMonth() <= date1.getDayOfMonth() ? months - 1 : months);
            }
            case QUARTER_YEARS: return MONTHS.calculateBetween(date1, date2) / 3;
            case HALF_YEARS: return MONTHS.calculateBetween(date1, date2) / 6;
            case YEARS: {
                long years = ((long) date2.getYear()) - date1.getYear();  // no overflow
                if (date2.getMonth() <= date1.getMonth() || (date2.getMonth() == date1.getMonth() && date2.getDayOfMonth() <= date1.getDayOfMonth())) {
                    years--;
                }
                return years;
            }
            case DECADES: return YEARS.calculateBetween(date1, date2) / 10;
            case CENTURIES: return YEARS.calculateBetween(date1, date2) / 100;
            case MILLENIA: return YEARS.calculateBetween(date1, date2) / 1000;
            case ERAS: return 0;
            case FOREVER: return 0;
        }
        throw new IllegalStateException("Unreachable");
    }

    @Override
    public long calculateBetween(LocalTime time1, LocalTime time2) {
        return 0;
    }

    @Override
    public long calculateBetween(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        LocalDate start = dateTime1.toLocalDate();
        LocalDate end = dateTime2.toLocalDate();
        if (dateTime2.toLocalTime().isBefore(dateTime1.toLocalTime())) {
            end = end.minusDays(1);
        }
        return calculateBetween(start, end);
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
        return getName();
    }

}
