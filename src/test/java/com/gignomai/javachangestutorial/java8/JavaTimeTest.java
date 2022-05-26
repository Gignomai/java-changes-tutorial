package com.gignomai.javachangestutorial.java8;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Assertions.assertThat;

class JavaTimeTest {

    public static final String TEST_DATE_STRING = "2022-05-16";

    @Test
    void shouldCreateAnLocalDate() {
        final LocalDate localDate = LocalDate.now();
        assertThat(localDate.toString()).isEqualTo(LocalDate.now().toString());

        final LocalDate localDateFromParameters = LocalDate.of(2022, 5, 16);
        assertThat(localDateFromParameters.toString()).isEqualTo(TEST_DATE_STRING);

        final LocalDate localDateFromString = LocalDate.parse(TEST_DATE_STRING);
        assertThat(localDateFromString.toString()).isEqualTo(TEST_DATE_STRING);
    }

    @Test
    void shouldModifyDateWithLocalDateMethods() {
        final LocalDate tomorrowFromDate = LocalDate.parse(TEST_DATE_STRING).plusDays(1);
        assertThat(tomorrowFromDate.toString()).isEqualTo("2022-05-17");

        final LocalDate previousMonthSameDay = LocalDate.parse(TEST_DATE_STRING).minus(1, ChronoUnit.MONTHS);
        assertThat(previousMonthSameDay.toString()).isEqualTo("2022-04-16");
    }

    @Test
    void shouldGetDateParts() {
        final DayOfWeek monday = LocalDate.parse(TEST_DATE_STRING).getDayOfWeek();
        assertThat(monday).isEqualTo(DayOfWeek.MONDAY);

        final int sixteen = LocalDate.parse(TEST_DATE_STRING).getDayOfMonth();
        assertThat(sixteen).isEqualTo(16);

        final Month may = LocalDate.parse(TEST_DATE_STRING).getMonth();
        assertThat(may).isEqualTo(Month.MAY);

        final int year = LocalDate.parse(TEST_DATE_STRING).getYear();
        assertThat(year).isEqualTo(2022);
    }

    @Test
    void shouldCheckLeapYear() {
        final boolean leapYear = LocalDate.parse("2016-05-16").isLeapYear();
        assertThat(leapYear).isTrue();

        final boolean notLeapYear = LocalDate.parse(TEST_DATE_STRING).isLeapYear();
        assertThat(notLeapYear).isFalse();
    }

    @Test
    void shouldCheckDateOrder() {
        boolean isBefore = LocalDate.parse(TEST_DATE_STRING).isBefore(LocalDate.parse("2022-05-17"));
        assertThat(isBefore).isTrue();

        boolean isAfter = LocalDate.parse(TEST_DATE_STRING).isAfter(LocalDate.parse("2022-05-15"));
        assertThat(isAfter).isTrue();
    }

    @Test
    void shouldGetDateBoundariesFromDate() {
        LocalDateTime beginningOfDay = LocalDate.parse(TEST_DATE_STRING).atStartOfDay();
        assertThat(beginningOfDay).isEqualTo(LocalDateTime.parse(TEST_DATE_STRING + "T00:00:00"));

        LocalDate firstDayOfMonth = LocalDate.parse(TEST_DATE_STRING).with(TemporalAdjusters.firstDayOfMonth());
        assertThat(firstDayOfMonth).isEqualTo(LocalDate.parse("2022-05-01"));
    }

    @Test
    void shouldCreateALocalDateTime() {
        LocalDateTime first = LocalDateTime.now();
        sleep();
        LocalDateTime second = LocalDateTime.now();

        assertThat(first).isBefore(second);
        assertThat(first.getDayOfMonth()).isEqualTo(second.getDayOfMonth());
        assertThat(first.getMinute()).isEqualTo(second.getMinute());
        assertThat(first.getSecond()).isNotEqualTo(second.getSecond());
    }

    private void sleep() {
        try {
            double pauseLength = (1000 + (Math.random() * 10000));
            Thread.sleep((int) pauseLength);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
